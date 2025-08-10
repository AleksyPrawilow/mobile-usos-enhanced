package com.cdkentertainment.mobilny_usos_enhanced.models

import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class GradesPageModel {

    suspend fun fetchUserGrades() : List<Season> ? {
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
            }

            val response: Map<String, String> = OAuthSingleton.get("grades/terms2?term_ids=$termIds") //terms_id has to be dinamicaly, now it is temporary\

            if (response.containsKey("response") && response["response"] != null) {
                val responseString: String = response["response"]!!
                return@withContext parseGradesToSeasons(parser.decodeFromString<Map<String, Map<String, UserGrades>>>(responseString))
            } else {
                return@withContext null
            }
        }
    }

    fun parseGradesToSeasons(data: Map<String, Map<String, UserGrades>>): List<Season> {
        val resultList: ArrayList<Season> = ArrayList()
        for (key in data.keys) {
            val courseList: ArrayList<Course> = ArrayList()
            for (subjectName in data[key]!!.keys) {
                if (data[key]!![subjectName] != null) {
                    val course = Course(subjectName, data[key]!![subjectName]!!)
                    courseList.add(element = course)
                }
            }

            val season = Season(seasonId = key, courseList = courseList)
            resultList.add(season)
        }
        return resultList
    }
}

@Serializable
data class Season (
    val seasonId: String,
    val courseList: List<Course>
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
    val exam_session_number: Int
)

@Serializable
data class LangDict(
    val pl: String,
    val en: String
)