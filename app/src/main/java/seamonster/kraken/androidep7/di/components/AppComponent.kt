package seamonster.kraken.androidep7.di.components

import android.content.Context
import com.airbnb.mvrx.MavericksViewModel
import dagger.BindsInstance
import dagger.Component
import seamonster.kraken.androidep7.di.AssistedViewModelFactory
import seamonster.kraken.androidep7.di.modules.DataModule
import seamonster.kraken.androidep7.di.modules.ViewModelModule
import javax.inject.Singleton

@Component(
    modules = [ViewModelModule::class, DataModule::class]
)
@Singleton
interface AppComponent {

    fun viewModelFactories(): Map<Class<out MavericksViewModel<*>>, AssistedViewModelFactory<*, *>>

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}