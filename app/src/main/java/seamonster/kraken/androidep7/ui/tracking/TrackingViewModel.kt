package seamonster.kraken.androidep7.ui.tracking

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.Response
import seamonster.kraken.androidep7.data.models.Tracking
import seamonster.kraken.androidep7.data.repos.TrackingRepository
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.viewModelFactory
import seamonster.kraken.androidep7.util.toMessage

data class TrackingState(
    val timeline: Async<List<Tracking>> = Uninitialized,
    val trackingAction: Async<Tracking> = Uninitialized
) : MavericksState

class TrackingViewModel @AssistedInject constructor(
    @Assisted state: TrackingState,
    private val repository: TrackingRepository
) : MavericksViewModel<TrackingState>(state) {

    fun fetchAll() {
        setState { copy(trackingAction = Loading()) }
        suspend {
            val response = repository.getTrackingTimeline()
            if (response.isSuccessful && response.body() != null) response.body()!!
            else {
                val message = response.errorBody().toMessage()
                throw Throwable(message)
            }
        }.execute { copy(timeline = it) }
    }

    fun saveTracking(tracking: Tracking) = executionScope { repository.saveTracking(tracking) }

    fun deleteTracking(id: Int?) {
        if (id == null) {
            setState { copy(trackingAction = Fail(Throwable("Invalid id!"))) }
            return
        }
        executionScope { repository.deleteTracking(id) }
    }

    fun updateTracking(tracking: Tracking) = executionScope { repository.updateTracking(tracking) }

    private fun executionScope(block: suspend () -> Response<Tracking>) {
        setState { copy(trackingAction = Loading()) }
        suspend {
            val response = block.invoke()
            if (response.isSuccessful && response.body() != null) response.body()!!
            else {
                val message = response.errorBody().toMessage()
                throw Throwable(message)
            }
        }.execute { copy(trackingAction = it) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<TrackingViewModel, TrackingState> {
        override fun create(state: TrackingState): TrackingViewModel
    }

    companion object :
        MavericksViewModelFactory<TrackingViewModel, TrackingState> by viewModelFactory()
}