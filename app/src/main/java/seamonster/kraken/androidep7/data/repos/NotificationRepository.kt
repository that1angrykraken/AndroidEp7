package seamonster.kraken.androidep7.data.repos

import seamonster.kraken.androidep7.data.sources.api.NotificationApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val api: NotificationApi
) {

    suspend fun fetchAll() = api.getAll()
}