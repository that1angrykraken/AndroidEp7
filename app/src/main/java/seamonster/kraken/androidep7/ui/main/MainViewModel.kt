package seamonster.kraken.androidep7.ui.main

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
import retrofit2.Response
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.data.repos.UserRepository
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.viewModelFactory
import seamonster.kraken.androidep7.util.toMessage

data class MainState(
    val currentUser: Async<User?> = Uninitialized
) : MavericksState

class MainViewModel @AssistedInject constructor(
    @Assisted state: MainState,
    private val repository: UserRepository
) : MavericksViewModel<MainState>(state) {

    fun fetchCurrentUser() = executionScope { repository.fetchCurrentUser() }

    fun updateMyself(user: User) = executionScope { repository.updateMyself(user) }

    private fun executionScope(block: suspend () -> Response<User>) {
        setState { copy(currentUser = Loading()) }
        suspend {
            val response = block.invoke()
            if (response.isSuccessful) response.body()
            else {
                val message = response.errorBody().toMessage()
                throw Throwable(message)
            }
        }.execute { copy(currentUser = it)}
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<MainViewModel, MainState> {
        override fun create(state: MainState): MainViewModel
    }

    companion object : MavericksViewModelFactory<MainViewModel, MainState> by viewModelFactory()
}