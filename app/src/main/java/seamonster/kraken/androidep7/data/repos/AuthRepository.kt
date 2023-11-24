package seamonster.kraken.androidep7.data.repos

import seamonster.kraken.androidep7.data.models.TokenResponse
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.data.models.UserCredentials
import seamonster.kraken.androidep7.data.sources.TokenPreferences
import seamonster.kraken.androidep7.data.sources.api.PublicApi
import seamonster.kraken.androidep7.data.sources.api.TokenApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val tokenApi: TokenApi,
    private val publicApi: PublicApi,
    private val tokenPreferences: TokenPreferences
) {

    suspend fun login(username: String, password: String): TokenResponse {
        val body = UserCredentials(
            username = username,
            password = password,
            grantType = TokenApi.GRANT_TYPE_PASSWORD
        )
        return tokenApi.oauth(body)
    }

    suspend fun loginWithRefreshToken(): TokenResponse {
        val refreshToken = getRefreshToken()
        if (refreshToken.isNullOrEmpty()) {
            throw IllegalArgumentException("Refresh token not found!")
        }
        val body = UserCredentials(
            refreshToken = refreshToken,
            grantType = TokenApi.GRANT_TYPE_REFRESH
        )
        return tokenApi.loginWithRefreshToken(body)
    }

    suspend fun signUp(user: User): User {
        return publicApi.signUp(user)
    }

    suspend fun logout() = tokenApi.revokeToken()

    fun clearToken() {
        tokenPreferences.authToken = ""
        tokenPreferences.authTokenRefresh = ""
    }

    fun saveAccessToken(token: TokenResponse) {
        if (!token.accessToken.isNullOrEmpty()) {
            tokenPreferences.authToken = token.accessToken
        }
        if (!token.refreshToken.isNullOrEmpty()){
            tokenPreferences.authTokenRefresh = token.refreshToken
        }
    }

    fun getRefreshToken(): String? = tokenPreferences.authTokenRefresh

}