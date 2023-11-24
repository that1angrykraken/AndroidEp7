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

data class SignUpState(
    val signUp: Async<User> = Uninitialized,
    val user: User? = null
) : MavericksState

class SignUpViewModel @AssistedInject constructor(
    @Assisted state: SignUpState,
    private val authRepository: AuthRepository
) : MavericksViewModel<SignUpState>(state) {

    fun handleSignUp(){
        setState { copy(signUp = Loading()) }
        withState { state ->
            suspend {
                if (state.user != null) authRepository.signUp(state.user)
                else error("err 400")
            }.execute { copy(signUp = it) }
        }
    }

    fun setUser(newUser: User?) {
        setState { copy(user = newUser) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<SignUpViewModel, SignUpState> {
        override fun create(state: SignUpState): SignUpViewModel
    }

    companion object : MavericksViewModelFactory<SignUpViewModel, SignUpState> by viewModelFactory()
}