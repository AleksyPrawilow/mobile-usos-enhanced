package com.cdkentertainment.mobilny_usos_enhanced.models


import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.security.auth.Subject

class TestsPageModel {
    private val parser: Json = Json { ignoreUnknownKeys = true }
    private val allTestsUrl: String = "crstests/participant"
    private val singleSubjectFields: String
    = "name|description|id|students_points|grade_node_details[students_grade]|subnodes_deep"
    private val singleSubjectUrl: String = "crstests/node2"
    private fun parseAllTests(responseString: String): TestsContainer {
        val participantTests: TestsContainer = parser.decodeFromString<TestsContainer>(responseString)
        return participantTests
    }
    public suspend fun getAllTests(): TestsContainer {
        return withContext(Dispatchers.IO) {
            val response: Map<String, String> = OAuthSingleton.get(allTestsUrl)
            if (response.containsKey("response") && response["response"] != null) {
                val responseString: String = response["response"]!!
                val parsedParticipantsTests: TestsContainer = parseAllTests(responseString)
                return@withContext parsedParticipantsTests
            } else {
                throw(Exception("API Error"))
            }
        }
    }
    public suspend fun getSingleTestInfo(nodeId: Int) {
        return withContext(Dispatchers.IO) {
            val response: Map<String, String> = OAuthSingleton.get("$singleSubjectUrl?node_id=$nodeId&fields=$singleSubjectFields")
            println(response["response"]!!)
            val parsed = parser.decodeFromString<SubjectTestContainer>(response["response"]!!)
        }
    }
}
@Serializable
data class TestsContainer ( val tests: Map<String, Map<String, Test>>)
@Serializable
data class CourseEdition (
    val course_id: String,
    val course_name: SharedDataClasses.LangDict
)
@Serializable
data class LimitToGroupsObject (
    val course_unit_id: Int,
    val group_number: Int
)
@Serializable
data class Test (
    val node_id: Int,
    val course_edition: CourseEdition ?,
//    val limit_to_groups: LimitToGroupsObject, //narazie nie działa
    val name: SharedDataClasses.LangDict,
    val description: SharedDataClasses.LangDict
)
// --------------- subject info data classes -----------------
@Serializable
data class SubjectTestContainer (
    val name: SharedDataClasses.LangDict,
    val description: SharedDataClasses.LangDict,
    val id: Int,
    val students_points: StudentsPoints ?,
    val folder_node_details: FolderNodeDetails ?,
    val grade_node_details: GradeNodeDetails ?,
    val task_node_details: TaskNodeDetails ?,
    val subnodes_deep: List<SubjectTestContainer ?>?
)
@Serializable
data class StudentsPoints (
    val points: Float,
    val comment: String ?,
    val grader: SharedDataClasses.Human,
    val last_changed: String?
)
@Serializable
data class FolderNodeDetails (
    val points_max: Float?,
    val points_min: Float?
)
@Serializable
data class TaskNodeDetails (
    val points_min: Float ?,
    val points_max: Float ?,
    val points_precision: Float ?,
    val variables: String ?,
    val algorithm: String ?,
    val algorithm_description: SharedDataClasses.LangDict ?
)
@Serializable
data class GradeNodeDetails (
    val students_grade: StudentsGrade ?,
    //val all_students_grades: List<>
)
@Serializable
data class StudentsGrade (
    val grade_value: GradeValue ?,
    val automatic_grade_value: GradeValue ?,
    val comment: String ?,
    val last_changed: String ?
)
@Serializable
data class GradeValue (
    val order_key: Int ?,
    val symbol: String ?,
    val name: SharedDataClasses.LangDict ?
)
