package seamonster.kraken.androidep7.util

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import seamonster.kraken.androidep7.data.sources.UserPreferences

object ThemeHelper {

    private const val TAG = "ThemeHelper"

    fun changeTheme(context: Context) {
        val mode = getThemeMode(context)
        AppCompatDelegate.setDefaultNightMode(mode)
        if (context is Activity) context.recreate()
    }

    fun getThemeMode(context: Context): Int {
        return UserPreferences(context).theme
    }

    fun setThemeMode(context: Context, theme: Int) {
        UserPreferences(context).theme = theme
        Log.d(TAG, "setThemeMode: ${UserPreferences(context).theme}")
    }
}