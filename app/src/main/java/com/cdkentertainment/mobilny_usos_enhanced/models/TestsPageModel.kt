package com.cdkentertainment.mobilny_usos_enhanced.models


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class TestsPageModel {
    private val parser: Json = Json { ignoreUnknownKeys = true }
    private val allTestsUrl: String = "Tests"
    private val singleSubjectUrl: String = "Tests/Core"
    private fun parseAllTests(responseString: String): TestsContainer {
        val participantTests: TestsContainer = parser.decodeFromString<TestsContainer>(responseString)
        return participantTests
    }
    public suspend fun getAllTests(): TestsContainer {
        return withContext(Dispatchers.IO) {
            val response: BackendDataSender.BackendResponse = BackendDataSender.get(allTestsUrl)
            if (response.statusCode == 200 && response.body != null) {
                val responseString: String = response.body!!
                val parsedParticipantsTests: TestsContainer = parser.decodeFromString<TestsContainer>(responseString)
                return@withContext parsedParticipantsTests
            } else {
                throw(Exception("API Error"))
            }
        }
    }
    public suspend fun getSingleTestInfo(nodeId: Int): SubjectTestContainer {
        return withContext(Dispatchers.IO) {
            val response: BackendDataSender.BackendResponse = BackendDataSender.get("$singleSubjectUrl?id=$nodeId")
            println(response)
            if (response.statusCode == 200 && response.body != null) {
                val parsedTests: SubjectTestContainer = parser.decodeFromString<SubjectTestContainer>(response.body!!)
                return@withContext parsedTests
            } else {
                throw(Exception("API Error"))
            }
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
    val name: SharedDataClasses.LangDict ?,
    val description: SharedDataClasses.LangDict ?,
    val id: Int ?,
    val grade_node_details: GradeNodeDetails ?,
    val task_node_details: TaskNodeDetails ?,
    val subnodes_deep: List<SubjectTestContainer ?>?
)
@Serializable
data class StudentsPoints (
    val points: Float?,
)
@Serializable
data class TaskNodeDetails (
    val students_points: StudentsPoints?
)
@Serializable
data class GradeNodeDetails (
    val students_grade: StudentsGrade ?,
)
@Serializable
data class StudentsGrade (
    val grade_value: GradeValue ?
)
@Serializable
data class GradeValue (
    val symbol: String ?
)
