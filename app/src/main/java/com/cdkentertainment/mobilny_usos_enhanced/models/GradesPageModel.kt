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
    private val courseIdsString: String = "courses/units"
    private val courseIdsFields: String = "course_name|classtype_id"
    private val classtypeUrl: String = "courses/classtypes_index"
    @Serializable
    private data class TermIdObject (
        val terms: List<TermId>
    )
    @Serializable
    private data class TermId (
        val id: String
    )
    @Serializable
    private data class CourseUnitData(
        val course_name: SharedDataClasses.LangDict,
        val classtype_id: String
    )
    private suspend fun getTermsIdsString(): String {
        return withContext(Dispatchers.IO) {
            val termIdsResponse: Map<String, String> = OAuthSingleton.get("$userTermsUrl?fields=$userTermsFields")
            var termIds: String
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
    private fun getCOurseUnitIdsString(grades: List<Season>): String {
        val courseUnitIds: MutableList<String> = mutableListOf<String>()
        for (season in grades) {
            for (course in season.courseList) {
                for(singleId in course.courseGrades.course_units_grades.keys) {
                    courseUnitIds.add(singleId)
                }
            }
        }
        val courseUnitIdsString: String = courseUnitIds.joinToString("|")
        return courseUnitIdsString
    }
    private fun parseGradesToSeasonsAndCalculateAvgGrade(responseString: String): List<Season> {
        val parsedGrades: Map<String, Map<String, CourseGrades>> = parser.decodeFromString<Map<String, Map<String, CourseGrades>>>(responseString) //niestety ewidentnie ktoś kto pisał to api chciał nam to utrudnić
        val finalSeasonObject: MutableList<Season> = mutableListOf<Season>()
        for (seasonId in parsedGrades.keys) {
            var seasonAvgGrade: Float = 0f
            var seasonGeadesCount: Int = 0
            val seasonCourseList: MutableList<Course> = mutableListOf<Course>()
            val currentSeason = parsedGrades[seasonId]
            if (currentSeason != null) {
                for (course in currentSeason.keys) {
                    val currentSingleCourse = currentSeason[course]
                    if (currentSingleCourse != null) {
                        val currentCourseObject: Course = Course(course, CourseGrades(currentSingleCourse.course_units_grades, currentSingleCourse.course_grades))
                        for (courseId in currentSingleCourse.course_units_grades.keys) {
                            val currentCourseUnitObject = currentSingleCourse.course_units_grades[courseId]!![0]
                            for (i in 1..2) {
                                if (currentCourseUnitObject["$i"] != null) {
                                    val valueGrade: Float? = currentCourseUnitObject["$i"]!!.value_symbol.replace(",", ".").toFloatOrNull()
                                    if (valueGrade != null) {
                                        seasonAvgGrade += valueGrade
                                        seasonGeadesCount ++
                                    }
                                }
                            }
                        }
                        seasonCourseList.add(currentCourseObject)
                    }
                }
            }
            seasonAvgGrade /= seasonGeadesCount.toFloat()
            val currentSeasonObject: Season = Season(seasonId, seasonCourseList, seasonAvgGrade)
            finalSeasonObject.add(currentSeasonObject)
        }
        return finalSeasonObject
    }

    private suspend fun getGradesObject(termIds: String): List<Season> {
        return withContext(Dispatchers.IO) {
            val response: Map< String, String> = OAuthSingleton.get("$gradesUrl?term_ids=$termIds&fields=$gradesFields")
            if (response.containsKey("response") && response["response"] != null) {
                val responseString: String = response["response"]!!
                val parsedGrades: List<Season> = parseGradesToSeasonsAndCalculateAvgGrade(responseString)
                return@withContext parsedGrades
            } else {
                throw(Exception("API Error"))
            }
        }
    }
    private fun parseCourseUnitIds(courseData: Map<String, CourseUnitData?>): Map<String, CourseUnitIds>{
        val resultData: MutableMap<String, CourseUnitIds> = mutableMapOf<String, CourseUnitIds>()
        for (key in courseData.keys) {
            if(courseData[key] != null) {
                val currentElement: CourseUnitIds = CourseUnitIds(courseData[key]!!.course_name.pl, courseData[key]!!.classtype_id)
                resultData.put(key, currentElement)
            } //dorobic zwracanie nulla jesli znalazlo
        }
        return resultData
    }

    private suspend fun getCourseUnitIds(unitString: String): Map<String, CourseUnitIds> {
        return withContext(Dispatchers.IO) {
            val parser: Json = Json{ignoreUnknownKeys = true; encodeDefaults = true}
            val apiResponse: Map<String, String> = OAuthSingleton.get("$courseIdsString?unit_ids=$unitString&fields=$courseIdsFields")
            if (apiResponse.containsKey("response") && apiResponse["response"] != null) {
                val responseString: String = apiResponse["response"]!!
                return@withContext parseCourseUnitIds(parser.decodeFromString<Map<String, CourseUnitData?>>(responseString))
            } else {
                throw(Exception("API Error"))
            }
        }
    }
    public suspend fun fetchClasstypeIds(): Map<String, SharedDataClasses.IdAndName> {
        return withContext(Dispatchers.IO) {
            val apiResponse: Map<String, String> = OAuthSingleton.get(classtypeUrl)
            if (apiResponse.containsKey("response") && apiResponse["response"] != null) {
                val responseString: String = apiResponse["response"]!!
                return@withContext parser.decodeFromString<Map<String, SharedDataClasses.IdAndName>>(responseString)
            } else {
                throw(Exception("API Error"))
            }
        }
    }
    public suspend fun fetchUserGrades() : Pair<List<Season>, Map<String, CourseUnitIds>> ?{
        return withContext(Dispatchers.IO) {
            val termIds: String = getTermsIdsString()
            val gradesObject: List<Season> = getGradesObject(termIds)
            val courseUnitIdsString: String = getCOurseUnitIdsString(gradesObject)
            val parsedcourseUnitObject: Map<String, CourseUnitIds> = getCourseUnitIds(courseUnitIdsString)
            return@withContext Pair<List<Season>, Map<String, CourseUnitIds>>(gradesObject, parsedcourseUnitObject)
        }
    }
}
@Serializable
data class CourseGrades (
    val course_units_grades: Map<String, List<Map<String, TermGrade?>>>,
    val course_grades: List<Map<String, String> ?>?
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
@Serializable
data class CourseUnitIds( //it is the final structure for Unit ids and names, Json parser doesn't use it
    val course_name: String,
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