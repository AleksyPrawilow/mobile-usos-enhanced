package com.cdkentertainment.mobilny_usos_enhanced.models


import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class TestsPageModel {
    private val parser: Json = Json { ignoreUnknownKeys = true }
    private val allTestsUrl: String = "crstests/participant"
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
