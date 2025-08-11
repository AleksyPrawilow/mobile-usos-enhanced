package com.cdkentertainment.mobilny_usos_enhanced.models

import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class GradesPageModel {

    suspend fun fetchUserGrades() : List<Season> {
        return withContext(Dispatchers.IO) {
            val parser: Json = Json { ignoreUnknownKeys = true }
            val termIdsResponse: Map<String, String> = OAuthSingleton.get("courses/user?fields=terms[id]")
            var termIds: String = ""

            if (termIdsResponse.containsKey("response") && termIdsResponse["response"] != null) {
                val termIdsResponseString: String = termIdsResponse["response"]!!
                var termIdsObject: Map<String, List<Map<String, String>>> = parser.decodeFromString<Map<String, List<Map<String, String>>>>(termIdsResponseString)

                val termListSize: Int = termIdsObject["terms"]!!.size
                for (i in 0 until termListSize) {
                    termIds += termIdsObject["terms"]!![i]["id"]
                    if (i + 1 != termListSize) {
                       termIds += "|"
                    }
                }
            } else {
                throw(Exception("API Error"))
            }

            val response: Map<String, String> = OAuthSingleton.get("grades/terms2?term_ids=$termIds")

            if (response.containsKey("response") && response["response"] != null) {
                val responseString: String = response["response"]!!
                return@withContext parseGradesToSeasons(parser.decodeFromString<Map<String, Map<String, UserGrades>>>(responseString))
            } else {
                throw(Exception("API Error"))
            }
        }
    }

    fun parseGradesToSeasons(data: Map<String, Map<String, UserGrades>>): List<Season> {
        val resultList: ArrayList<Season> = ArrayList()
        val courseUnitIds: ArrayList<String> = ArrayList<String>()
        for (key in data.keys) {
            var seasonGradesSum: Float = 0f
            var seasonGradesCount: Int = 0
            val courseList: ArrayList<Course> = ArrayList()

            for (subjectName in data[key]!!.keys) {
                val subject: UserGrades ? = data[key]!![subjectName]
                if (subject != null) {

                    val course = Course(subjectName, subject)
                    courseList.add(element = course)
                    for (courseUnit in subject.course_units_grades.keys) {

                        courseUnitIds.add(courseUnit)
                        val term = subject.course_units_grades[courseUnit]!![0]

                        for (i in 1..2) {
                            if (term["$i"] != null) {
                                val valueGrade: Float? =
                                    term["$i"]!!.value_symbol.replace(",", ".").toFloatOrNull()
                                if (valueGrade != null) {
                                    seasonGradesSum += valueGrade
                                    seasonGradesCount++
                                }
                            }
                        }
                    }
                }
            }
            val avgGrade: Float = seasonGradesSum / seasonGradesCount.toFloat()
            val season = Season(seasonId = key, courseList = courseList, avgGrade = avgGrade)
            resultList.add(season)
            println(avgGrade)
        }

        return resultList
    }
}

@Serializable
data class Season (
    val seasonId: String,
    val courseList: List<Course>,
    val avgGrade: Float ? = null
)

@Serializable
data class Course (
    val courseId: String,
    val userGrades: UserGrades
)

@Serializable
data class UserGrades (
    val course_units_grades : Map<String, List<Map<String, TermGrade ?>> ?>,
    val course_grades: List<Map<String, String> ?>?
)

@Serializable
data class TermGrade (
    val value_symbol: String = "N/A",
    val value_description: LangDict,
    val exam_id: Int,
    val exam_session_number: Int,
    val grade_value: Float ? = null
)

@Serializable
data class LangDict(
    val pl: String,
    val en: String
)