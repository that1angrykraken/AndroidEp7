package seamonster.kraken.androidep7.ui.entry

import android.util.Log
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.data.repos.UserRepository
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.trackingViewModelFactory

data class EntryState(
    val currentUser: Async<User> = Uninitialized
) : MavericksState

class EntryViewModel @AssistedInject constructor(
    @Assisted state: EntryState,
    private val userRepository: UserRepository
) : MavericksViewModel<EntryState>(state) {

    fun handle(action: EntryAction) {
        Log.d(TAG, "handle: got action ${action.javaClass.simpleName}")
        when (action) {
            is EntryAction.GetCurrentUser -> handleCurrentUser()
        }
    }

    private fun handleCurrentUser() {
        setState {
            copy(currentUser = Loading())
        }
        suspend { userRepository.getCurrentUser() }.execute { copy(currentUser = it) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<EntryViewModel, EntryState> {
        override fun create(state: EntryState): EntryViewModel
    }

    companion object :
        MavericksViewModelFactory<EntryViewModel, EntryState> by trackingViewModelFactory() {
        private const val TAG = "EntryViewModel"
    }
}