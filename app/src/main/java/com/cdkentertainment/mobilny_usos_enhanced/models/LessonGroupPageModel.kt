package com.cdkentertainment.mobilny_usos_enhanced.models

import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class LessonGroupPageModel {
    private val parser = Json {ignoreUnknownKeys = true}
    private val fields = "course_id|course_name|lecturers|group_number|course_unit_id|class_type_id"
    private val requestUrl = "groups/user"
    private val participantUrl = "groups/group"
    private val participantField = "participants"
    @Serializable
    private data class SeasonGroups (
        val groups: Map<String, List<LessonGroup>>
    )
    private fun parseLessonGroupApiResponse(responseString: String): SeasonGroupsGroupedBySubject {
        val parsedSeasonGroups: SeasonGroups = parser.decodeFromString<SeasonGroups>(responseString)
        val groupsGroupedBySubjects: SeasonGroupsGroupedBySubject =
            SeasonGroupsGroupedBySubject(mutableMapOf<String, Map<String, List<LessonGroup>>>())
        for (season in parsedSeasonGroups.groups.keys) {
            val seasonGroups = parsedSeasonGroups.groups[season]
            if (seasonGroups != null) {
                groupsGroupedBySubjects.groups.put(season, mergeGroupsBySubjects(seasonGroups))
            } else {
                print("todo")
            }
        }
        return groupsGroupedBySubjects
    }
    private fun parseParticipantsApiResponse(responseString: String): Participants {
        val parsedParticipants: Participants = parser.decodeFromString(responseString)
        return parsedParticipants
    }
    public fun mergeGroupsBySubjects(seasonGroups: List<LessonGroup>): Map<String, List<LessonGroup>> {
        val groupedBySubjects = seasonGroups.groupBy { group ->
            group.course_id
        }
        return groupedBySubjects
    }
    public suspend fun getLessonGroups(): SeasonGroupsGroupedBySubject {
        return withContext(Dispatchers.IO) {
            val response: Map<String, String> = OAuthSingleton.get("$requestUrl?fields=$fields")
            if (response.containsKey("response") && response["response"] != null) {
                val parsedLessonGroups: SeasonGroupsGroupedBySubject = parseLessonGroupApiResponse(response["response"]!!)
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
data class SeasonGroupsGroupedBySubject (
    val groups: MutableMap<String, Map<String, List<LessonGroup>>>
)
@Serializable
data class LessonGroup (
    val course_id: String,
    val course_unit_id: Int,
    val group_number: Int,
    val course_name: SharedDataClasses.LangDict,
    val lecturers: List<SharedDataClasses.Human>,
    val class_type_id: String
)
@Serializable
data class Participants(
    val participants: List<SharedDataClasses.Human>
)