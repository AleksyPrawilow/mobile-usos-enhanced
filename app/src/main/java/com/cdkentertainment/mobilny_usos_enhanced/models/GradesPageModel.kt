package com.cdkentertainment.mobilny_usos_enhanced.models

import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class GradesPageModel {
    private val parser = Json{ignoreUnknownKeys = true}
    private val userTermsUrl: String = "courses/user"
    private val userTermsFields: String = "terms[id]"
    private val gradesUrl: String = "grades/terms2"
    private val gradesFields: String =
        "date_modified|modification_author|value_symbol|passes|value_description|exam_id|exam_session_number"
    @Serializable
    private data class TermIdObject (
        val terms: List<TermId>
    )
    @Serializable
    private data class TermId (
        val id: String
    )
    private suspend fun getTermsIdsString(): String {
        return withContext(Dispatchers.IO) {
            val termIdsResponse: Map<String, String> = OAuthSingleton.get("$userTermsUrl?fields=$userTermsFields")
            var termIds: String = ""
            if (termIdsResponse.containsKey("response") && termIdsResponse["response"] != null) {
                val termIdsResponseString: String = termIdsResponse["response"]!!
                val termIdsObject: TermIdObject = parser.decodeFromString<TermIdObject>(termIdsResponseString)
                val termList: MutableList<String> = mutableListOf<String>()
                for (term in termIdsObject.terms) {
                    termList.add(term.id)
                }
                termIds = termList.joinToString(separator = "|")
                return@withContext termIds
            } else {
                throw (Exception("API Error"))
            }
        }
    }

    private fun parseGradesToSeasonsAndCalculateAvgGrade(responseString: String): Map<String, Map<String, CourseGrades>> {
        val parsedGrades: Map<String, Map<String, CourseGrades>> = parser.decodeFromString<Map<String, Map<String, CourseGrades>>>(responseString) //niestety ewidentnie ktoś kto pisał to api chciał nam to utrudnić
        val finalSeasonObject: MutableList<Season> = mutableListOf<Season>()
        for (seasonId in parsedGrades.keys) {
            val currentCourse = parsedGrades[seasonId]
            if (currentCourse != null) {
                for (unit in currentCourse.keys) {

                }
            }
            val currentSeasonObject: Season = Season(seasonId, )
        }
        return parsedGrades
    }

    public suspend fun fetchUserGrades() : Pair<List<Season>, Map<String, CourseUnitIds>> ?{
        return withContext(Dispatchers.IO) {
            val termIds: String = getTermsIdsString()
            val response: Map< String, String> = OAuthSingleton.get("$gradesUrl?term_ids=$termIds&fields=$gradesFields")

            if (response.containsKey("response") && response["response"] != null) {
                val responseString: String = response["response"]!!
                val parsedGrades: Map<String, Map<String, CourseGrades>> = parseGradesToSeasonsAndCalculateAvgGrade(responseString)
                println(parsedGrades)
                return@withContext null
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

    suspend fun fetchClasstypeIds(): Map<String, SharedDataClasses.IdAndName> {
        return withContext(Dispatchers.IO) {
            val apiResponse: Map<String, String> = OAuthSingleton.get("courses/classtypes_index")
            if (apiResponse.containsKey("response") && apiResponse["response"] != null) {
                val responseString: String = apiResponse["response"]!!
                return@withContext parser.decodeFromString<Map<String, SharedDataClasses.IdAndName>>(responseString)
            } else {
                throw(Exception("API Error"))
            }
        }
    }
}

@Serializable
data class CourseGrades (
    val course_units_grades: Map<String, List<Map<String, SingleTermGradeObject?>>>,
    val course_grades: List<Map<String, String> ?>?
)
@Serializable
data class SingleTermGradeObject (
    val value_symbol: String = "N/A",
    val value_description: SharedDataClasses.LangDict,
    val exam_id: Int,
    val exam_session_number: Int,
    val grade_value: Float ? = null,
    val date_modified: String? = null,
    val modification_author: SharedDataClasses.Human? = null
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
    val courseGrades: CourseGrades
)
//@Serializable
//data class UserGrades (
//    val course_units_grades : Map<String, List<Map<String, TermGrade ?>> ?>,
//    val course_grades: List<Map<String, String> ?>?
//)









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
data class TermGrade (
    val value_symbol: String = "N/A",
    val value_description: SharedDataClasses.LangDict,
    val exam_id: Int,
    val exam_session_number: Int,
    val grade_value: Float ? = null,
    val date_modified: String? = null,
    val modification_author: SharedDataClasses.Human? = null
)