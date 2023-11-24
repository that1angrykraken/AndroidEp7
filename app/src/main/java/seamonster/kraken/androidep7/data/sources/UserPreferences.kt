package seamonster.kraken.androidep7.data.sources

import android.content.Context

class UserPreferences (context: Context){

    companion object {
        private const val NAME = "user_preferences"
        private const val ROLE_NAME = "role_name"
        private const val THEME = "theme"
        private const val LANGUAGE = "language"
    }

    private val preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    var userRoles: String?
        get() = preferences.getString(ROLE_NAME, null)
        set(value) = edit(ROLE_NAME, value)

    var theme: String?
        get() = preferences.getString(THEME, null)
        set(value) = edit(THEME, value)

    var language: String?
        get() = preferences.getString(LANGUAGE, null)
        set(value) = edit(LANGUAGE, value)

    private fun edit(key: String, newValue: String?) {
        preferences.edit().run {
            putString(key, newValue)
            apply()
        }
    }
}