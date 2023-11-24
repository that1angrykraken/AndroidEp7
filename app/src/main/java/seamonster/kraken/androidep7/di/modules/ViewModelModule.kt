package seamonster.kraken.androidep7.di.modules

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.ViewModelKey
import seamonster.kraken.androidep7.ui.checkin.CheckInViewModel
import seamonster.kraken.androidep7.ui.login.LoginViewModel
import seamonster.kraken.androidep7.ui.entry.EntryViewModel
import seamonster.kraken.androidep7.ui.main.MainViewModel
import seamonster.kraken.androidep7.ui.me.MeViewModel
import seamonster.kraken.androidep7.ui.notifications.NotificationViewModel
import seamonster.kraken.androidep7.ui.signup.SignUpViewModel
import seamonster.kraken.androidep7.ui.tracking.TrackingViewModel
import seamonster.kraken.androidep7.ui.user.UserViewModel

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(EntryViewModel::class)
    fun entryViewModelFactory(factory: EntryViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun loginViewModelFactory(factory: LoginViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    fun signUpViewModelFactory(factory: SignUpViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun mainViewModelFactory(factory: MainViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(MeViewModel::class)
    fun meViewModelFactory(factory: MeViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    fun userViewModelFactory(factory: UserViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel::class)
    fun notificationViewModelFactory(factory: NotificationViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(CheckInViewModel::class)
    fun checkInViewModelFactory(factory: CheckInViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(TrackingViewModel::class)
    fun trackingViewModelFactory(factory: TrackingViewModel.Factory): AssistedViewModelFactory<*, *>
}