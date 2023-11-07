package seamonster.kraken.androidep7

import android.app.Application
import androidx.activity.ComponentActivity
import com.airbnb.mvrx.Mavericks
import seamonster.kraken.androidep7.di.components.AppComponent
import seamonster.kraken.androidep7.di.components.DaggerAppComponent

class TrackingApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        Mavericks.initialize(this)
    }
}

fun ComponentActivity.appComponent(): AppComponent {
    return (application as TrackingApplication).appComponent
}