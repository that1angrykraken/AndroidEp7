package seamonster.kraken.androidep7.ui.notifications

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
import retrofit2.Response
import seamonster.kraken.androidep7.data.models.Notification
import seamonster.kraken.androidep7.data.repos.NotificationRepository
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.viewModelFactory
import seamonster.kraken.androidep7.util.toMessage

data class NotificationState(
    val notifications: Async<List<Notification>> = Uninitialized
) : MavericksState

class NotificationViewModel @AssistedInject constructor(
    @Assisted state: NotificationState,
    private val repository: NotificationRepository
) : MavericksViewModel<NotificationState>(state) {

    fun fetchAll() {
        setState { copy(notifications = Loading()) }

        suspend {
            val response = repository.fetchAll()
            if (response.isSuccessful && response.body() != null) response.body()!!
            else {
                val message = response.errorBody().toMessage()
                throw Throwable(message)
            }
        }.execute { copy(notifications = it) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<NotificationViewModel, NotificationState> {
        override fun create(state: NotificationState): NotificationViewModel
    }

    companion object :
        MavericksViewModelFactory<NotificationViewModel, NotificationState> by viewModelFactory()
}