package seamonster.kraken.androidep7.data.sources.remote

import retrofit2.Call
import retrofit2.http.*
import seamonster.kraken.androidep7.data.models.Comment

interface TrackingRestController {
    @GET("tracking")
    fun getAllByUser(): Call<Comment>

    @POST("tracking")
    fun save(@Body comment: Comment): Call<Comment>

    @POST("tracking/{id}")
    fun update(@Body comment: Comment, @Path("id") id: Int) : Call<Comment>

    @DELETE("tracking/{id}")
    fun delete(@Path("id") id: Int): Call<Comment>
}