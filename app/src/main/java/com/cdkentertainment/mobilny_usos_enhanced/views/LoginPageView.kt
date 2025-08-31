package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import android.graphics.Color.TRANSPARENT
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.DatabaseSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.findActivity
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LoginPageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.ScreenManagerViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginPageView(screenManagerViewModel: ScreenManagerViewModel = viewModel<ScreenManagerViewModel>()) {
    val coroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    val pageViewModel: LoginPageViewModel = viewModel<LoginPageViewModel>()
    val currentState: LoginPageViewModel.LoginState = pageViewModel.loginState
    val activity = remember { context.findActivity() }

    LaunchedEffect(Unit) {
        pageViewModel.tryGoogleAutoLogIn(context, activity!!)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = "Mobilny USOS Enhanced",
            style = MaterialTheme.typography.headlineLarge,
            color = UISingleton.color4.primaryColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        AnimatedContent(
            transitionSpec = { fadeIn() + scaleIn() togetherWith fadeOut() + slideOutHorizontally() },
            targetState = currentState
        ) { state ->
            when (state) {
                LoginPageViewModel.LoginState.GOOGLE_AUTO_LOGIN -> GoogleAutoLoginView()
                LoginPageViewModel.LoginState.GOOGLE_LOGIN -> GoogleLoginView { idToken ->
                    coroutineScope.launch {
                        try {
                            DatabaseSingleton.client.auth.signInWith(IDToken) {
                                this.idToken = idToken
                                provider = Google
                            }
                            // success
                        } catch (e: Exception) {
                            // handle
                        }
                    }
                    //pageViewModel.loginGoogle(activity!!)
                }
                LoginPageViewModel.LoginState.USOS_AUTO_LOGIN -> UsosAutoLoginView(pageViewModel)
                LoginPageViewModel.LoginState.USOS_LOGIN -> UsosLoginView {
                    coroutineScope.launch {
                        pageViewModel.authorize(context)
                    }
                }
                LoginPageViewModel.LoginState.USOS_RETREIVING_REQUEST_TOKEN -> UsosRequestTokenView()
                LoginPageViewModel.LoginState.USOS_OAUTH_VERIFIER -> UsosOauthVerifierView(pageViewModel)
                LoginPageViewModel.LoginState.DATABASE_SAVING_SESSION -> DatabaseSavingSessionView(pageViewModel)
                LoginPageViewModel.LoginState.SUCCESS -> SuccessView(screenManagerViewModel)
            }
        }
    }
}

@Composable
private fun GoogleAutoLoginView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Sprawdzam połączenie z serwisami Google...",
            textAlign = TextAlign.Center,
            color = UISingleton.color4.primaryColor,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        CircularProgressIndicator(color = UISingleton.color3.primaryColor)
    }
}

@Composable
private fun GoogleLoginView(onIdToken: (String) -> Unit) {
    val context = LocalContext.current
    val activity = context.findActivity() // use the findActivity() extension you already have

    // launcher
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java) // safe in callback
            val idToken = account?.idToken
            if (!idToken.isNullOrEmpty()) {
                onIdToken(idToken)
            } else {

            }
        } catch (e: ApiException) {

        } catch (e: Exception) {

        }
    }

    Button(onClick = {
        if (activity == null) {
            return@Button
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("96510249553-u074a5donhaf16700lr2bgjnapbkul13.apps.googleusercontent.com") // <-- MUST be web client id
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(activity, gso)
        val intent = client.signInIntent
        launcher.launch(intent)
    }) {
        Text("Sign in with Google")
    }
//    Button(
//        colors = ButtonDefaults.buttonColors(
//            contentColor = UISingleton.color3.primaryColor,
//            containerColor = UISingleton.color2.primaryColor,
//        ),
//        onClick = onClick
//    ) {
//        Text(
//            text = "Zaloguj się za pomocą Google"
//        )
//    }
}

@Composable
private fun UsosAutoLoginView(viewModel: LoginPageViewModel) {
    var tryingToLogin: Boolean by rememberSaveable { mutableStateOf(false) }
    val context: Context = LocalContext.current
    LaunchedEffect(Unit) {
        if (tryingToLogin) {
            return@LaunchedEffect
        }
        tryingToLogin = true
        viewModel.tryAutoLogin(context)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Sprawdzam połączenie z serwisami USOS...",
            textAlign = TextAlign.Center,
            color = UISingleton.color4.primaryColor,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        CircularProgressIndicator(color = UISingleton.color3.primaryColor)
    }
}

@Composable
private fun UsosLoginView(onClick: () -> Unit) {
    Button(
        colors = ButtonDefaults.buttonColors(
            contentColor = UISingleton.color3.primaryColor,
            containerColor = UISingleton.color2.primaryColor,
        ),
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Zaloguj się",
                style = MaterialTheme.typography.headlineSmall
            )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = "Icon",
            )
        }
    }
}

@Composable
private fun UsosRequestTokenView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Sprawdzam połączenie z serwisami USOS...",
            textAlign = TextAlign.Center,
            color = UISingleton.color4.primaryColor,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        CircularProgressIndicator(color = UISingleton.color3.primaryColor)
    }
}

@Composable
private fun UsosOauthVerifierView(viewModel: LoginPageViewModel) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    var oauthPin: String by rememberSaveable { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = viewModel.errorMessage
        )
        Text(
            text = "Wprowadź PIN podany na stronie",
            color = UISingleton.color4.primaryColor,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            TextField(
                colors = TextFieldDefaults.colors(
                    focusedTextColor = UISingleton.color3.primaryColor,
                    unfocusedTextColor = UISingleton.color3.primaryColor,
                    focusedContainerColor = Color(TRANSPARENT),
                    unfocusedContainerColor = Color(TRANSPARENT),
                    cursorColor = UISingleton.color3.primaryColor,
                    focusedIndicatorColor = UISingleton.color2.primaryColor,
                    unfocusedIndicatorColor = UISingleton.color2.primaryColor
                ),
                value = oauthPin,
                leadingIcon = { Icon(imageVector = Icons.Rounded.Lock, contentDescription = "lockIcon", tint = UISingleton.color3.primaryColor) },
                placeholder = { Text(text = "Wprowadź pin.") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    if (it.length <= 8 && it.all { char -> char.isDigit() }) {
                        oauthPin = it
                    }
                }
            )
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = UISingleton.color3.primaryColor,
                    containerColor = UISingleton.color2.primaryColor,
                ),
                onClick = {
                    if (oauthPin.length == 8) {
                        coroutineScope.launch {
                            viewModel.getAccessToken(oauthPin, context)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = "Authorize"
                )
            }
        }
    }
}

@Composable
private fun DatabaseSavingSessionView(viewModel: LoginPageViewModel) {
    var tryingToSave: Boolean by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (tryingToSave) {
            return@LaunchedEffect
        }
        tryingToSave = true
        viewModel.saveUserSession()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Jeszcze chwilka...",
            color = UISingleton.color4.primaryColor,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        CircularProgressIndicator(color = UISingleton.color3.primaryColor)
    }
}

@Composable
private fun SuccessView(screenManagerViewModel: ScreenManagerViewModel) {
    LaunchedEffect(Unit) {
        delay(1000)
        screenManagerViewModel.authorize()
    }
    Text(
        text = "Zalogowany",
        style = MaterialTheme.typography.titleLarge,
        color = UISingleton.color3.primaryColor
    )
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    val currentScreen: Screens = Screens.LOGIN
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1.primaryColor)
            .padding(12.dp)
    ) {
        AnimatedContent(targetState = currentScreen) { target ->
            if (currentScreen == target) {
                LoginPageView()
            }
        }
    }
}