package com.cdkentertainment.mobilny_usos_enhanced

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

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

    suspend fun signInUser() {
        withContext(Dispatchers.IO) {
            try {
                client.auth.signInWith(Email) {
                    email = "example@gmail.com"
                    password = "examplepassword"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun updateUserSession(userId: String, universityId: Int) {
        withContext(Dispatchers.IO) {
            try {
                val userSessionData = UserSessionData(userId, universityId)
                val res = client.postgrest.rpc("upsert_user_session_to_current", userSessionData)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

@Serializable
private data class UserSessionData(
    val p_user_id: String,
    val p_university_id: Int
)