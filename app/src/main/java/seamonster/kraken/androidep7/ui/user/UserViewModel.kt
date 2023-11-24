package seamonster.kraken.androidep7.ui.user

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
import retrofit2.Response
import seamonster.kraken.androidep7.data.models.Page
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.data.repos.UserRepository
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.viewModelFactory
import seamonster.kraken.androidep7.util.toMessage

data class UserState(
    val searchResult: Async<Page<User>> = Uninitialized,
    val userAction: Async<User?> = Uninitialized,
    val pageIndex: Int = 0,
) : MavericksState

class UserViewModel @AssistedInject constructor(
    @Assisted state: UserState,
    private val repository: UserRepository
) : MavericksViewModel<UserState>(state) {

    fun fetchAllUsers(nextPage: Boolean = false) {
        setState {
            val nextPageIndex = if (nextPage) pageIndex + 1 else 0
            copy(searchResult = Loading(), pageIndex = nextPageIndex)
        }

        withState { state ->
            viewModelScope.launch {
                try {
                    val response = repository.fetchSearchResult("", state.pageIndex)
                    if (response.isSuccessful && response.body() != null){
                        setState { copy(searchResult = Success(response.body()!!)) }
                    } else {
                        val message = response.errorBody().toMessage()
                        setState { copy(searchResult = Fail(Throwable(message))) }
                    }
                } catch (e: Exception) {
                    setState { copy(searchResult = Fail(e)) }
                }
            }
        }
    }

    fun blockUser(id: Int) = executionScope { repository.blockUser(id) }

    fun fetchUser(id: Int) = executionScope { repository.fetchUser(id) }

    fun resetUserAction() = setState { copy(userAction = Uninitialized) }

    fun updateUser(user: User) = executionScope { repository.updateUser(user) }

    private fun executionScope(scope: suspend () -> Response<User>) {
        setState { copy(userAction = Loading()) }

        viewModelScope.launch {
            try {
                val response = scope.invoke()
                if (response.isSuccessful) {
                    setState { copy(userAction = Success(response.body())) }
                } else {
                    val message = response.errorBody().toMessage()
                    setState { copy(userAction = Fail(Throwable(message))) }
                }
            } catch (e: Exception) {
                setState { copy(userAction = Fail(e)) }
            }
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<UserViewModel, UserState> {
        override fun create(state: UserState): UserViewModel
    }

    companion object : MavericksViewModelFactory<UserViewModel, UserState> by viewModelFactory() {
        private const val TAG = "UserViewModel"
    }
}