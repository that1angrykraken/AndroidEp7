package seamonster.kraken.androidep7.data.sources.api

import retrofit2.http.Body
import retrofit2.http.POST
import seamonster.kraken.androidep7.data.models.User

interface PublicApi {

    @POST("public/sign")
    suspend fun signUp(@Body user: User): User
}