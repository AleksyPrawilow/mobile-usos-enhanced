package com.cdkentertainment.mobilny_usos_enhanced.models

import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class SchedulePageModel {
    private val parser = Json { ignoreUnknownKeys = true }
    private val requestDayUrl = "Schedule/Day"
    private val requestWeekUrl = "Schedule/Week"
    private fun getFormattedDateString(date: LocalDate, format: String = "yyyy-MM-dd"): String {
        val formatter = DateTimeFormatter.ofPattern(format)
        return date.format(formatter)
    }
    private fun getDayOfWeekDate(
        fromDate: LocalDate = LocalDate.now(),
        first: Boolean = true
    ): String {
        val dayOfWeek =
            if (first) fromDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)) else fromDate.with(
                TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)
            )
        return getFormattedDateString(dayOfWeek)
    }
    public suspend fun getWeekSchedule(date: LocalDate = LocalDate.now()): Schedule {
        return withContext(Dispatchers.IO) {
            val firstDayOFWeekString = getDayOfWeekDate(date, true)
            val response: BackendDataSender.BackendResponse = BackendDataSender.get("$requestWeekUrl?date=$firstDayOFWeekString")
            if (response.statusCode == 200) {
                val responseString: String = response.body
                val parsedSchedule: Schedule = parser.decodeFromString<Schedule>(responseString)
                return@withContext parsedSchedule
            } else {
                throw (Exception("API ERROR"))
            }
        }
    }
    public suspend fun getSingleDaySchedule(date: LocalDate = LocalDate.now()): Schedule {
        return withContext(Dispatchers.IO) {
            val dayString: String = getFormattedDateString(date)
            val response: BackendDataSender.BackendResponse = BackendDataSender.get("$requestDayUrl?date=$dayString")
            if (response.statusCode == 200) {
                val responseString: String = response.body
                val parsedSchedule: Schedule = parser.decodeFromString<Schedule>(responseString)
                return@withContext parsedSchedule
            } else {
                throw(Exception("API ERROR"))
            }
        }
    }
}
@Serializable
data class Schedule (
    var lessons: Map<Int, List<Lesson>>,
    var startDay: String? = null,
    var endDay: String? = null,
)
@Serializable
data class Lesson(
    val start_time: String,
    val end_time: String,
    var room_number: String,
    val course_name: SharedDataClasses.LangDict,
    val classtype_id: String,
    val building_name: SharedDataClasses.LangDict,
    val lecturer_ids: List<Int>
)