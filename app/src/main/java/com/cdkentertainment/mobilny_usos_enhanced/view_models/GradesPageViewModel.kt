package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.CourseUnitIds
import com.cdkentertainment.mobilny_usos_enhanced.models.GradesPageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.Season
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main(): Unit = runBlocking {
    OAuthSingleton.setTestAccessToken()
    val gradesPageViewModel: GradesPageViewModel = GradesPageViewModel()
    launch {
        gradesPageViewModel.fetchUserGrades()
    }
}

class GradesPageViewModel: ViewModel() {

    var userGrades: List<Season>? by mutableStateOf(null)
    var userSubjects: Map<String, CourseUnitIds>? by mutableStateOf(null)
    var classtypeIdInfo: Map<String, SharedDataClasses.IdAndName>? by mutableStateOf(null)
    val gradesPageModel: GradesPageModel = GradesPageModel()

    suspend fun fetchUserGrades() {
        withContext(Dispatchers.IO) {
            if (classtypeIdInfo == null) {
                try {
                    classtypeIdInfo = gradesPageModel.fetchClasstypeIds()
                } catch (e: Exception) {
                    println(e)
                }
            }
            try {
                val data:Pair<List<Season>, Map<String, CourseUnitIds>>? = gradesPageModel.fetchUserGrades()
                userGrades = data!!.first
                userSubjects = data.second
            } catch (e: Exception) {
                println(e)
                //return@withContext //ogarnij coś tutaj @aleksy
            }
        }
    }
}