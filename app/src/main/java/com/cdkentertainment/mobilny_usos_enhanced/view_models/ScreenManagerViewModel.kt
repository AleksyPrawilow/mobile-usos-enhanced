package com.cdkentertainment.mobilny_usos_enhanced.view_models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UserDataSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScreenManagerViewModel: ViewModel() {
    var selectedScreen: Screens by mutableStateOf(Screens.LOGIN)
        private set
    var authorized: Boolean by mutableStateOf(false)

    fun changeScreen(newScreen: Screens, context: Context?) {
        if (selectedScreen == newScreen) {
            return
        }
        if (selectedScreen == Screens.SETTINGS && context != null) {
            CoroutineScope(Dispatchers.IO).launch {
                UserDataSingleton.saveUserSettings(context)
            }
        }
        selectedScreen = newScreen
    }

    fun authorize() {
        authorized = true
        changeScreen(Screens.HOME, null)
    }
}

enum class Screens(val pageName: Int) {
    LOGIN(R.string.app_name),
    HOME(R.string.home_page),
    GRADES(R.string.grade_page),
    TESTS(R.string.tests_page),
    CALENDAR(R.string.schedule_page),
    GROUPS(R.string.class_groups_page),
    PAYMENTS(R.string.payments_page),
    ATTENDANCE(R.string.attendance_page),
    LECTURERS(R.string.lecturers_page),
    SETTINGS(R.string.settings_page);

    companion object {
        fun fromOrdinal(ordinal: Int): Screens? = Screens.entries.getOrNull(ordinal)
    }
}