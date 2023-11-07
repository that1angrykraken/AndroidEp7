package seamonster.kraken.androidep7.data.sources.remote

import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.*
import seamonster.kraken.androidep7.data.models.*

interface UserRestController {
    @POST("public/sign")
    fun createOrUpdate(@Body user: User): Call<User>

    @GET("users/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

    @GET("users/get-user-current")
    suspend fun getCurrentUser(): User

    @GET("users/lock/{id}")
    fun blockUser(@Path("id") id: Int): Call<User>

    @POST("users/searchByPage")
    fun searchByPage(@Body search: Search): Call<Page<User>>

    @GET("users/token-device")
    fun edit(@Query("token-device") tokenDevice: String): Call<User>

    @POST("users/update-myself")
    fun updateMyself(@Body user: User): Call<User>

    @POST("users/update/{id}")
    fun edit(@Body user: User, @Path("id") id: Int): Call<User>
}