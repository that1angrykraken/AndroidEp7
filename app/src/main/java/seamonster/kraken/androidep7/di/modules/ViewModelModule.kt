package seamonster.kraken.androidep7.di.modules

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.ViewModelKey
import seamonster.kraken.androidep7.ui.auth.AuthViewModel
import seamonster.kraken.androidep7.ui.entry.EntryViewModel
import seamonster.kraken.androidep7.ui.auth.LoginViewModel

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun loginViewModelFactory(factory: LoginViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(EntryViewModel::class)
    fun entryViewModelFactory(factory: EntryViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    fun authViewModelFactory(factory: AuthViewModel.Factory): AssistedViewModelFactory<*, *>
}