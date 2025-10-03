package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cdkentertainment.mobilny_usos_enhanced.Lecturer
import com.cdkentertainment.mobilny_usos_enhanced.PeopleSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UserDataSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerRate
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerRatesPageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturersIndex
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import com.cdkentertainment.mobilny_usos_enhanced.models.UserRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main(): Unit = runBlocking {
    val model = LecturerRatesPageModel()
    println(model.getStaffIndex("1234", 10, 10))
}
class LecturerRatesPageViewModel: ViewModel() {
    var selectedLecturer: SharedDataClasses.Human? by mutableStateOf(null)
        private set
    var lecturerRates: MutableMap<String, LecturerRate> = mutableStateMapOf<String, LecturerRate>()
    var userRatings: MutableMap<String, UserRate> = mutableStateMapOf<String, UserRate>()

    // *****Lecturers Index block*****
    var lecturersIndexLoading: MutableMap<String, Boolean> = mutableStateMapOf()
    var lecturersIndexLoaded: MutableMap<String, Boolean> = mutableStateMapOf()
    var lecturersIndexError: MutableMap<String, Boolean> = mutableStateMapOf()
    var lecturersIndex: MutableMap<String, List<SharedDataClasses.Human>> = mutableStateMapOf()
    var lecturersIndexTotal: MutableMap<String, Int> = mutableStateMapOf()
    var lecturersIndexNextPage: Boolean = true
    // *****End lecturers Index block*****

    // *****Lecturers search block*****
    var lecturersSearchLoading: Boolean by mutableStateOf(false)
    var lecturersSearchLoaded: Boolean by mutableStateOf(false)
    var lecturersSearchError: Boolean by mutableStateOf(false)
    var lastQuery: String by mutableStateOf("")
    var lastQueryResults: List<Lecturer>? by mutableStateOf(null)
    // *****End lecturers search block*****

    fun selectLecturer(lecturer: SharedDataClasses.Human) {
        selectedLecturer = lecturer
    }
    private val model = LecturerRatesPageModel()

    suspend fun addUserRate(lecturerId: String, rate: LecturerRate): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val userRate = UserRate(
                    userId = UserDataSingleton.userData!!.id.toInt(),
                    lecturerId = lecturerId.toInt(),
                    universityId = 1, //TEMPORARY
                    rate1 = rate.rate_1.toInt(),
                    rate2 = rate.rate_2.toInt(),
                    rate3 = rate.rate_3.toInt(),
                    rate4 = rate.rate_4.toInt(),
                    rate5 = rate.rate_5.toInt()
                )
                model.addUserRate(userRate)
                userRatings[lecturerId] = UserRate(
                    userId = UserDataSingleton.userData!!.id.toInt(),
                    lecturerId = lecturerId.toInt(),
                    universityId = 1, //TEMPORARY
                    rate1 = rate.rate_1.toInt(),
                    rate2 = rate.rate_2.toInt(),
                    rate3 = rate.rate_3.toInt(),
                    rate4 = rate.rate_4.toInt(),
                    rate5 = rate.rate_5.toInt()
                )
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

    fun getLecturersRatings(lecturerIds: List<String>, onSuccess: () -> Unit = {}, onError: () -> Unit = {}) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val nonFetchedIds: List<String> = lecturerIds.filter { !PeopleSingleton.lecturers.containsKey(it) }
                try {
                    model.getExtendedLecturersInfo(nonFetchedIds)
                    onSuccess()
                } catch (e: Exception) {
                    onError()
                }
            }
        }
    }

    fun fetchUserRates(onSuccess: () -> Unit = {}, onFailed: () -> Unit = {}) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val rates: List<UserRate> = model.getUserRates(UserDataSingleton.userData!!.id.toInt())
                    rates.associateTo(userRatings) { it.lecturerId.toString() to it }
                    onSuccess()
                } catch (e: Exception) {
                    e.printStackTrace()
                    onFailed()
                }
            }
        }
    }

    suspend fun suspendLoadStaffIndex(facultyId: String = "0600000000", offset: Int = 0, pageSize: Int = 20) {
        if (lecturersIndex["$facultyId/$offset"] != null) return
        lecturersIndexLoading["$facultyId/$offset"] = true
        lecturersIndexError["$facultyId/$offset"] = false
        withContext(Dispatchers.IO) {
            try {
                val index: LecturersIndex = model.getStaffIndex(facultyId, offset, pageSize)
                model.getLecturersInfo(index.users)
                lecturersIndex["$facultyId/$offset"] = index.users
                lecturersIndexTotal[facultyId] = index.total
                lecturersIndexNextPage = index.next_page
                lecturersIndexLoading["$facultyId/$offset"] = false
                lecturersIndexLoaded["$facultyId/$offset"] = true
            } catch (e: Exception) {
                e.printStackTrace()
                lecturersIndexError["$facultyId/$offset"] = true
                lecturersIndexLoading["$facultyId/$offset"] = false
            }
        }
    }

    fun loadStaffIndex(facultyId: String = "0600000000", offset: Int = 0, pageSize: Int = 20, onFetched: () -> Unit = {}) {
        if (lecturersIndex["$facultyId/$offset"] != null) {
            onFetched()
            return
        }
        lecturersIndexLoading["$facultyId/$offset"] = true
        lecturersIndexError["$facultyId/$offset"] = false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val index: LecturersIndex = model.getStaffIndex(facultyId, offset, pageSize)
                    model.getLecturersInfo(index.users)
                    lecturersIndex["$facultyId/$offset"] = index.users
                    lecturersIndexTotal[facultyId] = index.total
                    lecturersIndexNextPage = index.next_page
                    lecturersIndexLoading["$facultyId/$offset"] = false
                    lecturersIndexLoaded["$facultyId/$offset"] = true
                    onFetched()
                } catch (e: Exception) {
                    e.printStackTrace()
                    lecturersIndexError["$facultyId/$offset"] = true
                    lecturersIndexLoading["$facultyId/$offset"] = false
                    onFetched()
                }
            }
        }
    }

    suspend fun queryLecturersSearch(query: String, force: Boolean = false) {
        if (lastQuery == query && !force) {
            return
        }
        lecturersSearchLoading = true
        lecturersSearchError = false
        lecturersSearchLoaded = false
        withContext(Dispatchers.IO) {
            try {
                lastQuery = query
                val lecturers: List<SharedDataClasses.Human> = model.queryLecturersSearch(query)
                val filteredLecturers: List<SharedDataClasses.Human> = lecturers.filter { !PeopleSingleton.lecturers.containsKey(it.id) }
                model.getLecturersInfo(filteredLecturers)
                lastQueryResults = lecturers.map { PeopleSingleton.lecturers[it.id]!! }
                lecturersSearchLoading = false
                lecturersSearchLoaded = true
            } catch(e: Exception) {
                e.printStackTrace()
                lastQueryResults = null
                lecturersSearchError = true
                lecturersSearchLoading = false
            }
        }
    }
}