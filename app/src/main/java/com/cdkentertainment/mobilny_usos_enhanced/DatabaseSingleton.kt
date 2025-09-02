package com.cdkentertainment.mobilny_usos_enhanced

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.cdkentertainment.mobilny_usos_enhanced.models.BasicUserData
import com.cdkentertainment.mobilny_usos_enhanced.usos_installations.Universities
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object DatabaseSingleton {
    val client = createSupabaseClient(
        supabaseUrl = "https://shpubzahtgrktqihdhnh.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNocHViemFodGdya3RxaWhkaG5oIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTYxMzY1NDAsImV4cCI6MjA3MTcxMjU0MH0.HXh7Hlc6ja77IFhPvJQ60ALF7ijWdvcKiMa0INEzpWA"
    ) {
        install(Auth) {
            autoSaveToStorage = true
            alwaysAutoRefresh = true
        }
        install(Postgrest)
    }

    var selectedUniversity: Universities by mutableStateOf(Universities.UAM)
    var userInformation: BasicUserData? by mutableStateOf(null)

    suspend fun updateUserSession(userId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val previousSessionString: String = client.postgrest.rpc("check_user_session", UserSessionData(userId, 1)).data
                val parser: Json = Json { ignoreUnknownKeys = true }
                var previousSession: List<UserSessionFullData> = listOf()
                try {
                    previousSession = parser.decodeFromString<List<UserSessionFullData>>(previousSessionString)
                } catch (e: Exception) {
                    previousSession = listOf()
                    e.printStackTrace()
                }
                if (previousSession.count() != 0) {
                    val currentUser: String? = client.auth.currentUserOrNull()?.id
                    if (previousSession[0].user_session == currentUser) {
                        println("no update")
                        return@withContext true
                    }
                }
                val userSessionData = UserSessionData(userId, selectedUniversity.ordinal)
                val res = client.postgrest.rpc("upsert_user_session_to_current", userSessionData)
                return@withContext true
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }
    }
}

@Serializable
private data class UserSessionData(
    val p_user_id: String,
    val p_university_id: Int
)

@Serializable
private data class UserSessionFullData(
    val user_id: String,
    val user_session: String?,
    val university_id: Int
)