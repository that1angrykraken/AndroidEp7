package seamonster.kraken.androidep7.ui.login

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import seamonster.kraken.androidep7.data.models.TokenResponse
import seamonster.kraken.androidep7.data.repos.AuthRepository
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.viewModelFactory

data class LoginState(
    val login: Async<TokenResponse> = Uninitialized,
) : MavericksState

class LoginViewModel @AssistedInject constructor(
    @Assisted state: LoginState,
    private val repository: AuthRepository
) : MavericksViewModel<LoginState>(state) {

    fun saveToken(token: TokenResponse) {
        repository.saveAccessToken(token)
    }

    fun login(username: String?, password: String?) {
        setState { copy(login = Loading()) }
        suspend {
            if (!username.isNullOrBlank() && !password.isNullOrBlank()) {
                repository.login(username, password)
            } else error("err 400")
        }.execute { copy(login = it) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<LoginViewModel, LoginState> {
        override fun create(state: LoginState): LoginViewModel
    }

    companion object : MavericksViewModelFactory<LoginViewModel, LoginState> by viewModelFactory() {
        private const val TAG = "AuthViewModel"
    }
}