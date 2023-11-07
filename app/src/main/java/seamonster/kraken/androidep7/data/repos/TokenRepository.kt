package seamonster.kraken.androidep7.data.repos

import seamonster.kraken.androidep7.data.models.TokenResponse
import seamonster.kraken.androidep7.data.models.UserCredentials
import seamonster.kraken.androidep7.data.sources.local.UserPreferences
import seamonster.kraken.androidep7.data.sources.remote.TokenController
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor(
    val tokenApi: TokenController,
    val userPreferences: UserPreferences
) {

    suspend fun login(username: String, password: String): TokenResponse {
        val body = UserCredentials(
            username = username,
            password = password,
            refreshToken = null,
            grantType = TokenController.GRANT_TYPE_PASSWORD
        )
        return tokenApi.oauth(body)
    }

    suspend fun saveAccessToken(token: TokenResponse) {
        if (token.accessToken != null && token.refreshToken != null) {
            userPreferences.saveAccessTokens(token.accessToken, token.refreshToken)
        }
    }
}