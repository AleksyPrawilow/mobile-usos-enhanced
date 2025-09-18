package com.cdkentertainment.mobilny_usos_enhanced.models

import android.content.Context
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AttendancePageModel {
    @Serializable
    private data class AttendanceDates (
        val start_time: String,
        val end_time: String,
    )
    private val parser: Json = Json {ignoreUnknownKeys = true}
    private val attendanceUrl: String = "tt/classgroup_dates2"
    private val fields: String = "start_time|end_time"
    private val timeFormat: String = "yyyy-MM-dd HH:mm:ss"
    private fun parseToLocalDateTime(format: String, date: String): LocalDateTime { //it could be public.
        val formatter = DateTimeFormatter.ofPattern(format)
        return LocalDateTime.parse(date, formatter)
    }
    private fun parseAttendanceDates(responseString: String): List<AttendanceDatesObject> {
        val parsedAttendanceDates: List<AttendanceDates> = parser.decodeFromString(responseString)
        val datesList: MutableList<AttendanceDatesObject> = mutableListOf<AttendanceDatesObject>()
        for(dates in parsedAttendanceDates) {
            val startDate: LocalDateTime = parseToLocalDateTime(timeFormat, dates.start_time)
            val endDate: LocalDateTime = parseToLocalDateTime(timeFormat, dates.end_time)
            datesList.add(AttendanceDatesObject(startDate, endDate))
        }
        return datesList
    }

    public fun filterPastAttendanceDates(dates: List<AttendanceDatesObject>): List<AttendanceDatesObject> {
        val currentDateTime: LocalDateTime = LocalDateTime.now()
        val pastDates = dates.filter {
            it.endDateTime < currentDateTime
        }
        return pastDates
    }

    public suspend fun getGivenSubjectAttendanceDates(courseUnitId: String, groupNumber: String): List<AttendanceDatesObject> {
        return withContext(Dispatchers.IO) {
            val response = OAuthSingleton.get("$attendanceUrl?unit_id=$courseUnitId&group_number=$groupNumber&fields=$fields")
            if (response.containsKey("response") && response["response"] != null) {
                val responseString = response["response"]!!
                val dates: List<AttendanceDatesObject> = parseAttendanceDates(responseString)
                return@withContext dates
            } else {
                throw(Exception("API Error"))
            }
        }
    }
    public suspend fun savePinnedGroups(groups: List<LessonGroup>, fileName: String, context: Context): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val json: String = parser.encodeToString(groups)
                val file = File(context.filesDir, fileName)
                file.writeText(json)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    public suspend fun readPinnedGroups(fileName: String, context: Context): List<LessonGroup> {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(context.filesDir, fileName)
                val json: String = file.readText()
                val groups: List<LessonGroup> = parser.decodeFromString(json)
                return@withContext groups
            } catch (e: Exception) {
                return@withContext emptyList()
            }
        }
    }
}

data class AttendanceDatesObject (
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
)