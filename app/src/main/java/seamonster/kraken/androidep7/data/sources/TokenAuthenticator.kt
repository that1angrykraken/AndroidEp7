package seamonster.kraken.androidep7.data.sources

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import seamonster.kraken.androidep7.data.models.UserCredentials
import seamonster.kraken.androidep7.data.sources.api.TokenApi
import seamonster.kraken.androidep7.util.Errors
import seamonster.kraken.androidep7.util.toMessage

class TokenAuthenticator (
    private val preferences: TokenPreferences
) : Authenticator {

    companion object {
        private const val TAG = "TokenAuthenticator"

        private const val AUTHORIZATION = "Authorization"
        private const val BASIC = "Basic Y29yZV9jbGllbnQ6c2VjcmV0"
        private const val BEARER_POSTFIX = "Bearer "
    }

    private var mAccessToken: String? = ""

    private var mRequestNewToken = false

    private var mRequestNewTokenFailed = false

    override fun authenticate(route: Route?, response: Response): Request? {
        mAccessToken = preferences.authToken

        val authorizationHeader = response.request.header(AUTHORIZATION)
        val credential = if (mAccessToken.isNullOrEmpty()) BASIC else bearerToken()

        if (credential == authorizationHeader) {
            fetchNewToken()
        }

        if (mRequestNewTokenFailed) {
            mRequestNewTokenFailed = false
            mRequestNewToken = false
            return null
        }

        return response.request
            .newBuilder()
            .header(AUTHORIZATION, credential)
            .build()
    }

    private fun bearerToken() = BEARER_POSTFIX + mAccessToken

    private fun fetchNewToken() {
        if (mRequestNewToken) return
        mRequestNewToken = true
        val refreshToken = preferences.authTokenRefresh
        if (refreshToken.isNullOrEmpty()) error(Errors.UNAUTHORIZED)
        preferences.authToken = ""
        val api = RemoteDataSource(preferences).create(TokenApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.oauth(
                    UserCredentials(
                        clientId = TokenApi.CLIENT_ID,
                        clientSecret = TokenApi.CLIENT_SECRET,
                        refreshToken = refreshToken,
                        grantType = TokenApi.GRANT_TYPE_REFRESH
                    )
                )
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    preferences.authToken = body.accessToken
                    preferences.authTokenRefresh = body.refreshToken
                } else {
                    val message = response.errorBody().toMessage() ?: Errors.INVALID_TOKEN
                    error(message)
                }
            } catch (e: Exception) {
                Log.e(TAG, "fetchNewToken: ", e)
                mRequestNewTokenFailed = true
            }
        }
    }

}