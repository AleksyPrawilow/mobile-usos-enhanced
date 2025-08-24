package com.cdkentertainment.mobilny_usos_enhanced.models


import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class TestsPageModel {
    @Serializable
    public data class TestsContainer ( val tests: Map<String, Map<String, Test>>)
    @Serializable
    public data class CourseEdition (
        val course_id: String,
        val course_name: SharedDataClasses.LangDict
    )
    @Serializable
    public data class Test (
        val node_id: Int,
        val course_edition: CourseEdition ?
    )
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