package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.SubjectTestContainer
import com.cdkentertainment.mobilny_usos_enhanced.models.TestsContainer
import com.cdkentertainment.mobilny_usos_enhanced.models.TestsPageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.prettyPrint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Stack

fun main(): Unit = runBlocking { //dla testów
    OAuthSingleton.setTestAccessToken()
    val model = TestsPageModel()
    launch {
        val test = model.getAllTests()
        val testData = model.getSingleTestInfo(94530)
        println(testData.prettyPrint())
    }
}

class TestsPageViewModel: ViewModel() {
    var tests: TestsContainer? by mutableStateOf(null)
    var testDetails: MutableMap<Int, SubjectTestContainer> = mutableStateMapOf()
    var testsSelectedFolder: MutableMap<Int, SubjectTestContainer> = mutableStateMapOf()
    var testsPrevFolders: MutableMap<Int, Stack<SubjectTestContainer>> = mutableMapOf()
    val model = TestsPageModel()

    suspend fun fetchTests() {
        withContext(Dispatchers.IO) {
            if (tests != null) {
                return@withContext
            }
            try {
                tests = model.getAllTests()
            } catch(e: Exception) {
                println(e)
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
                return@withContext true
            } catch(e: Exception) {
                println(e)
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