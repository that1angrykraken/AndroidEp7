package seamonster.kraken.androidep7.data.sources

import android.annotation.SuppressLint
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import seamonster.kraken.androidep7.BuildConfig
import java.io.IOException
import java.net.CookieManager
import java.net.CookiePolicy
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.*

@Singleton
class RemoteDataSource @Inject constructor(
    private val tokenPreferences: TokenPreferences
) {

    companion object {
        private const val BASE_URL = "http://android-tracking.oceantech.com.vn/mita/"
        private const val DEFAULT_USER_AGENT = "AndroidEp7"
        private const val DEFAULT_CONTENT_TYPE = "application/json"
    }

    fun <Api> create(dataSourceClass: Class<Api>): Api {
        val authenticator = TokenAuthenticator(tokenPreferences)

        val gson = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeHierarchyAdapter(Calendar::class.java, CalendarTypeAdapter())
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getRetrofitClient(authenticator))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(dataSourceClass)
    }

    private fun getRetrofitClient(authenticator: Authenticator): OkHttpClient {

        return getUnsafeOkHttpClient()
            .writeTimeout(31, TimeUnit.SECONDS)
            .readTimeout(31, TimeUnit.SECONDS)
            .connectTimeout(31, TimeUnit.SECONDS)
            .cookieJar(cookieJar())
            .addNetworkInterceptor(customInterceptor())
            .also { client ->
                client.authenticator(authenticator)
                if (BuildConfig.DEBUG) {
                    client.addInterceptor(loggingInterceptor())
                }
            }
            .build()
    }

    private fun cookieJar(): CookieJar {
        val cookieManager = CookieManager().apply {
            setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        }
        return JavaNetCookieJar(cookieManager)
    }

    private fun loggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun customInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val request: Request = original.newBuilder() // rewrite the request
                .header("User-Agent", DEFAULT_USER_AGENT)
                .header("Accept", DEFAULT_CONTENT_TYPE)
                .header("Content-Type", DEFAULT_CONTENT_TYPE)
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder =
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf(
                @SuppressLint("CustomX509TrustManager")
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) = Unit

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) = Unit

                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                }
            )
            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            OkHttpClient.Builder()
                .sslSocketFactory(
                    sslSocketFactory,
                    trustAllCerts[0] as X509TrustManager
                )
                .hostnameVerifier { _, _ -> true }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
}

class CalendarTypeAdapter : TypeAdapter<Calendar>() {

    companion object {
        private const val PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    }

    private val defaultTimeZone = TimeZone.getTimeZone("GMT+00:00")

    private val dateFormat = SimpleDateFormat(PATTERN, Locale.getDefault()).apply {
        timeZone = defaultTimeZone
    }

    @Throws(IOException::class)
    override fun write(writer: JsonWriter?, value: Calendar?) {
        if (value == null) {
            writer?.nullValue()
        } else {
            writer?.value(dateFormat.format(value.time))
        }
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader?): Calendar? {
        if (reader?.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }

        val dateString = reader?.nextString()
        if (dateString != null) {
            try {
                val calendar = Calendar.getInstance()
                val date = dateFormat.parse(dateString) as Date
                calendar.time = date
                return calendar
            } catch (e: ParseException) {
                throw IOException(e)
            }
        }
        return null
    }
}