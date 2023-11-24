package seamonster.kraken.androidep7.data.repos

import seamonster.kraken.androidep7.data.sources.api.TimeSheetApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeSheetRepository @Inject constructor(private val api: TimeSheetApi) {

    suspend fun fetchCheckInHistory() = api.getAll()

    suspend fun checkIn(ip: String) = api.checkIn(ip)
}