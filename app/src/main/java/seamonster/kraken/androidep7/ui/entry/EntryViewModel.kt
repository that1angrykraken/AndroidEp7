package seamonster.kraken.androidep7.ui.entry

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import seamonster.kraken.androidep7.data.repos.UserRepository
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.viewModelFactory

data class EntryState(
    val currentUser: Async<Unit> = Uninitialized
) : MavericksState

class EntryViewModel @AssistedInject constructor(
    @Assisted state: EntryState,
    private val repository: UserRepository
) : MavericksViewModel<EntryState>(state) {

    val currentUser = repository.currentUserLiveData

    fun fetchCurrentUser() {
        setState { copy(currentUser = Loading()) }
        suspend {
            repository.fetchCurrentUser()
        }.execute { copy(currentUser = it) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<EntryViewModel, EntryState> {
        override fun create(state: EntryState): EntryViewModel
    }

    companion object :
        MavericksViewModelFactory<EntryViewModel, EntryState> by viewModelFactory()
}