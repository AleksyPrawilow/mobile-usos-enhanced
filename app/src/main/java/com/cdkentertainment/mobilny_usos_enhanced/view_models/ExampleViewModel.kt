package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.ExampleModel
import com.cdkentertainment.mobilny_usos_enhanced.models.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

// = runBlocking is needed if the coroutines are present in the main
fun main(): Unit = runBlocking {
    // This sets a test access token so that calls to the API can be made. The credentials can be changed in the "secrets.properties" file
    OAuthSingleton.setTestAccessToken()
    val exampleViewModel: ExampleViewModel = ExampleViewModel()
    // Launches a coroutine, so that println prints after the data is fetched by the "suspend" function
    launch {
        exampleViewModel.fetchUserData()
        println(exampleViewModel.userData)
    }
}

// The class must be a ViewModel so that it can be used in the UI
class ExampleViewModel: ViewModel() {
    /*
     val for constants and var for everything else
     vars defined with "by mutableStateOf" cause the UI to recompose(refresh) on change
     */
    var userData: UserData? by mutableStateOf(null)
    // All data fetches should be made through the model as per mvvm architectural pattern, that way our app is more modular
    val exampleModel: ExampleModel = ExampleModel()

    // Async functions are defined by the "suspend" keyword
    suspend fun fetchUserData() {
        withContext(Dispatchers.IO) {
            userData = exampleModel.fetchUserData()
        }
    }
}