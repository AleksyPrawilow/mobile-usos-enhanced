package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerRate
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class LecturerRatesPageViewModel: ViewModel() {
    var selectedLecturer: SharedDataClasses.Human? by mutableStateOf(null)
        private set
    var lecturerRates: MutableMap<String, LecturerRate> = mutableStateMapOf<String, LecturerRate>()
    var userRatings: MutableMap<String, LecturerRate> = mutableStateMapOf<String, LecturerRate>()

    fun selectLecturer(lecturer: SharedDataClasses.Human) {
        selectedLecturer = lecturer
    }

    suspend fun addUserRate(lecturerId: String, rate: LecturerRate): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                delay(1000) // remove when database saving is ready
                //Save to database WITH CONVERTING THE RATES TO INT!!!!!!!!
                userRatings[lecturerId] = rate
                lecturerRates[lecturerId] = rate // for testing only, will most likely work in another way
                return@withContext true
            } catch (e: Exception) {
                return@withContext false
            }
        }
    }

    suspend fun deleteUserRate(lecturerId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // delete from database
                delay(1000) // remove when database deletion function is ready
                userRatings.remove(lecturerId)
                lecturerRates.remove(lecturerId) // for testing only, will most likely work in another way
                return@withContext true
            } catch (e: Exception) {
                return@withContext false
            }
        }
    }
}