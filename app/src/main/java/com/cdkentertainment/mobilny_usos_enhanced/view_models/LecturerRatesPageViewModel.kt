package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerRate
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerRatesPageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import com.cdkentertainment.mobilny_usos_enhanced.models.UserRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class LecturerRatesPageViewModel: ViewModel() {
    var selectedLecturer: SharedDataClasses.Human? by mutableStateOf(null)
        private set
    var lecturerRates: MutableMap<String, LecturerRate> = mutableStateMapOf<String, LecturerRate>()
    var userRatings: MutableMap<String, LecturerRate> = mutableStateMapOf<String, LecturerRate>()

    fun selectLecturer(lecturer: SharedDataClasses.Human) {
        selectedLecturer = lecturer
    }
    private val model = LecturerRatesPageModel()

    suspend fun addUserRate(lecturerId: String, rate: LecturerRate): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val userRate = UserRate(
                    userId = OAuthSingleton.userData!!.basicInfo.id.toInt(),
                    lecturerId = lecturerId.toInt(),
                    universityId = 1, //TEMPORARY
                    rate1 = rate.rate_1.toInt(),
                    rate2 = rate.rate_2.toInt(),
                    rate3 = rate.rate_3.toInt(),
                    rate4 = rate.rate_4.toInt(),
                    rate5 = rate.rate_5.toInt()
                )
                model.addUserRate(userRate)
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