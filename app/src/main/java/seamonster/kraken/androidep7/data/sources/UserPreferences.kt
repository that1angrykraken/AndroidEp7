package seamonster.kraken.androidep7.data.sources

import android.content.Context

class UserPreferences(context: Context) {

    companion object {
        private const val NAME = "user_preferences"
        private const val ROLE_NAME = "role_name"
        private const val THEME_MODE = "theme"
        private const val APP_LANGUAGE = "language"
    }

    private val preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    var userRoles: String?
        get() = preferences.getString(ROLE_NAME, null)
        set(value) = preferences.edit().run {
            putString(ROLE_NAME, value)
            apply()
        }

    var theme: Int
        get() = preferences.getInt(THEME_MODE, -1)
        set(value) = preferences.edit().run {
            putInt(THEME_MODE, value)
            apply()
        }
}