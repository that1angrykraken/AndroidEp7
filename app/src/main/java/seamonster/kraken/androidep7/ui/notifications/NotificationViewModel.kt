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

        val onSuccess: NotificationState.(Response<List<Notification>>) -> NotificationState =
            {
                val async = if (it.isSuccessful) {
                    Success(it.body() ?: emptyList())
                } else {
                    val message = it.errorBody().toMessage()
                    Fail(Throwable(message))
                }
                copy(notifications = async)
            }

        suspend { repository.fetchAll() }
            .execute { response ->
                when (response) {
                    is Success -> onSuccess(this@execute, response.invoke())
                    is Fail -> copy(notifications = Fail(response.error))
                    else -> this
                }
            }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<NotificationViewModel, NotificationState> {
        override fun create(state: NotificationState): NotificationViewModel
    }

    companion object :
        MavericksViewModelFactory<NotificationViewModel, NotificationState> by viewModelFactory()
}