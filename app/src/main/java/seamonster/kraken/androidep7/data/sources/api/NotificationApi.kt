package seamonster.kraken.androidep7.data.sources.api

import retrofit2.Response
import retrofit2.http.GET
import seamonster.kraken.androidep7.data.models.Notification

interface NotificationApi {

    @GET("notifications")
    suspend fun getAll(): Response<List<Notification>>
}