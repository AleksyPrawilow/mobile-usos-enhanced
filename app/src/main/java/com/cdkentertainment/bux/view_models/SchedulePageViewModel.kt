package com.cdkentertainment.bux.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.bux.models.Lesson
import com.cdkentertainment.bux.models.Schedule
import com.cdkentertainment.bux.models.SchedulePageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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
    var loading: Boolean by mutableStateOf(false)
    var error: Boolean by mutableStateOf(false)
    var loaded: Boolean by mutableStateOf(false)
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

    suspend fun fetchWeekData(date: LocalDate) {
        withContext(Dispatchers.IO) {
            if (schedule != null) {
                return@withContext
            }
            loading = true
            error = false
            loaded = false
            try {
                schedule = model.getWeekSchedule(date)
                loaded = true
                loading = false
                error = false
            } catch (e: Exception) {
                e.printStackTrace()
                loading = false
                loaded = false
                error = true
            }
        }
    }
}