package com.cdkentertainment.mobilny_usos_enhanced

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.github.scribejava.core.model.OAuth1AccessToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("user_settings")

object UserDataSingleton {
    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token_key")
    private val ACCESS_TOKEN_SECRET = stringPreferencesKey("access_token_secret")
    private val SELECTED_THEME = intPreferencesKey("SELECTED_THEME")

    var currentSettings: SettingsObject = SettingsObject()

    suspend fun saveUserCredentials(context: Context, accessToken: OAuth1AccessToken) {
        context.dataStore.edit { settings ->
            settings[ACCESS_TOKEN_KEY] = accessToken.token
            settings[ACCESS_TOKEN_SECRET] = accessToken.tokenSecret
        }
    }

    suspend fun saveUserSettings(context: Context) {
        println("Saving user settings...")
        context.dataStore.edit { settings ->
            settings[SELECTED_THEME] = currentSettings.selectedTheme
        }
    }

    suspend fun readSettings(context: Context) {
        println("Reading user settings...")
        val selectedTheme = context.dataStore.data
            .map { prefs -> prefs[SELECTED_THEME] ?: 0 }
            .first()
        try {
            val theme: Theme? = UISingleton.themes[UISingleton.themes.keys.elementAt(selectedTheme)]
            if (theme != null) {
                UISingleton.changeTheme(theme)
            }
        } catch (e: Exception) {
            
        }
        currentSettings = SettingsObject(selectedTheme = selectedTheme)
    }

    fun readAccessTokenKey(context: Context): Flow<String> {
        return context.dataStore.data
            .map { prefs -> prefs[ACCESS_TOKEN_KEY] ?: "" }
    }

    fun readAccessTokenSecret(context: Context): Flow<String> {
        return context.dataStore.data
            .map { prefs -> prefs[ACCESS_TOKEN_SECRET] ?: "" }
    }

    suspend fun readAccessToken(context: Context): OAuth1AccessToken? {
        val key: String = readAccessTokenKey(context).first()
        val secret: String = readAccessTokenSecret(context).first()
        if (key != "" && secret != "") {
            return OAuth1AccessToken(key, secret)
        }
        return null
    }
}

data class SettingsObject(
    var selectedTheme: Int = 0
)