package com.cdkentertainment.mobilny_usos_enhanced.view_models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.AttendancePageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.HomePageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroupPageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.SeasonGroups
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

//----- Tutaj dałem tymczasowo funkcję do testowania
fun main(): Unit = runBlocking {
    OAuthSingleton.setTestAccessToken()
    launch {
        val model = AttendancePageModel()
        val test = model.getGivenSubjectAttendanceDates("461823", "111")
        println(test)
    }
}
class AttendancePageViewModel: ViewModel() {
    var lessonGroups: SeasonGroups? by mutableStateOf(null)
        private set
    var showPopup: Boolean by mutableStateOf(false)
        private set
    var savingAttendance: Boolean by mutableStateOf(false)
    var popupData: PopupData? by mutableStateOf(null)
        private set
    private var userId: String? = null

    private val lessongGroupModel: LessonGroupPageModel = LessonGroupPageModel()
    private val userInfoModel: HomePageModel = HomePageModel()

    suspend fun fetchLessonGroups() {
        withContext(Dispatchers.IO) {
            if (userId == null) {
                try {
                    userId = userInfoModel.fetchUserData()!!.basicInfo.id
                } catch (e: Exception) {
                    // TODO: Add error handling
                    return@withContext
                }
            }
            if (lessonGroups != null) {
                return@withContext
            }
            try {
                lessonGroups = lessongGroupModel.getLessonGroups()
            } catch (e: Exception) {
                // TODO: Add error handling
                return@withContext
            }
        }
    }

    suspend fun showPopup(classGroupData: LessonGroup, context: Context) {
        if (showPopup) {
            return
        }
        withContext(Dispatchers.IO) {
            showPopup = true
            // Read the attendance data from a file using the id and course_unit_id
            popupData = PopupData(classGroupData)
        }
    }

    suspend fun dismissPopup(context: Context) {
        if (savingAttendance) {
            return
        }
        withContext(Dispatchers.IO) {
            savingAttendance = true
            delay(100) // Save the attendance data using the id and course_unit_id
            savingAttendance = false
            showPopup = false
            popupData = null
        }
    }
}

data class PopupData(
    val classGroupData: LessonGroup
    // Some attendance map value
)