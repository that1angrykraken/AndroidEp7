package seamonster.kraken.androidep7.ui.login

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import seamonster.kraken.androidep7.data.models.TokenResponse
import seamonster.kraken.androidep7.data.repos.AuthRepository
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.viewModelFactory
import seamonster.kraken.androidep7.util.toMessage

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

    fun login(username: String, password: String) {
        setState { copy(login = Loading()) }
        suspend {
            val response = repository.login(username, password)
            if (response.isSuccessful) response.body()!!
            else {
                val message = response.errorBody().toMessage()
                throw Throwable(message)
            }
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