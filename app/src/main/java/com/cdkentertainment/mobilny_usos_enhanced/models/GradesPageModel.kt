package com.cdkentertainment.mobilny_usos_enhanced.models

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class GradesPageModel {
    private val parser = Json{ignoreUnknownKeys = true}
    private val gradesUrl: String = "Grades"
    private val termIdsUrl: String = "TermIds"
    private val classtypeUrl: String = "ClassTypeIds"
    private val examDistributionUrl: String = "Distribution"

    public suspend fun fetchUserGrades(seasonId: String) : Season{
        return withContext(Dispatchers.IO) {
            val response: BackendDataSender.BackendResponse = BackendDataSender.get("$gradesUrl?seasonId=$seasonId")
            if (response.statusCode == 200 && response.body != null) {
                val responseString: String = response.body!!
                val parsedGrades: Season = parser.decodeFromString<Season>(responseString)
                return@withContext parsedGrades
            } else {
                throw(Exception("API Error"))
            }
        }
    }
    public suspend fun getGivenExamGradesDistribution(examId: Int): GradesDistribution {
        return withContext(Dispatchers.IO) {
            val response: BackendDataSender.BackendResponse = BackendDataSender.get("$gradesUrl/$examDistributionUrl?id=$examId")
            if (response.statusCode == 200 && response.body != null) {
                val responseString: String = response.body!!
                val parsedExamDistribution: GradesDistribution = parser.decodeFromString<GradesDistribution>(responseString)
                return@withContext parsedExamDistribution
            } else {
                throw(Exception("API Error"))
            }
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
    val avgGrade: Float ? = null,
    val courseList: List<Course>,
    var courseUnitIds: Map<String, CourseUnitData>? = null
)
@Serializable
data class Course (
    val courseId: String,
    val courseGrades: CourseGrades
)
@Serializable
data class CourseUnitData(
    val course_name: SharedDataClasses.LangDict,
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
    val modification_author: SharedDataClasses.Human? = null,
    val counts_into_average: String
)

@Serializable
data class GradesDistribution (
    val grades_distribution: List<SingleGradeStatistic>,
    val type_description: SharedDataClasses.LangDict,
    val description: SharedDataClasses.LangDict
)
@Serializable
data class SingleGradeStatistic (
    val percentage: Int,
    val grade_symbol: String
)