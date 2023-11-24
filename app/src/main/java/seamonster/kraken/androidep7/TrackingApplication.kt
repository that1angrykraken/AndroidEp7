package seamonster.kraken.androidep7

import android.app.Application
import androidx.activity.ComponentActivity
import com.airbnb.mvrx.Mavericks
import com.creative.ipfyandroid.Ipfy
import seamonster.kraken.androidep7.di.components.AppComponent
import seamonster.kraken.androidep7.di.components.DaggerAppComponent

class TrackingApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Mavericks.initialize(this)
        Ipfy.init(this)
        appComponent = DaggerAppComponent.factory().create(this)

    }
}

fun ComponentActivity.appComponent(): AppComponent {
    return (application as TrackingApplication).appComponent
}