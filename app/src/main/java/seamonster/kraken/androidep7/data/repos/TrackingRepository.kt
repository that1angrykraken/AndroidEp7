package seamonster.kraken.androidep7.data.repos

import seamonster.kraken.androidep7.data.models.Tracking
import seamonster.kraken.androidep7.data.sources.api.TrackingApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackingRepository @Inject constructor(private val trackingApi: TrackingApi) {

    suspend fun getTrackingTimeline() = trackingApi.getAllByUser()

    suspend fun saveTracking(tracking: Tracking) = trackingApi.save(tracking)

    suspend fun updateTracking(tracking: Tracking) = trackingApi.update(tracking, tracking.id!!)

    suspend fun deleteTracking(id: Int) = trackingApi.delete(id)
}