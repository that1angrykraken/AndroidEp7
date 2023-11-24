package seamonster.kraken.androidep7.data.sources

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenPreferences: TokenPreferences
) : Authenticator {

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val BASIC = "Basic Y29yZV9jbGllbnQ6c2VjcmV0"
    }

    private var mAccessToken: String? = ""

    override fun authenticate(route: Route?, response: Response): Request? {
        mAccessToken = tokenPreferences.authToken
        val bearer = "Bearer $mAccessToken"

        val authorizationHeader = response.request.header(AUTHORIZATION)
        val credential = if (!mAccessToken.isNullOrEmpty()) bearer else BASIC

        return if (credential != authorizationHeader) {
            response.request.newBuilder().header(AUTHORIZATION, credential).build()
        } else {
            tokenPreferences.authToken = ""
            null // If failed to authenticate, give up
        }
    }
}