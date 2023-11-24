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

    private fun executionScope(scope: suspend () -> Response<User>) {
        setState { copy(currentUser = Loading()) }

        val onSuccess: MainState.(Response<User>) -> MainState = {
            val asyncState = if (it.isSuccessful) {
                Success(it.body())
            } else {
                val message = it.errorBody().toMessage()
                Fail(Throwable(message))
            }
            copy(currentUser = asyncState)
        }

        scope.execute { result ->
            when (result) {
                is Success -> onSuccess(this@execute, result.invoke())
                is Fail -> copy(currentUser = Fail(result.error))
                else -> this
            }
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<MainViewModel, MainState> {
        override fun create(state: MainState): MainViewModel
    }

    companion object : MavericksViewModelFactory<MainViewModel, MainState> by viewModelFactory()
}