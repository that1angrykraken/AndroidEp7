package seamonster.kraken.androidep7.ui.user

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
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

data class UserState(
    val searchResult: Async<Unit> = Uninitialized,
    val userAction: Async<User?> = Uninitialized,
    val pageIndex: Int = 0,
) : MavericksState

class UserViewModel @AssistedInject constructor(
    @Assisted state: UserState,
    private val repository: UserRepository
) : MavericksViewModel<UserState>(state) {

    val currentUser = repository.currentUserLiveData
    val userPage = repository.pageLiveData

    fun fetchAllUsers(nextPage: Boolean = false) {
        setState {
            val nextPageIndex = if (nextPage) pageIndex + 1 else 0
            copy(searchResult = Loading(), pageIndex = nextPageIndex)
        }

        withState { state ->
            suspend {
                repository.fetchSearchResult("", state.pageIndex)
            }.execute { copy(searchResult = it) }
        }
    }

    fun blockUser(id: Int) = executionScope { repository.blockUser(id) }

    fun fetchUser(id: Int) = executionScope { repository.fetchUser(id) }

    fun resetUserAction() = setState { copy(userAction = Uninitialized) }

    fun updateUser(user: User) = executionScope { repository.updateUser(user) }

    private fun executionScope(scope: suspend () -> Response<User>) {
        setState { copy(userAction = Loading()) }
        suspend {
            val response = scope.invoke()
            if (response.isSuccessful) response.body()
            else {
                val message = response.errorBody().toMessage()
                throw Throwable(message)
            }
        }.execute { copy(userAction = it) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<UserViewModel, UserState> {
        override fun create(state: UserState): UserViewModel
    }

    companion object : MavericksViewModelFactory<UserViewModel, UserState> by viewModelFactory()
}