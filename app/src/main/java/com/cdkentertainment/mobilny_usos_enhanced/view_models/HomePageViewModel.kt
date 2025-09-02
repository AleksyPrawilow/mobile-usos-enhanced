package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.HomePageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.UserInfoClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main(): Unit = runBlocking {
    OAuthSingleton.setTestAccessToken()
    val boom = HomePageViewModel()
    launch {
        boom.fetchData()
        println(boom.userInfo)
    }
}

class HomePageViewModel: ViewModel() {
    var userInfo: UserInfoClass? by mutableStateOf(null)
    val model: HomePageModel = HomePageModel()

    suspend fun fetchData() {
        withContext(Dispatchers.IO) {
            userInfo = OAuthSingleton.userData
        }
    }
}