package com.cdkentertainment.muniversity.view_models

import androidx.lifecycle.ViewModel
import com.cdkentertainment.muniversity.Theme
import com.cdkentertainment.muniversity.UISingleton
import com.cdkentertainment.muniversity.UserDataSingleton

class SettingsPageViewModel: ViewModel() {
    fun chooseTheme(newTheme: Theme) {
        UserDataSingleton.currentSettings.selectedTheme = newTheme.id
        UISingleton.changeTheme(newTheme)
    }
}