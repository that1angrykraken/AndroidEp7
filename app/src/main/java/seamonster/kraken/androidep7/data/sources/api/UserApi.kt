package seamonster.kraken.androidep7.data.sources.api

import retrofit2.Response
import retrofit2.http.*
import seamonster.kraken.androidep7.data.models.*

interface UserApi {
    @POST("public/sign")
    suspend fun createOrUpdate(@Body user: User): Response<User>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<User>

    @GET("users/get-user-current")
    suspend fun getCurrentUser(): Response<User>

    @GET("users/lock/{id}")
    suspend fun blockUser(@Path("id") id: Int): Response<User>

    @POST("users/searchByPage")
    suspend fun searchByPage(@Body search: Search): Response<Page<User>>

    @GET("users/token-device")
    suspend fun edit(@Query("tokenDevice") tokenDevice: String): Response<User>

    @POST("users/update-myself")
    suspend fun updateMyself(@Body user: User): Response<User>

    @POST("users/update/{id}")
    suspend fun edit(@Body user: User, @Path("id") id: Int): Response<User>
}