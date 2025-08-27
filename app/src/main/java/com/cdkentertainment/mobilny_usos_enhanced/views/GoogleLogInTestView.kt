package com.cdkentertainment.mobilny_usos_enhanced.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.cdkentertainment.mobilny_usos_enhanced.DatabaseSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.AuthResponse
import com.cdkentertainment.mobilny_usos_enhanced.models.GoogleAuthManager
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen() {
    val context = LocalContext.current
    val authManager = remember {
        GoogleAuthManager(context)
    }
    val coroutineScope = rememberCoroutineScope()
    var loggedIn: Boolean by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (loggedIn) {
            return@LaunchedEffect
        }
        val response = authManager.sessionExists()
        if (response is AuthResponse.Success) {
            loggedIn = true
        } else {
            Log.e("auth", "No session found")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1.primaryColor),
        contentAlignment = Alignment.Center
    ) {
        if (!loggedIn) {
            Button(
                onClick = {
                    authManager.loginGoogleUser()
                        .onEach { result ->
                            if (result is AuthResponse.Success) {
                                Log.d("auth", "Google Success")
                                loggedIn = true
                            } else {
                                Log.e("auth", "Google Failed")
                            }
                        }
                        .launchIn(coroutineScope)
                },
            ) {
                Text(
                    text = "Sign In With Google",
                )
            }
        } else {
            Column{
                Text(
                    "Logged in successfully"
                )
                Button(
                    onClick = {
                        coroutineScope.launch {
                            DatabaseSingleton.client.auth.signOut()
                            loggedIn = false
                        }
                    }
                ) {
                    Text("Logout")
                }
            }
        }
    }
}

@Preview
@Composable
private fun RegisterPreview() {
    RegisterScreen()
}