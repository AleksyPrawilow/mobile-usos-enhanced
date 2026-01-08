package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.models.Schedule
import com.cdkentertainment.mobilny_usos_enhanced.models.SchedulePageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class HomePageViewModel: ViewModel() {
    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    var todaySchedule: Schedule? by mutableStateOf(null)
    val scheduleModel: SchedulePageModel = SchedulePageModel()

    fun getTimeFromDate(date: String): String {
        val localDateTime = LocalDateTime.parse(date, timeFormatter)
        val localTime: LocalTime = localDateTime.toLocalTime()
        return localTime.toString()
    }

    suspend fun fetchSchedule(): Boolean {
        if (todaySchedule != null) {
            return true
        }
        return withContext(Dispatchers.IO) {
            try {
                todaySchedule = scheduleModel.getSingleDaySchedule(LocalDate.now())
                return@withContext true
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }
    }
}