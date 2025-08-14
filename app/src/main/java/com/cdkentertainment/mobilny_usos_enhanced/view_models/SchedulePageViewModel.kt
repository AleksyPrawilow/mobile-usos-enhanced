package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Schedule
import com.cdkentertainment.mobilny_usos_enhanced.models.SchedulePageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.LocalDate

fun main(): Unit = runBlocking {
    OAuthSingleton.setTestAccessToken()
    val boom = SchedulePageViewModel()
    launch {
        boom.fetchWeekData(LocalDate.of(2025, 5, 13))
    }
}

class SchedulePageViewModel: ViewModel() {
    var schedule: Schedule? by mutableStateOf(null)
    val model: SchedulePageModel = SchedulePageModel()

    suspend fun fetchTodaysActivities() {
        withContext(Dispatchers.IO) {
            if (schedule != null) {
                return@withContext
            }
            schedule = model.getSingleDaySchedule(LocalDate.now())
        }
    }

    suspend fun fetchWeekData(date: LocalDate) {
        withContext(Dispatchers.IO) {
            if (schedule != null) {
                return@withContext
            }
            schedule = model.getWeekSchedule(date)
        }
    }
}