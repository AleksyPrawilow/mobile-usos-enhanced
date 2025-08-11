package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.HomePageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.UserInfoClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val _visibleStates = MutableStateFlow<List<Boolean>>(emptyList())
    val visibleStates: StateFlow<List<Boolean>> = _visibleStates

    fun initList(size: Int) {
        _visibleStates.value = List(size) { false }
    }

    fun setVisible(index: Int, visible: Boolean) {
        _visibleStates.value = _visibleStates.value.toMutableList().also {
            it[index] = visible
        }
    }

    suspend fun fetchData() {
        withContext(Dispatchers.IO) {
            userInfo = model.fetchUserData()
        }
    }
}