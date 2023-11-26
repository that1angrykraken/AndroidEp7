package seamonster.kraken.androidep7

import android.app.Application
import androidx.activity.ComponentActivity
import com.airbnb.mvrx.Mavericks
import com.creative.ipfyandroid.Ipfy
import seamonster.kraken.androidep7.di.components.AppComponent
import seamonster.kraken.androidep7.di.components.DaggerAppComponent
import seamonster.kraken.androidep7.util.ThemeHelper

class TrackingApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Mavericks.initialize(this)
        appComponent = DaggerAppComponent.factory().create(this)
        Ipfy.init(this)
        ThemeHelper.changeTheme(this)
    }
}

fun ComponentActivity.appComponent(): AppComponent {
    return (application as TrackingApplication).appComponent
}