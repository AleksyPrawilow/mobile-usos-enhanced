package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel

class ScreenManagerViewModel: ViewModel() {
    var selectedScreen: Screens by mutableStateOf(Screens.LOGIN)
        private set
    var authorized: Boolean by mutableStateOf(false)

    fun changeScreen(newScreen: Screens) {
        if (selectedScreen == newScreen) {
            return
        }
        selectedScreen = newScreen
    }

    fun authorize() {
        authorized = true
        selectedScreen = Screens.HOME
    }
}

enum class Screens(val icon: ImageVector) {
    LOGIN(Icons.Rounded.Person),
    HOME(Icons.Rounded.Home),
    GRADES(Icons.Rounded.Star),
    TESTS(Icons.Rounded.Warning),
    CALENDAR(Icons.Rounded.DateRange),
    GROUPS(Icons.Rounded.Person),
    PAYMENTS(Icons.Rounded.ShoppingCart),
    ATTENDANCE(Icons.Rounded.Notifications),
    SETTINGS(Icons.Rounded.Settings);

    companion object {
        fun fromOrdinal(ordinal: Int): Screens? = Screens.entries.getOrNull(ordinal)
    }
}