package com.cdkentertainment.bux.view_models

import androidx.lifecycle.ViewModel
import com.cdkentertainment.bux.Theme
import com.cdkentertainment.bux.UISingleton
import com.cdkentertainment.bux.UserDataSingleton

class SettingsPageViewModel: ViewModel() {
    fun chooseTheme(newTheme: Theme) {
        UserDataSingleton.currentSettings.selectedTheme = newTheme.id
        UISingleton.changeTheme(newTheme)
    }
}