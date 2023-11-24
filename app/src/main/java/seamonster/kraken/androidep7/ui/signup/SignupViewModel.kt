package seamonster.kraken.androidep7.ui.signup

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
import seamonster.kraken.androidep7.data.repos.AuthRepository
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.viewModelFactory
import seamonster.kraken.androidep7.util.toMessage

data class SignUpState(
    val signUp: Async<User> = Uninitialized
) : MavericksState

class SignupViewModel @AssistedInject constructor(
    @Assisted state: SignUpState,
    private val authRepository: AuthRepository
) : MavericksViewModel<SignUpState>(state) {

    fun signUp(user: User) {
        setState { copy(signUp = Loading()) }
        suspend {
            val response = authRepository.signUp(user)
            if (response.isSuccessful) response.body()!!
            else {
                val message = response.errorBody().toMessage()
                throw Throwable(message)
            }
        }.execute { copy(signUp = it) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<SignupViewModel, SignUpState> {
        override fun create(state: SignUpState): SignupViewModel
    }

    companion object : MavericksViewModelFactory<SignupViewModel, SignUpState> by viewModelFactory()
}