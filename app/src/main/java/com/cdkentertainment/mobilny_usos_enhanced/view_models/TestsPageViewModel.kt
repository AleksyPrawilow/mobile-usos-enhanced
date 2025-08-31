package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.TestsContainer
import com.cdkentertainment.mobilny_usos_enhanced.models.TestsPageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main(): Unit = runBlocking { //dla testów
    OAuthSingleton.setTestAccessToken()
    val model = TestsPageModel()
    launch {
        val test = model.getAllTests()
        println(model.getAllTests())
        println(model.getSingleTestInfo(94530))
    }
}

class TestsPageViewModel: ViewModel() {
    var tests: TestsContainer? by mutableStateOf(null)
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
}