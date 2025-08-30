package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses

class LecturerRatesPageViewModel: ViewModel() {
    var selectedLecturer: SharedDataClasses.Human? by mutableStateOf(null)
        private set

    fun selectLecturer(lecturer: SharedDataClasses.Human) {
        selectedLecturer = lecturer
    }
}