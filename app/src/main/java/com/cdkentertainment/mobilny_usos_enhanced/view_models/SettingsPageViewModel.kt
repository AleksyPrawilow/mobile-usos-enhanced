package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.Theme
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.UserDataSingleton

class SettingsPageViewModel: ViewModel() {
    fun chooseTheme(newTheme: Theme) {
        UserDataSingleton.currentSettings.selectedTheme = newTheme.id
        UISingleton.changeTheme(newTheme)
    }
}