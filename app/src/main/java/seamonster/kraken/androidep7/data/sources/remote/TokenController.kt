package seamonster.kraken.androidep7.data.sources.remote

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*
import seamonster.kraken.androidep7.data.models.TokenResponse
import seamonster.kraken.androidep7.data.models.UserCredentials

interface TokenController {
    @DELETE("oauth/logout")
    fun revokeToken(): Call<Unit>

    @POST("oauth/token/revokeById/{tokenId}")
    fun revokeToken(@Path("tokenId") tokenId: String): Call<Unit>

    @GET("tokens")
    fun getTokens(): Call<List<String>>

    @GET("tokens/{clientId}")
    fun getTokens(@Path("clientId") clientId: String): Call<List<String>>

    @POST("tokens/revokeRefreshToken/{tokenId}")
    fun revokeRefreshToken(@Path("tokenId") tokenId: String): Call<String>

    @POST("oauth/token")
    fun loginWithRefreshToken(@Body credentials: UserCredentials): Call<TokenResponse>

    @POST("oauth/token")
    suspend fun oauth(@Body credentials: UserCredentials): TokenResponse

    companion object {
        const val CLIENT_ID = "core_client" //"core_client"

        const val CLIENT_SECRET = "secret" //"secret"

        const val GRANT_TYPE_PASSWORD = "password"

        const val GRANT_TYPE_REFRESH = "refresh_token"

        const val DEFAULT_SCOPES = "read write delete"
    }
}