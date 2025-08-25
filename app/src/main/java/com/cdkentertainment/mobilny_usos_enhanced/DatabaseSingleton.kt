package com.cdkentertainment.mobilny_usos_enhanced

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object DatabaseSingleton {
    val client = createSupabaseClient(
        supabaseUrl = "https://shpubzahtgrktqihdhnh.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNocHViemFodGdya3RxaWhkaG5oIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTYxMzY1NDAsImV4cCI6MjA3MTcxMjU0MH0.HXh7Hlc6ja77IFhPvJQ60ALF7ijWdvcKiMa0INEzpWA"
    ) {
        install(Auth)
        install(Postgrest)
    }
}