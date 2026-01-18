package com.cdkentertainment.muniversity

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cdkentertainment.muniversity.models.BackendDataSender
import com.cdkentertainment.muniversity.models.SharedDataClasses
import com.cdkentertainment.muniversity.models.UserInfo
import com.cdkentertainment.muniversity.usos_installations.Universities
import com.github.scribejava.core.model.OAuth1AccessToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("user_settings")

object UserDataSingleton {
    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token_key")
    private val ACCESS_TOKEN_SECRET = stringPreferencesKey("access_token_secret")
    private val SELECTED_UNIVERSITY = intPreferencesKey("SELECTED_UNIVERSITY")
    private val SELECTED_THEME = intPreferencesKey("SELECTED_THEME")

    var currentSettings: SettingsObject = SettingsObject()
    var selectedUniversity: Int by mutableIntStateOf(0)
    var userData: UserInfo? by mutableStateOf(null)
    var userFaculties: MutableMap<String, SharedDataClasses.LangDict> = mutableStateMapOf()

    suspend fun saveUserCredentials(context: Context, accessToken: OAuth1AccessToken) {
        context.dataStore.edit { settings ->
            settings[ACCESS_TOKEN_KEY] = accessToken.token
            settings[ACCESS_TOKEN_SECRET] = accessToken.tokenSecret
            settings[SELECTED_UNIVERSITY] = selectedUniversity
        }
    }

    suspend fun deleteUserCredentials(context: Context) {
        BackendDataSender.oAuth1AccessToken = null
        context.dataStore.edit { settings ->
            settings[ACCESS_TOKEN_KEY] = ""
            settings[ACCESS_TOKEN_SECRET] = ""
            settings[SELECTED_UNIVERSITY] = Universities.NOT_CHOSEN.id
        }
    }

    suspend fun saveUserSettings(context: Context) {
        context.dataStore.edit { settings ->
            settings[SELECTED_THEME] = currentSettings.selectedTheme
        }
    }

    suspend fun readSettings(context: Context) {
        val selectedTheme = context.dataStore.data
            .map { prefs -> prefs[SELECTED_THEME] ?: 0 }
            .first()
        try {
            val theme: Theme? = UISingleton.themes[UISingleton.themes.keys.elementAt(selectedTheme)]
            if (theme != null) {
                UISingleton.changeTheme(theme)
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

    fun readSelectedUniversity(context: Context): Flow<Int> {
        return context.dataStore.data
            .map { prefs -> prefs[SELECTED_UNIVERSITY] ?: 0 }
    }

    suspend fun readAccessToken(context: Context): OAuth1AccessToken? {
        val key: String = readAccessTokenKey(context).first()
        val secret: String = readAccessTokenSecret(context).first()
        selectedUniversity = readSelectedUniversity(context).first()
        if (key != "" && secret != "") {
            return OAuth1AccessToken(key, secret)
        }
        return null
    }

    fun getUserFaculties(userData: UserInfo): MutableMap<String, SharedDataClasses.LangDict> {
        val faculties: MutableMap<String, SharedDataClasses.LangDict> = mutableMapOf()
        for (programme in userData.student_programmes) {
            val programmeFaculties = programme.programme.all_faculties
            for (faculty in programmeFaculties) {
                if (faculty.id.toInt() == 0) {
                    continue
                }
                faculties[faculty.id] = faculty.name
            }
        }
        return faculties
    }
}

data class SettingsObject(
    var selectedTheme: Int = 0
)