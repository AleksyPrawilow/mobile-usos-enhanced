package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Lesson
import com.cdkentertainment.mobilny_usos_enhanced.models.Schedule
import com.cdkentertainment.mobilny_usos_enhanced.models.SchedulePageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.prettyPrint
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
    val boom = SchedulePageViewModel()
    launch {
        boom.fetchWeekData(LocalDate.of(2025, 5, 13))
        val model = SchedulePageModel()
        println(model.getSingleDaySchedule(LocalDate.of(2025, 5, 13)))
        println(model.getWeekSchedule(LocalDate.of(2025, 5, 13)).prettyPrint())
    }
}

class SchedulePageViewModel: ViewModel() {
    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    var selectedDay: Int by mutableStateOf(0)
        private set
    var selectedWeekOption: Int by mutableStateOf(0)
        private set
    var displayDateSelector: Boolean by mutableStateOf(false)
        private set
    var schedule: Schedule? by mutableStateOf(null)
        private set
    var groupedByHours: Map<Int, List<Lesson>> = mutableStateMapOf<Int, List<Lesson>>()
    private val model: SchedulePageModel = SchedulePageModel()

    fun selectWeekOption(weekOptionIndex: Int) {
        selectedWeekOption = weekOptionIndex
    }

    fun selectDay(dayIndex: Int) {
        selectedDay = dayIndex
    }

    fun setDateSelectorVisibility(visible: Boolean) {
        displayDateSelector = visible
    }

    fun resetSchedule() {
        schedule = null
    }

    fun getTimeFromDate(date: String): String {
        val localDateTime = LocalDateTime.parse(date, timeFormatter)
        val localTime: LocalTime = localDateTime.toLocalTime()
        return localTime.toString()
    }

    fun groupLessonsByHour(lessons: List<Lesson>?) {
        if (lessons == null) {
            groupedByHours = mapOf()
            return
        }
        groupedByHours = lessons.groupBy {
            getTimeFromDate(it.start_time).substring(0, 2).toInt()
        }
    }

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