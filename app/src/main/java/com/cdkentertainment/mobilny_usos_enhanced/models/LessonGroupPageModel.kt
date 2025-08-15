package com.cdkentertainment.mobilny_usos_enhanced.models

import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class LessonGroupPageModel {
    private val parser = Json {ignoreUnknownKeys = true}
    private val fields = "course_id|course_name|lecturers|group_number|course_unit_id"
    private val requestUrl = "groups/user"
    private val participantUrl = "groups/group"
    private val participantField = "participants"


    private fun parseLessonGroupApiResponse(responseString: String): SeasonGroups {
        val parsedSeasonGroups: SeasonGroups = parser.decodeFromString<SeasonGroups>(responseString)
        return parsedSeasonGroups
    }

    private fun parseParticipantsApiResponse(responseString: String): Participants {
        val parsedParticipants: Participants = parser.decodeFromString(responseString)
        return parsedParticipants
    }

    public suspend fun getLessonGroups(): SeasonGroups {
        return withContext(Dispatchers.IO) {
            val response: Map<String, String> = OAuthSingleton.get("$requestUrl?fields=$fields")
            if (response.containsKey("response") && response["response"] != null) {
                val parsedLessonGroups: SeasonGroups = parseLessonGroupApiResponse(response["response"]!!)
                return@withContext parsedLessonGroups
            } else {
                throw(Exception("API Error"))
            }
        }
    }

   public suspend fun getParticipantOfGivenGroup(groupNumber: String, courseUnitId: String): Participants {
       return withContext(Dispatchers.IO) {
           val apiRequest: String = "$participantUrl?course_unit_id=$courseUnitId&group_number=$groupNumber&fields=$participantField"
           val response: Map<String, String> = OAuthSingleton.get(apiRequest)
           if (response.containsKey("response") && response["response"] != null) {
               val parsedParticipants = parseParticipantsApiResponse(response["response"]!!)
               return@withContext parsedParticipants
           } else {
               throw(Exception("API Error"))
           }
       }
   }
}

@Serializable
data class SeasonGroups (
    val groups: Map<String, List<LessonGroup>>
)

@Serializable
data class LessonGroup (
    val course_id: String,
    val course_unit_id: Int,
    val group_number: Int,
    val course_name: LangDict,
    val lecturers: List<Human>
)

@Serializable
data class Human (
    val id: String,
    val first_name: String,
    val last_name: String
)

@Serializable
data class Participants(
    val participants: List<Human>
)