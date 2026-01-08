package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cdkentertainment.mobilny_usos_enhanced.models.GradesDistribution
import com.cdkentertainment.mobilny_usos_enhanced.models.GradesPageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.Season
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GradesPageViewModel: ViewModel() {
    var gradesDistribution: MutableMap<Int, Map<String, Int>> = mutableStateMapOf()
    var userSubjects: MutableMap<String, Season> = mutableStateMapOf<String, Season>()
    var loadingMap: MutableMap<String, Boolean> = mutableStateMapOf<String, Boolean>()
    var errorMap: MutableMap<String, Boolean> = mutableStateMapOf<String, Boolean>()
    var loadedMap: MutableMap<String, Boolean> = mutableStateMapOf<String, Boolean>()
    var classtypeIdInfo: Map<String, SharedDataClasses.IdAndName>? by mutableStateOf(null)
    val gradesPageModel: GradesPageModel = GradesPageModel()

    suspend fun suspendFetchSemesterGrades(semester: String) {
        if (userSubjects.containsKey(semester) || loadingMap[semester] == true) return
        loadingMap[semester] = true
        errorMap[semester] = false
        loadedMap[semester] = false
        withContext(Dispatchers.IO) {
            try {
                val semesterCourses: Season = gradesPageModel.fetchUserGrades(semester)
                userSubjects[semester] = semesterCourses
                errorMap[semester] = false
                loadedMap[semester] = true
            } catch (e: Exception) {
                errorMap[semester] = true
                loadedMap[semester] = false
            }
            loadingMap[semester] = false
        }
    }
    fun fetchSemesterGrades(semester: String) {
        if (userSubjects.containsKey(semester) || loadingMap[semester] == true) return
        loadingMap[semester] = true
        errorMap[semester] = false
        loadedMap[semester] = false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val semesterCourses: Season = gradesPageModel.fetchUserGrades(semester)
                    userSubjects[semester] = semesterCourses
                    errorMap[semester] = false
                    loadedMap[semester] = true
                } catch (e: Exception) {
                    errorMap[semester] = true
                    loadedMap[semester] = false
                }
                loadingMap[semester] = false
            }
        }
    }

    suspend fun fetchGradesDistribution(examId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val distribution: GradesDistribution = gradesPageModel.getGivenExamGradesDistribution(examId)
                val map: Map<String, Int> = distribution.grades_distribution.associate { it.grade_symbol to it.percentage }
                gradesDistribution[examId] = map
                return@withContext true
            } catch (e: Exception) {
                println(e)
                return@withContext false
            }
        }
    }
}