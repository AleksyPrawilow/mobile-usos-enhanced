package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.UserDataSingleton

class SettingsPageViewModel: ViewModel() {
    var darkThemeChecked: Boolean by mutableStateOf(false)
        private set

    init {
        darkThemeChecked = UserDataSingleton.currentSettings.darkTheme
    }

    fun setDarkTheme(value: Boolean) {
        darkThemeChecked = value
        UserDataSingleton.currentSettings.darkTheme = value
        UISingleton.changeTheme(value)
    }
}