package seamonster.kraken.androidep7.ui.main

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.data.repos.UserRepository
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.viewModelFactory

data class MainState(
    val currentUser: Async<Unit> = Uninitialized
) : MavericksState

class MainViewModel @AssistedInject constructor(
    @Assisted state: MainState,
    private val repository: UserRepository
) : MavericksViewModel<MainState>(state) {

    val currentUser = repository.currentUserLiveData

    fun fetchCurrentUser() = executionScope { repository.fetchCurrentUser() }

    fun updateMyself(user: User) = executionScope { repository.updateMyself(user) }

    private fun executionScope(block: suspend () -> Unit) {
        setState { copy(currentUser = Loading()) }
        suspend {
            block.invoke()
        }.execute { copy(currentUser = it)}
    }

    fun registerTokenDevice(tokenDevice: String) = executionScope {
        repository.registerTokenDevice(tokenDevice)
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<MainViewModel, MainState> {
        override fun create(state: MainState): MainViewModel
    }

    companion object : MavericksViewModelFactory<MainViewModel, MainState> by viewModelFactory()
}