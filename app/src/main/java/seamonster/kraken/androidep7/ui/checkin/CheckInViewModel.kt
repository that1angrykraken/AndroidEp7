package seamonster.kraken.androidep7.ui.checkin

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import seamonster.kraken.androidep7.data.models.TimeSheet
import seamonster.kraken.androidep7.data.repos.TimeSheetRepository
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.viewModelFactory
import seamonster.kraken.androidep7.util.toMessage

data class CheckInState(
    val checkIn: Async<TimeSheet> = Uninitialized,
    val history: Async<List<TimeSheet>> = Uninitialized,
    val shouldCheckIn: Boolean = true
) : MavericksState

class CheckInViewModel @AssistedInject constructor(
    @Assisted state: CheckInState,
    private val repository: TimeSheetRepository
) : MavericksViewModel<CheckInState>(state) {

    fun fetchHistory() {
        setState { copy(history = Loading(), shouldCheckIn = false) }

        suspend {
            val response = repository.fetchCheckInHistory()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                val message = response.errorBody().toMessage()
                throw Throwable(message)
            }
        }.execute { copy(history = it, shouldCheckIn = true) }
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
        }.execute { copy(checkIn = it, shouldCheckIn = true) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<CheckInViewModel, CheckInState> {
        override fun create(state: CheckInState): CheckInViewModel
    }

    companion object :
        MavericksViewModelFactory<CheckInViewModel, CheckInState> by viewModelFactory()
}