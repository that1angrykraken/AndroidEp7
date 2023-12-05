package seamonster.kraken.androidep7.data.sources.api

import retrofit2.Response
import retrofit2.http.*
import seamonster.kraken.androidep7.data.models.TokenResponse
import seamonster.kraken.androidep7.data.models.UserCredentials

interface TokenApi {
    @DELETE("oauth/logout")
    suspend fun revokeToken(): Response<Unit>

    @POST("oauth/token/revokeById/{tokenId}")
    suspend fun revokeToken(@Path("tokenId") tokenId: String): Unit

    @GET("tokens")
    suspend fun getTokens(): List<String>

    @GET("tokens/{clientId}")
    suspend fun getTokens(@Path("clientId") clientId: String): List<String>

    @POST("tokens/revokeRefreshToken/{tokenId}")
    suspend fun revokeRefreshToken(@Path("tokenId") tokenId: String): String

    @POST("oauth/token")
    suspend fun oauth(@Body credentials: UserCredentials): Response<TokenResponse>

    companion object {
        const val CLIENT_ID = "core_client" //"core_client"

        const val CLIENT_SECRET = "secret" //"secret"

        const val GRANT_TYPE_PASSWORD = "password"

        const val GRANT_TYPE_REFRESH = "refresh_token"

        const val DEFAULT_SCOPES = "read write delete"
    }
}