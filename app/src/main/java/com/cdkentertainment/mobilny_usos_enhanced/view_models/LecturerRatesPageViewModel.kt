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
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerAvgRates
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerRate
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerRatesPageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturersIndex
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import com.cdkentertainment.mobilny_usos_enhanced.models.UserRate
import com.cdkentertainment.mobilny_usos_enhanced.models.UserRatePatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                val prevRate: LecturerAvgRates = PeopleSingleton.lecturers[lecturerId]!!.rating
                val newAvgRate: LecturerAvgRates = LecturerAvgRates(
                    lecturerId = lecturerId.toInt(),
                    universityId = 1,
                    ratesCount = prevRate.ratesCount + 1,
                    avgRate1 = (prevRate.avgRate1 * prevRate.ratesCount + rate.rate_1) / (prevRate.ratesCount + 1),
                    avgRate2 = (prevRate.avgRate2 * prevRate.ratesCount + rate.rate_2) / (prevRate.ratesCount + 1),
                    avgRate3 = (prevRate.avgRate3 * prevRate.ratesCount + rate.rate_3) / (prevRate.ratesCount + 1),
                    avgRate4 = (prevRate.avgRate4 * prevRate.ratesCount + rate.rate_4) / (prevRate.ratesCount + 1),
                    avgRate5 = (prevRate.avgRate5 * prevRate.ratesCount + rate.rate_5) / (prevRate.ratesCount + 1),
                )
                PeopleSingleton.lecturers[lecturerId]!!.rating = newAvgRate
                return@withContext true
            } catch (e: Exception) {
                return@withContext false
            }
        }
    }

    suspend fun deleteUserRate(lecturerId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                model.deleteUserRate(lecturerId.toInt(), 1)
                val userRate: UserRate = userRatings[lecturerId]!!
                userRatings.remove(lecturerId)
                lecturerRates.remove(lecturerId) // for testing only, will most likely work in another way
                val prevRate: LecturerAvgRates = PeopleSingleton.lecturers[lecturerId]!!.rating
                val newAvgRate: LecturerAvgRates = LecturerAvgRates(
                    lecturerId = lecturerId.toInt(),
                    universityId = 1,
                    ratesCount = prevRate.ratesCount - 1,
                    avgRate1 = if (prevRate.ratesCount - 1 == 0) 0.0f else (prevRate.avgRate1 * prevRate.ratesCount - userRate.rate1.toFloat()) / (prevRate.ratesCount - 1),
                    avgRate2 = if (prevRate.ratesCount - 1 == 0) 0.0f else (prevRate.avgRate2 * prevRate.ratesCount - userRate.rate2.toFloat()) / (prevRate.ratesCount - 1),
                    avgRate3 = if (prevRate.ratesCount - 1 == 0) 0.0f else (prevRate.avgRate3 * prevRate.ratesCount - userRate.rate3.toFloat()) / (prevRate.ratesCount - 1),
                    avgRate4 = if (prevRate.ratesCount - 1 == 0) 0.0f else (prevRate.avgRate4 * prevRate.ratesCount - userRate.rate4.toFloat()) / (prevRate.ratesCount - 1),
                    avgRate5 = if (prevRate.ratesCount - 1 == 0) 0.0f else (prevRate.avgRate5 * prevRate.ratesCount - userRate.rate5.toFloat()) / (prevRate.ratesCount - 1),
                )
                PeopleSingleton.lecturers[lecturerId]!!.rating = newAvgRate
                return@withContext true
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }
    }

    suspend fun updateUserRate(lecturerId: String, rate: LecturerRate): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val prevUserRate: UserRate = userRatings[lecturerId]!!
                val ratePatch = UserRatePatch(
                    lecturerId = lecturerId.toInt(),
                    universityId = 1,
                    rate1 = rate.rate_1.toInt(),
                    rate2 = rate.rate_2.toInt(),
                    rate3 = rate.rate_3.toInt(),
                    rate4 = rate.rate_4.toInt(),
                    rate5 = rate.rate_5.toInt()
                )
                model.updateUserRate(ratePatch)
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
                val prevRate: LecturerAvgRates = PeopleSingleton.lecturers[lecturerId]!!.rating
                val prevRateModified: LecturerAvgRates = LecturerAvgRates(
                    lecturerId = lecturerId.toInt(),
                    universityId = 1,
                    ratesCount = prevRate.ratesCount - 1,
                    avgRate1 = if (prevRate.ratesCount - 1 == 0) 0.0f else (prevRate.avgRate1 * prevRate.ratesCount - prevUserRate.rate1.toFloat()) / (prevRate.ratesCount - 1),
                    avgRate2 = if (prevRate.ratesCount - 1 == 0) 0.0f else (prevRate.avgRate2 * prevRate.ratesCount - prevUserRate.rate2.toFloat()) / (prevRate.ratesCount - 1),
                    avgRate3 = if (prevRate.ratesCount - 1 == 0) 0.0f else (prevRate.avgRate3 * prevRate.ratesCount - prevUserRate.rate3.toFloat()) / (prevRate.ratesCount - 1),
                    avgRate4 = if (prevRate.ratesCount - 1 == 0) 0.0f else (prevRate.avgRate4 * prevRate.ratesCount - prevUserRate.rate4.toFloat()) / (prevRate.ratesCount - 1),
                    avgRate5 = if (prevRate.ratesCount - 1 == 0) 0.0f else (prevRate.avgRate5 * prevRate.ratesCount - prevUserRate.rate5.toFloat()) / (prevRate.ratesCount - 1)
                )
                val newAvgRate: LecturerAvgRates = LecturerAvgRates(
                    lecturerId = lecturerId.toInt(),
                    universityId = 1,
                    ratesCount = prevRateModified.ratesCount + 1,
                    avgRate1 = (prevRateModified.avgRate1 * prevRateModified.ratesCount + rate.rate_1) / (prevRateModified.ratesCount + 1),
                    avgRate2 = (prevRateModified.avgRate2 * prevRateModified.ratesCount + rate.rate_2) / (prevRateModified.ratesCount + 1),
                    avgRate3 = (prevRateModified.avgRate3 * prevRateModified.ratesCount + rate.rate_3) / (prevRateModified.ratesCount + 1),
                    avgRate4 = (prevRateModified.avgRate4 * prevRateModified.ratesCount + rate.rate_4) / (prevRateModified.ratesCount + 1),
                    avgRate5 = (prevRateModified.avgRate5 * prevRateModified.ratesCount + rate.rate_5) / (prevRateModified.ratesCount + 1),
                )
                PeopleSingleton.lecturers[lecturerId]!!.rating = newAvgRate
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
                if (nonFetchedIds.isEmpty()) {
                    onSuccess()
                    return@withContext
                }
                try {
                    model.getExtendedLecturersInfo(nonFetchedIds)
                    onSuccess()
                } catch (e: Exception) {
                    e.printStackTrace()
                    onError()
                }
            }
        }
    }

    fun getLecturersExtendedData(lecturerIds: List<String>, onSuccess: () -> Unit = {}, onError: () -> Unit = {}) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val nonFetchedIds: List<String> = lecturerIds.filter { PeopleSingleton.lecturers[it]?.lecturerData == null }
                if (nonFetchedIds.isEmpty()) {
                    onSuccess()
                    return@withContext
                }
                try {
                    model.getExtendedLecturersInfo(nonFetchedIds, false)
                    onSuccess()
                } catch (e: Exception) {
                    e.printStackTrace()
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
                val lecturers: List<SharedDataClasses.Human> = model.queryLecturersSearch(query, start = 0).items // TODO Add multiple pages
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