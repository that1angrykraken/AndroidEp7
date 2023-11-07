package seamonster.kraken.androidep7.ui.auth

import com.airbnb.mvrx.*
import dagger.assisted.*
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.trackingViewModelFactory

data class LoginState(
    val message: Async<String> = Uninitialized
) : MavericksState

class LoginViewModel @AssistedInject constructor(
    @Assisted initialState: LoginState,
//    tokenRepo: TokenRepository,

) : MavericksViewModel<LoginState>(initialState) {

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<LoginViewModel, LoginState> {
        override fun create(state: LoginState): LoginViewModel
    }

    companion object : MavericksViewModelFactory<LoginViewModel, LoginState> by trackingViewModelFactory()
}