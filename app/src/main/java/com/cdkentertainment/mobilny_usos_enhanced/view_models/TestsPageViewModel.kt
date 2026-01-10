package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.models.SubjectTestContainer
import com.cdkentertainment.mobilny_usos_enhanced.models.TestsContainer
import com.cdkentertainment.mobilny_usos_enhanced.models.TestsPageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Stack

class TestsPageViewModel: ViewModel() {
    var loading: Boolean by mutableStateOf(false)
    var loaded: Boolean by mutableStateOf(false)
    var error: Boolean by mutableStateOf(false)
    var tests: TestsContainer? by mutableStateOf(null)
    var testDetails: MutableMap<Int, SubjectTestContainer> = mutableStateMapOf()
    var testsSelectedFolder: MutableMap<Int, SubjectTestContainer> = mutableStateMapOf()
    var testsPrevFolders: MutableMap<Int, Stack<SubjectTestContainer>> = mutableMapOf()
    val model = TestsPageModel()

    suspend fun fetchTests() {
        if (tests != null || loaded) {
            return
        }
        withContext(Dispatchers.IO) {
            if (loading) {
                return@withContext
            }
            try {
                tests = model.getAllTests()
                loaded = true
                loading = false
                error = false
            } catch(e: Exception) {
                loaded = false
                loading = false
                error = true
                return@withContext
            }
        }
    }

    suspend fun fetchTestDetails(testId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            if (testDetails[testId] != null) {
                return@withContext true
            }
            try {
                val details = model.getSingleTestInfo(testId)
                testDetails[testId] = details
                testsSelectedFolder[testId] = details
                return@withContext false
            } catch(e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }
    }

    fun changeCurrentFolder(testId: Int, folder: SubjectTestContainer) {
        testsPrevFolders.getOrPut(testId) { Stack() }.push(testsSelectedFolder[testId])
        testsSelectedFolder[testId] = folder
    }

    fun retractToPreviousFolder(testId: Int): Boolean {
        testsSelectedFolder[testId] = testsPrevFolders[testId]?.pop() ?: testsSelectedFolder[testId]!!
        return testsPrevFolders[testId]?.isEmpty() ?: true
    }

    fun clearStack(testId: Int) {
        testsPrevFolders[testId]?.clear()
    }
}