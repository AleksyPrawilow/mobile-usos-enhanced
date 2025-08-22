package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TestsPageViewModel: ViewModel() {
    var tests: Int? by mutableStateOf(null)

    suspend fun fetchTests() {
        withContext(Dispatchers.IO) {
            tests = null
        }
    }
}