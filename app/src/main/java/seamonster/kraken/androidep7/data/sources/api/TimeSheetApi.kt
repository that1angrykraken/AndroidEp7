package seamonster.kraken.androidep7.data.sources.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import seamonster.kraken.androidep7.data.models.TimeSheet

interface TimeSheetApi {

    @GET("time-sheets")
    suspend fun getAll(): Response<List<TimeSheet>>

    @GET("time-sheets/check-in")
    suspend fun checkIn(@Query("ip") ip: String): Response<TimeSheet>
}