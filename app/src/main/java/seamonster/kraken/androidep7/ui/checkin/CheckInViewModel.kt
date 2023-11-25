package seamonster.kraken.androidep7.ui.checkin

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import seamonster.kraken.androidep7.data.models.TimeSheet
import seamonster.kraken.androidep7.data.repos.TimeSheetRepository
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.viewModelFactory
import seamonster.kraken.androidep7.util.toMessage

data class CheckInState(
    val checkIn: Async<TimeSheet> = Uninitialized,
    val history: Async<List<TimeSheet>> = Uninitialized
) : MavericksState

class CheckInViewModel @AssistedInject constructor(
    @Assisted state: CheckInState,
    private val repository: TimeSheetRepository
) : MavericksViewModel<CheckInState>(state) {

    fun fetchHistory() {
        setState { copy(history = Loading(), checkIn = Uninitialized) }

        suspend {
            val response = repository.fetchCheckInHistory()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                val message = response.errorBody().toMessage()
                throw Throwable(message)
            }
        }.execute { copy(history = it) }
    }

    fun checkIn(ip: String?) {
        setState { copy(checkIn = Loading()) }

        suspend {
            if (ip.isNullOrEmpty()) throw Throwable("IP address not valid!")
            val response = repository.checkIn(ip)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                val message = response.errorBody().toMessage()
                throw Throwable(message)
            }
        }.execute { copy(checkIn = it) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<CheckInViewModel, CheckInState> {
        override fun create(state: CheckInState): CheckInViewModel
    }

    companion object :
        MavericksViewModelFactory<CheckInViewModel, CheckInState> by viewModelFactory()
}