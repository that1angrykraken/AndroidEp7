package seamonster.kraken.androidep7.data.sources

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenPreferences @Inject constructor(context: Context) {

    companion object {
        const val NAME = "token_preferences"
        const val USER_TOKEN = "user_token"
        const val TOKEN_REFRESH = "refresh_token"
    }

    private var preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    var authTokenRefresh: String?
        get() = preferences.getString(TOKEN_REFRESH, null)
        set(value) = edit(TOKEN_REFRESH, value)

    var authToken: String?
        get() = preferences.getString(USER_TOKEN, null)
        set(value) = edit(USER_TOKEN, value)

    private fun edit(key: String, newValue: String?) {
        preferences.edit().run {
            putString(key, newValue)
            apply()
        }
    }
}