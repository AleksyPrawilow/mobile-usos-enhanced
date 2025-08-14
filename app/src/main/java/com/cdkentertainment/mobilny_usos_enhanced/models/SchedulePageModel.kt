package com.cdkentertainment.mobilny_usos_enhanced.models

import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class SchedulePageModel {
    private val parser = Json{ignoreUnknownKeys = true}
    private val fields = "start_time|end_time|room_number|course_name|classtype_id|building_name"
    private val baseUrl = "tt/student"

    private fun getDayIndexFromDate(date: LocalDate): Int {
        return date.dayOfWeek.value - 1
    }

    private fun getLocalDateFromString(date: String, pattern: String = "yyyy-MM-dd"): LocalDate {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return LocalDate.parse(date, formatter)
    }

    private fun getFormattedDateString(date: LocalDate, format: String = "yyyy-MM-dd"): String {
        val formatter = DateTimeFormatter.ofPattern(format)
        return date.format(formatter)
    }

    private fun getFirstDayOfWeekDate(fromDate: LocalDate = LocalDate.now()): String {
        val firstDayOfWeek = fromDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return getFormattedDateString(firstDayOfWeek)
    }

    private fun parseScheduleApiResponse(response: String): Schedule {
        val lessons: List<Lesson> = parser.decodeFromString<List<Lesson>>(response)
        val lessonMap = lessons.groupBy { lesson ->
            getDayIndexFromDate(getLocalDateFromString(lesson.start_time.split(" ")[0]))
        }

        val schedule = Schedule(lessonMap)
        return schedule
    }

    public suspend fun getWeekSchedule(date: LocalDate = LocalDate.now()): Schedule {
        return withContext(Dispatchers.IO) {
            val firstDayOFWeekString = getFirstDayOfWeekDate(date)
            val response: Map<String, String> = OAuthSingleton.get("$baseUrl?start=$firstDayOFWeekString&days=7&fields=$fields")

            if (response.containsKey("response") && response["response"] != null) {
                val responseString: String = response["response"]!!
                val parsedSchedule: Schedule = parseScheduleApiResponse(responseString)

                return@withContext parsedSchedule
            } else {
                throw(Exception("API ERROR"))
            }
        }
    }

    public suspend fun getSingleDaySchedule(date: LocalDate = LocalDate.now()): Schedule {
        return withContext(Dispatchers.IO) {
            val dayString: String = getFormattedDateString(date)
            val response: Map<String, String> = OAuthSingleton.get("$baseUrl?start=$dayString&days=1&fields=$fields")

            if (response.containsKey("response") && response["response"] != null) {
                val responseString: String = response["response"]!!
                val parsedSchedule: Schedule = parseScheduleApiResponse(responseString)

                return@withContext parsedSchedule
            } else {
                throw(Exception("API ERROR"))
            }
        }
    }
}

@Serializable
data class Schedule (
    val lessons: Map<Int, List<Lesson>>
)

@Serializable
data class Lesson(
    val start_time: String,
    val end_time: String,
    val room_number: String,
    val course_name: Map<String, String>,
    val classtype_id: String,
    val building_name: Map<String, String>
)