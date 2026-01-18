package com.cdkentertainment.muniversity.models

import com.cdkentertainment.muniversity.StudentData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class LessonGroupPageModel {
    private val parser = Json {ignoreUnknownKeys = true}
    private val requestUrl = "LessonGroups/Groups"
    private val participantUrl = "LessonGroups/Participants"
    private val userUrl = "Users/User"
    public fun mergeGroupsBySubjects(seasonGroups: List<LessonGroup>): Map<String, List<LessonGroup>> {
        val groupedBySubjects = seasonGroups.groupBy { group ->
            group.course_id
        }
        return groupedBySubjects
    }
    public suspend fun getLessonGroups(): SeasonGroupsGroupedBySubject {
        return withContext(Dispatchers.IO) {
            val response: BackendDataSender.BackendResponse = BackendDataSender.get(requestUrl)
            if (response.statusCode == 200 && response.body != null) {
                val parsedLessonGroups: SeasonGroupsGroupedBySubject = parser.decodeFromString<SeasonGroupsGroupedBySubject>(response.body!!)
                return@withContext parsedLessonGroups
            } else {
                throw(Exception("API Error"))
            }
        }
    }
    public suspend fun getParticipantOfGivenGroup(groupNumber: String, courseUnitId: String): Participants {
       return withContext(Dispatchers.IO) {
           val apiRequest: String = "$participantUrl?courseUnitId=$courseUnitId&groupNumber=$groupNumber"
           val response: BackendDataSender.BackendResponse = BackendDataSender.get(apiRequest)
           if (response.statusCode == 200 && response.body != null) {
               val parsedLessonGroups: Participants = parser.decodeFromString<Participants>(response.body!!)
               return@withContext parsedLessonGroups
           } else {
               throw (Exception("API Error"))
           }
       }
    }
    public suspend fun fetchUserInfo(human: SharedDataClasses.Human): StudentData {
        return withContext(Dispatchers.IO) {
            val request: String = "$userUrl?userId=${human.id}"
            val response: BackendDataSender.BackendResponse = BackendDataSender.get(request)
            if (response.statusCode == 200 && response.body != null) {
                val parsedStudentData: StudentData = parser.decodeFromString<StudentData>(response.body!!)
                return@withContext parsedStudentData
            } else {
                throw (Exception("API Error"))
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