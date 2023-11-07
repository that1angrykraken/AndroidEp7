package seamonster.kraken.androidep7.data.sources.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(UserPreferences.NAME)

@Singleton
class UserPreferences @Inject constructor(private val context: Context) {
    companion object {
        const val NAME = "user_preferences"

        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val USER_ID = longPreferencesKey("user_id")
        private val USER_ROLE= stringPreferencesKey("user_role")
        private val USERNAME = stringPreferencesKey("user_name")
        private val USER_FULL_NAME = stringPreferencesKey("user_full_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val LANGUAGE = stringPreferencesKey("language")
        private val DARK_THEME = booleanPreferencesKey("darkTheme")
    }

    val languageFlow: Flow<String> = context.dataStore.data.map { it[LANGUAGE] ?: "en" }

    suspend fun changeLanguage(selectedLanguage: String) {
        context.dataStore.edit { it[LANGUAGE] = selectedLanguage }
    }

    val darkThemeFlow: Flow<Boolean> = context.dataStore.data.map { it[DARK_THEME] ?: false }

    suspend fun useDarkTheme(useDarkTheme: Boolean) {
        context.dataStore.edit { it[DARK_THEME] = useDarkTheme }
    }

    suspend fun saveAccessTokens(accessToken: String, refreshToken: String) {
        context.dataStore.edit {
            it[ACCESS_TOKEN] = accessToken
            it[REFRESH_TOKEN] = refreshToken
        }
    }
}