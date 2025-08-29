package com.cdkentertainment.mobilny_usos_enhanced.view_models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
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

enum class Screens(val pageName: Pair<String, String>) {
    LOGIN(Pair("Logowanie", "Login")),
    HOME(Pair("Home", "Home")),
    GRADES(Pair("Oceny", "Grades")),
    TESTS(Pair("Sprawdziany", "Tests")),
    CALENDAR(Pair("Kalendarz", "Calendar")),
    GROUPS(Pair("Grupy", "Groups")),
    PAYMENTS(Pair("Płatności", "Payments")),
    ATTENDANCE(Pair("Obecność", "Attendance")),
    LECTURERS(Pair("Prowadzacy", "Lecturers")),
    SETTINGS(Pair("Ustawienia", "Settings"));

    companion object {
        fun fromOrdinal(ordinal: Int): Screens? = Screens.entries.getOrNull(ordinal)
    }
}