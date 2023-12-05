package seamonster.kraken.androidep7.data.repos

import retrofit2.Response
import seamonster.kraken.androidep7.data.models.TokenResponse
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.data.models.UserCredentials
import seamonster.kraken.androidep7.data.sources.TokenPreferences
import seamonster.kraken.androidep7.data.sources.api.TokenApi
import seamonster.kraken.androidep7.data.sources.api.UserApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val tokenApi: TokenApi,
    private val userApi: UserApi,
    private val tokenPreferences: TokenPreferences
) {

    suspend fun login(username: String, password: String): Response<TokenResponse> {
        val body = UserCredentials(
            username = username,
            password = password,
            grantType = TokenApi.GRANT_TYPE_PASSWORD
        )
        return tokenApi.oauth(body)
    }

    suspend fun signUp(user: User) = userApi.createOrUpdate(user)


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