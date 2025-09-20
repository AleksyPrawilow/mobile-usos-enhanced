package com.cdkentertainment.mobilny_usos_enhanced.view_models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.models.AttendanceDatesObject
import com.cdkentertainment.mobilny_usos_enhanced.models.AttendancePageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.GradesPageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroupPageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

//----- Tutaj dałem tymczasowo funkcję do testowania
fun main(): Unit = runBlocking {
    OAuthSingleton.setTestAccessToken()
    launch {
        val model = AttendancePageModel()
        val test = model.getGivenSubjectAttendanceDates("461821", "13")
        println(model.filterPastAttendanceDates(test))
    }
}
class AttendancePageViewModel: ViewModel() {
    var unitMeetings: MutableMap<String, List<AttendanceDatesObject>> = mutableStateMapOf()
    var lessonGroups: List<List<LessonGroup>>? by mutableStateOf(null)
        private set
    var pinnedGroups: List<LessonGroup>? = null
        private set
    var pinnedGroupedBySubject: Map<String, List<LessonGroup>>? by mutableStateOf(null)
        private set
    var showPopup: Boolean by mutableStateOf(false)
        private set
    var showUnpinnedPopup: Boolean by mutableStateOf(false)
    var savingAttendance: Boolean by mutableStateOf(false)
    var popupData: PopupData? by mutableStateOf(null)
        private set
    private var userId: String? = null

    private val lessongGroupModel: LessonGroupPageModel = LessonGroupPageModel()
    private val attendancePageModel: AttendancePageModel = AttendancePageModel()
    private val gradesPageModel: GradesPageModel = GradesPageModel()
    var classtypeIdInfo: Map<String, SharedDataClasses.IdAndName>? by mutableStateOf(null)

    suspend fun fetchLessonGroups() {
        withContext(Dispatchers.IO) {
            if (UIHelper.classTypeIds.isEmpty()) {
                try {
                    UIHelper.classTypeIds = gradesPageModel.fetchClasstypeIds()
                    classtypeIdInfo = UIHelper.classTypeIds
                } catch (e: Exception) {
                    println(e)
                }
            } else {
                classtypeIdInfo = UIHelper.classTypeIds
            }
            userId = OAuthSingleton.userData!!.basicInfo.id
            if (lessonGroups != null) {
                return@withContext
            }
            try {
                // TODO: Powinien być bieżący semestr!!!
                val groups = lessongGroupModel.getLessonGroups()
                lessonGroups = groups.groups["2025/SZ"]?.values?.toList() ?: emptyList()
            } catch (e: Exception) {
                // TODO: Add error handling
                return@withContext
            }
        }
    }

    fun showPopup(classGroupData: LessonGroup) {
        if (showPopup) {
            return
        }
        showPopup = true
        popupData = PopupData(classGroupData)
    }

    fun showUnpinnedPopup(classGroupData: LessonGroup) {
        if (showUnpinnedPopup) {
            return
        }
        showUnpinnedPopup = true
        popupData = PopupData(classGroupData)
    }

    fun dismissPopup() {
        showPopup = false
        popupData = null
    }

    fun dismissUnpinnedPopup() {
        showUnpinnedPopup = false
        popupData = null
    }

    suspend fun pinGroup(group: LessonGroup, context: Context) {
        withContext(Dispatchers.IO) {
            if (pinnedGroups == null) {
                pinnedGroups = emptyList()
            }
            val pinnedGroupsOldSize: Int = pinnedGroups!!.size
            pinnedGroups = pinnedGroups!! + group
            pinnedGroups = pinnedGroups!!.distinctBy { it.course_unit_id }
            if (pinnedGroupsOldSize == pinnedGroups!!.size) {
                return@withContext
            }
            savePinnedGroups(context)
            pinnedGroupedBySubject = lessongGroupModel.mergeGroupsBySubjects(pinnedGroups!!)
        }
    }

    suspend fun removePin(group: LessonGroup, context: Context) {
        withContext(Dispatchers.IO) {
            if (pinnedGroups == null) {
                return@withContext
            }
            pinnedGroups = pinnedGroups!!.filter { it.course_unit_id != group.course_unit_id }
            savePinnedGroups(context)
            pinnedGroupedBySubject = lessongGroupModel.mergeGroupsBySubjects(pinnedGroups!!)
        }
    }

    suspend fun savePinnedGroups(context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            if (pinnedGroups == null) {
                return@withContext false
            }
            val result = attendancePageModel.savePinnedGroups(pinnedGroups!!, "${OAuthSingleton.userData!!.basicInfo.id}_attendance_pinned_groups.json", context)
            return@withContext result.isSuccess
        }
    }

    suspend fun readPinnedGroups(context: Context) {
        withContext(Dispatchers.IO) {
            pinnedGroups = attendancePageModel.readPinnedGroups("${OAuthSingleton.userData!!.basicInfo.id}_attendance_pinned_groups.json", context)
            pinnedGroupedBySubject = lessongGroupModel.mergeGroupsBySubjects(pinnedGroups!!)
        }
    }

    suspend fun readAllCourseMeetings(group: LessonGroup): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (unitMeetings[group.course_unit_id.toString()] != null) {
                    return@withContext true
                }
                val dates: List<AttendanceDatesObject> = attendancePageModel.getGivenSubjectAttendanceDates(group.course_unit_id.toString(), group.group_number.toString())
                unitMeetings[group.course_unit_id.toString()] = dates
                return@withContext true
            } catch (e: Exception) {
                return@withContext false
            }
        }
    }
}

data class PopupData(
    val classGroupData: LessonGroup
)