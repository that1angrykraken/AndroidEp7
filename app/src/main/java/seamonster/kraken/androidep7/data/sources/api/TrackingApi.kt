package seamonster.kraken.androidep7.data.sources.api

import retrofit2.Response
import retrofit2.http.*
import seamonster.kraken.androidep7.data.models.Tracking

interface TrackingApi {
    @GET("tracking")
    suspend fun getAllByUser(): Response<List<Tracking>>

    @POST("tracking")
    suspend fun save(@Body tracking: Tracking): Response<Tracking>

    @POST("tracking/{id}")
    suspend fun update(@Body tracking: Tracking, @Path("id") id: Int) : Response<Tracking>

    @DELETE("tracking/{id}")
    suspend fun delete(@Path("id") id: Int): Response<Tracking>
}