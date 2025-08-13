package com.cdkentertainment.mobilny_usos_enhanced.models

import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class GradesPageModel {

    suspend fun fetchUserGrades() : Pair<List<Season>, Map<String, CourseUnitIds>>{
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

    suspend fun parseGradesToSeasons(data: Map<String, Map<String, UserGrades>>): Pair<List<Season>, Map<String, CourseUnitIds>> {
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
        }

        val courseUnitIdsString: String = courseUnitIds.joinToString("|")
        val parsedCourseIds: Map<String, CourseUnitIds> = getCourseUnitIdsFromApi(courseUnitIdsString)
        val returnResult = Pair<List<Season>, Map<String, CourseUnitIds>>(resultList, parsedCourseIds)
        return returnResult
    }

    fun parseCourseUnitIds(courseData: Map<String, CourseUnitData?>): Map<String, CourseUnitIds>{
        val resultData: LinkedHashMap<String, CourseUnitIds> = LinkedHashMap<String, CourseUnitIds>()
        for (key in courseData.keys) {
            if(courseData[key] != null) {
                val currentElement: CourseUnitIds = CourseUnitIds(courseData[key]!!.course_name["pl"]!!, courseData[key]!!.classtype_id)
                resultData.put(key, currentElement)
            } //dorobic zwracanie nulla jesli znalazlo
        }
        return resultData
    }

    suspend fun getCourseUnitIdsFromApi(unitString: String): Map<String, CourseUnitIds> {
        return withContext(Dispatchers.IO) {
            val parser: Json = Json{ignoreUnknownKeys = true; encodeDefaults = true}
            val apiResponse: Map<String, String> = OAuthSingleton.get("courses/units?unit_ids=$unitString&fields=course_name|classtype_id")
            if (apiResponse.containsKey("response") && apiResponse["response"] != null) {
                val responseString: String = apiResponse["response"]!!
                return@withContext parseCourseUnitIds(parser.decodeFromString<Map<String, CourseUnitData?>>(responseString))

            } else {
                throw(Exception("API Error"))
            }
        }
    }

    suspend fun fetchClasstypeIds(): Map<String, ClasstypeIdInfo> {
        return withContext(Dispatchers.IO) {
            val parser: Json = Json{ignoreUnknownKeys = true}
            val apiResponse: Map<String, String> = OAuthSingleton.get("courses/classtypes_index")
            if (apiResponse.containsKey("response") && apiResponse["response"] != null) {
                val responseString: String = apiResponse["response"]!!
                return@withContext parser.decodeFromString<Map<String, ClasstypeIdInfo>>(responseString)
            } else {
                throw(Exception("API Error"))
            }
        }
    }
}

@Serializable
data class ClasstypeIdInfo(
    val id: String,
    val name: LangDict
)


@Serializable
data class CourseUnitIds( //it is the final structure for Unit ids and names, Json parser doesn't use it
    val course_name: String,
    val classtype_id: String
)

@Serializable
data class CourseUnitData(
    val course_name: Map<String, String>,
    val classtype_id: String
)

@Serializable
data class Season ( //Final structure for grades per season
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