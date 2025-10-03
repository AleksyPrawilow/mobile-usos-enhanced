package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UserDataSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.GradesPageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.Schedule
import com.cdkentertainment.mobilny_usos_enhanced.models.SchedulePageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun main(): Unit = runBlocking {
    OAuthSingleton.setTestAccessToken()
    val boom = HomePageViewModel()
    launch {
        boom.fetchData()
        println(boom.userInfo)
    }
}

class HomePageViewModel: ViewModel() {
    private val gradesPageModel: GradesPageModel = GradesPageModel()
    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    var userInfo: UserInfo? by mutableStateOf(null)
    var todaySchedule: Schedule? by mutableStateOf(null)
    var scheduleFetchSuccess: Boolean by mutableStateOf(false)
    val scheduleModel: SchedulePageModel = SchedulePageModel()

    fun getTimeFromDate(date: String): String {
        val localDateTime = LocalDateTime.parse(date, timeFormatter)
        val localTime: LocalTime = localDateTime.toLocalTime()
        return localTime.toString()
    }

    suspend fun fetchData() {
        withContext(Dispatchers.IO) {
            userInfo = UserDataSingleton.userData
        }
    }

    suspend fun fetchSchedule() {
        scheduleFetchSuccess = true
        if (todaySchedule != null) {
            return
        }
        withContext(Dispatchers.IO) {
            try {
                todaySchedule = scheduleModel.getSingleDaySchedule(LocalDate.now())
            } catch (e: Exception) {
                e.printStackTrace()
                scheduleFetchSuccess = false
            }
        }
    }
}