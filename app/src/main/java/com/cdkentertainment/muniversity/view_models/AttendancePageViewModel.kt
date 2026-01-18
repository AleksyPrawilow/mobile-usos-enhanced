package com.cdkentertainment.muniversity.view_models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.muniversity.UIHelper
import com.cdkentertainment.muniversity.UserDataSingleton
import com.cdkentertainment.muniversity.models.AttendanceDatesObject
import com.cdkentertainment.muniversity.models.AttendancePageModel
import com.cdkentertainment.muniversity.models.LessonGroup
import com.cdkentertainment.muniversity.models.LessonGroupPageModel
import com.cdkentertainment.muniversity.models.SharedDataClasses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AttendancePageViewModel: ViewModel() {
    var loading: Boolean by mutableStateOf(false)
    var error: Boolean by mutableStateOf(false)
    var loaded: Boolean by mutableStateOf(false)

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
    var classtypeIdInfo: Map<String, SharedDataClasses.IdAndName>? by mutableStateOf(null)

    suspend fun fetchLessonGroups() {
        if (lessonGroups != null || loaded) {
            return
        }
        withContext(Dispatchers.IO) {
            classtypeIdInfo = UIHelper.classTypeIds
            try {
                loading = true
                error = false
                loaded = false
                userId = UserDataSingleton.userData!!.id
                val groups = lessongGroupModel.getLessonGroups()
                lessonGroups = groups.groups[UIHelper.termIds.last().id]?.values?.toList() ?: emptyList()
                loading = false
                loaded = true
                error = false
            } catch (e: Exception) {
                loaded = false
                loading = false
                error = true
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
            val result = attendancePageModel.savePinnedGroups(pinnedGroups!!, "${UserDataSingleton.userData!!.id}_attendance_pinned_groups.json", context)
            return@withContext result.isSuccess
        }
    }

    suspend fun readPinnedGroups(context: Context): Boolean {
        if (pinnedGroups != null) {
            return true
        }
        return withContext(Dispatchers.IO) {
            try {
                pinnedGroups = attendancePageModel.readPinnedGroups("${UserDataSingleton.userData!!.id}_attendance_pinned_groups.json", context)
                pinnedGroupedBySubject = lessongGroupModel.mergeGroupsBySubjects(pinnedGroups!!)
                return@withContext true
            } catch (e: Exception) {
                e.printStackTrace()
                pinnedGroups = emptyList()
                pinnedGroupedBySubject = emptyMap()
                return@withContext true
            }
        }
    }

    suspend fun readAllCourseMeetings(group: LessonGroup) {
        if (unitMeetings[group.course_unit_id.toString()] != null) {
            return
        }
        withContext(Dispatchers.IO) {
            val dates: List<AttendanceDatesObject> = attendancePageModel.getGivenSubjectAttendanceDates(group.course_unit_id.toString(), group.group_number.toString())
            unitMeetings[group.course_unit_id.toString()] = dates
            return@withContext
        }
    }
}

data class PopupData(
    val classGroupData: LessonGroup
)