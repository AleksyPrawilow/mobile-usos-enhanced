package com.cdkentertainment.bux.view_models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.bux.R

class ScreenManagerViewModel: ViewModel() {
    var selectedScreen: Screens by mutableStateOf(Screens.LOGIN)
        private set
    var showFab: Boolean by mutableStateOf(false)

    fun changeScreen(newScreen: Screens, context: Context?) {
        if (selectedScreen == newScreen) {
            return
        }
        selectedScreen = newScreen
    }

    fun authorize() {
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
    //ATTENDANCE(R.string.attendance_page),
    LECTURERS(R.string.lecturers_page),
    SETTINGS(R.string.settings_page);

    companion object {
        fun fromOrdinal(ordinal: Int): Screens? = Screens.entries.getOrNull(ordinal)
    }
}