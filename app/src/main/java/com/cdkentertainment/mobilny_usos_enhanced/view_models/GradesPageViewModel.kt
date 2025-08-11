package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.GradesPageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.Season
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main(): Unit = runBlocking {
    OAuthSingleton.setTestAccessToken()
    val gradesPageViewModel: GradesPageViewModel = GradesPageViewModel()
    launch {
        gradesPageViewModel.fetchUserGrades()
        println(gradesPageViewModel.userGrades)
    }
}

class GradesPageViewModel: ViewModel() {

    var userGrades: List<Season>? by mutableStateOf(null)
    val gradesPageModel: GradesPageModel = GradesPageModel()

    suspend fun fetchUserGrades() {
        withContext(Dispatchers.IO) {
            try {
                userGrades = gradesPageModel.fetchUserGrades()
            } catch (e: Exception) {
                return@withContext //ogarnij coś tutaj @aleksy
            }
        }
    }
}