package seamonster.kraken.androidep7.data.sources.local

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import seamonster.kraken.androidep7.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager(context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val TOKEN_REFRESH = "refresh_token"
    }

    fun saveAuthTokenRefresh(token: String) {
        val editor = prefs.edit()
        editor.putString(TOKEN_REFRESH, token)
        editor.apply()
    }

    fun fetchAuthTokenRefresh(): String? {
        return prefs.getString(TOKEN_REFRESH, null)
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }


}