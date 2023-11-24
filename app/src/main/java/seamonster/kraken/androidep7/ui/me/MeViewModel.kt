package seamonster.kraken.androidep7.ui.me

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import seamonster.kraken.androidep7.data.repos.AuthRepository
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.viewModelFactory
import seamonster.kraken.androidep7.util.toMessage

data class MeState(val logout: Async<Unit> = Uninitialized): MavericksState

class MeViewModel @AssistedInject constructor(
    @Assisted state: MeState,
    private val authRepository: AuthRepository
): MavericksViewModel<MeState>(state) {

    fun logout() {
        setState { copy(logout = Loading()) }
        suspend {
            val response = authRepository.logout()
            if (response.isSuccessful) response.body()!!
            else {
                val message = response.errorBody().toMessage()
                throw Throwable(message)
            }
        }.execute { copy(logout = it) }
    }

    fun clearToken() = authRepository.clearToken()

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<MeViewModel, MeState>{
        override fun create(state: MeState): MeViewModel
    }

    companion object: MavericksViewModelFactory<MeViewModel, MeState> by viewModelFactory()
}