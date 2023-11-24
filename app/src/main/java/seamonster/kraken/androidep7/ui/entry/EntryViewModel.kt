package seamonster.kraken.androidep7.ui.entry

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
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.data.repos.UserRepository
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.viewModelFactory
import seamonster.kraken.androidep7.util.toMessage

data class EntryState(
    val currentUser: Async<User?> = Uninitialized
) : MavericksState

class EntryViewModel @AssistedInject constructor(
    @Assisted state: EntryState,
    private val userRepository: UserRepository
) : MavericksViewModel<EntryState>(state) {

    fun fetchCurrentUser() {
        setState { copy(currentUser = Loading()) }

        viewModelScope.launch {
            try {
                val response = userRepository.fetchCurrentUser()
                val async = if (response.isSuccessful) {
                    Success(response.body())
                }
                else {
                    val message = response.errorBody().toMessage()
                    Fail(Throwable(message))
                }
                setState { copy(currentUser = async) }
            } catch (e: Exception) {
                setState { copy(currentUser = Fail(e)) }
            }
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<EntryViewModel, EntryState> {
        override fun create(state: EntryState): EntryViewModel
    }

    companion object :
        MavericksViewModelFactory<EntryViewModel, EntryState> by viewModelFactory() {
        private const val TAG = "EntryViewModel"
    }
}