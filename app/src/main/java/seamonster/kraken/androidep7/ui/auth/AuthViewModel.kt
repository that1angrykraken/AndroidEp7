package seamonster.kraken.androidep7.ui.auth

import android.util.Log
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import seamonster.kraken.androidep7.data.models.TokenResponse
import seamonster.kraken.androidep7.data.repos.TokenRepository
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.trackingViewModelFactory

data class AuthState(
    val userLogin: Async<TokenResponse> = Uninitialized
) : MavericksState

class AuthViewModel @AssistedInject constructor(
    @Assisted state: AuthState,
    val tokenRepository: TokenRepository
) : MavericksViewModel<AuthState>(state) {

    fun handle(action: LoginAction) {
        Log.d(TAG, "handle: got action ${action.javaClass.simpleName}")
        when (action) {
            LoginAction.ResetPassword -> {}
            LoginAction.SignUp -> {}
            is LoginAction.Submit -> login(action.username, action.password)
        }
    }

    private fun login(username: String, password: String) {
        setState { copy(userLogin = Loading()) }
        if (!isLoginInformationValid(username, password)) {
            val error = IllegalArgumentException("Username or password is invalid!")
            setState {
                copy(userLogin = Fail(error))
            }
        } else {
            suspend {
                tokenRepository.login(username, password)
            }.execute { copy(userLogin = it) }
        }
    }

    private fun isLoginInformationValid(username: String, password: String): Boolean {
        return username.isNotBlank() && password.isNotBlank()
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<AuthViewModel, AuthState> {
        override fun create(state: AuthState): AuthViewModel
    }

    companion object :
        MavericksViewModelFactory<AuthViewModel, AuthState> by trackingViewModelFactory() {
        private const val TAG = "AuthViewModel"
    }
}