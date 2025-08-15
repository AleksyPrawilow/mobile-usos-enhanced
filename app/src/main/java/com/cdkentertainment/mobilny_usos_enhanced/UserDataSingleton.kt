package com.cdkentertainment.mobilny_usos_enhanced

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
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
    private val DARK_THEME = booleanPreferencesKey("DARK_THEME")

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
            settings[DARK_THEME] = currentSettings.darkTheme
        }
    }

    suspend fun readSettings(context: Context) {
        println("Reading user settings...")
        val darkTheme = context.dataStore.data
            .map { prefs -> prefs[DARK_THEME] ?: false }
            .first()
        UISingleton.changeTheme(darkTheme)
        currentSettings = SettingsObject(darkTheme = darkTheme)
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
    var darkTheme: Boolean = false
)