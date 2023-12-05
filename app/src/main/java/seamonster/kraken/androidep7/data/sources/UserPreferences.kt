package seamonster.kraken.androidep7.data.sources

import android.content.Context

class UserPreferences(context: Context) {

    companion object {
        private const val NAME = "user_preferences"
        private const val ROLE_NAME = "role_name"
        private const val THEME_MODE = "theme"
        private const val SHOULD_REQUEST_PERMISSION = "should_req_permission"
        private const val TOKEN_DEVICE = "token_device"
    }

    private val preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    var userRoles: String?
        get() = preferences.getString(ROLE_NAME, null)
        set(value) = preferences.edit().run {
            putString(ROLE_NAME, value)
            apply()
        }

    var shouldRequestPermission: Boolean
        get() = preferences.getBoolean(SHOULD_REQUEST_PERMISSION, true)
        set(value) = preferences.edit().run {
            putBoolean(SHOULD_REQUEST_PERMISSION, value)
            apply()
        }

    var theme: Int
        get() = preferences.getInt(THEME_MODE, -1)
        set(value) = preferences.edit().run {
            putInt(THEME_MODE, value)
            apply()
        }

    var tokenDevice: String?
        get() = preferences.getString(TOKEN_DEVICE, null)
        set(value) = preferences.edit().run {
            putString(TOKEN_DEVICE, value)
            apply()
        }
}