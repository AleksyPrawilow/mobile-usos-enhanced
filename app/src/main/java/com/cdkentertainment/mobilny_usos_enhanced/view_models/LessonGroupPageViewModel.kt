package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.PeopleSingleton
import com.cdkentertainment.mobilny_usos_enhanced.Student
import com.cdkentertainment.mobilny_usos_enhanced.StudentData
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.models.GradesPageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroupPageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.Participants
import com.cdkentertainment.mobilny_usos_enhanced.models.SeasonGroupsGroupedBySubject
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main(): Unit = runBlocking {
    OAuthSingleton.setTestAccessToken()
    val model = LessonGroupPageModel()
    val lessonGroups = model.getLessonGroups()
    val participants = model.getParticipantOfGivenGroup("13", "461821") //do argumentów trzeba przekonwertować numer grupy i unitid z int na string
    println(lessonGroups)
    println(participants)
}

class LessonGroupPageViewModel: ViewModel() {
    var lessonGroups: SeasonGroupsGroupedBySubject? by mutableStateOf(null)
        private set
    var groupDetails = mutableMapOf<String, GroupDetails>()

    private val model: LessonGroupPageModel = LessonGroupPageModel()
    private val gradesPageModel: GradesPageModel = GradesPageModel()
    var classtypeIdInfo: Map<String, SharedDataClasses.IdAndName>? by mutableStateOf(null)

    suspend fun fetchLessonGroups() {
        withContext(Dispatchers.IO) {
            classtypeIdInfo = UIHelper.classTypeIds
            if (lessonGroups != null) {
                return@withContext
            }
            try {
                lessonGroups = model.getLessonGroups()
                for (seasonId in lessonGroups!!.groups.keys) {
                    val season: Map<String, List<LessonGroup>>? = lessonGroups!!.groups[seasonId]
                    if (season != null) {
                        for (subject in season.keys) {
                            val courseUnit: List<LessonGroup>? = season[subject]
                            if (courseUnit != null) {
                                for (group in courseUnit) {
                                    groupDetails["${group.course_unit_id.toString()}-${group.group_number}"] = GroupDetails()
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // TODO: Add error handling
                return@withContext
            }
        }
    }

    suspend fun fetchParticipants(groupNumber: String, courseUnitId: String): Participants? {
        return withContext(Dispatchers.IO) {
            try {
                return@withContext model.getParticipantOfGivenGroup(groupNumber, courseUnitId)
            } catch (e: Exception) {
                // TODO: Add error handling
                return@withContext null
            }
        }
    }

    suspend fun fetchUserInfo(human: SharedDataClasses.Human): Boolean {
        if (PeopleSingleton.students.containsKey(human.id)) return true
        return withContext(Dispatchers.IO) {
            try {
                val studentData: StudentData = model.fetchUserInfo(human)
                PeopleSingleton.students[human.id] = Student(human, studentData)
                return@withContext true
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }
    }
}

class GroupDetails {
    var showDetails: Boolean by mutableStateOf(false)
    var participants: Participants? by mutableStateOf(null)
}