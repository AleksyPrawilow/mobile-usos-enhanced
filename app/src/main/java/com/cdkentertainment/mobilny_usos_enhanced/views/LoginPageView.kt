package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.DatabaseSingleton
import com.cdkentertainment.mobilny_usos_enhanced.R
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
    var showLoginStuff: Boolean by rememberSaveable { mutableStateOf(false) }
    val loginWeight: Float by animateFloatAsState(
        targetValue = if (showLoginStuff) 0.3f else 0.01f,
        spring(stiffness = Spring.StiffnessLow)
    )

    LaunchedEffect(Unit) {
        delay(3000)
        showLoginStuff = true
        pageViewModel.tryGoogleAutoLogIn(context, activity!!)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        SplashScreenView(
            modifier = Modifier.weight(1f)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(loginWeight)
                .padding(horizontal = UISingleton.horizontalPadding, vertical = UISingleton.verticalPadding)
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = showLoginStuff,
                enter = expandVertically(spring(Spring.DampingRatioMediumBouncy), expandFrom = Alignment.Bottom),
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimatedContent(
                    transitionSpec = { fadeIn() + slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) togetherWith fadeOut() + slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) },
                    targetState = currentState,
                    modifier = Modifier.fillMaxWidth()
                ) { state ->
                    when (state) {
                        LoginPageViewModel.LoginState.GOOGLE_AUTO_LOGIN -> GoogleAutoLoginView()
                        LoginPageViewModel.LoginState.GOOGLE_LOGIN -> GoogleLoginView { idToken ->
                            coroutineScope.launch {
                                pageViewModel.loginState = LoginPageViewModel.LoginState.GOOGLE_AUTO_LOGIN
                                try {
                                    DatabaseSingleton.client.auth.signInWith(IDToken) {
                                        this.idToken = idToken
                                        provider = Google
                                    }
                                    pageViewModel.loginState = LoginPageViewModel.LoginState.USOS_AUTO_LOGIN
                                } catch (e: Exception) {
                                    pageViewModel.loginState = LoginPageViewModel.LoginState.GOOGLE_LOGIN
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
                        LoginPageViewModel.LoginState.USOS_RETREIVING_OAUTH_VERIFIER -> {}
                        LoginPageViewModel.LoginState.USOS_OAUTH_VERIFIER -> UsosOauthVerifierView(pageViewModel)
                        LoginPageViewModel.LoginState.DATABASE_SAVING_SESSION -> DatabaseSavingSessionView(pageViewModel)
                        LoginPageViewModel.LoginState.SUCCESS -> SuccessView(screenManagerViewModel)
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            pageViewModel.loginState == LoginPageViewModel.LoginState.USOS_RETREIVING_OAUTH_VERIFIER,
            enter = slideInHorizontally(spring(Spring.DampingRatioMediumBouncy)) + fadeIn(),
            exit = slideOutHorizontally(spring(Spring.DampingRatioMediumBouncy)) + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            UsosLoginView(pageViewModel.oauthUrl)
        }
    }
}

@Composable
private fun GoogleAutoLoginView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Sprawdzam połączenie z serwisami Google...",
            textAlign = TextAlign.Center,
            color = UISingleton.textColor1,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        CircularProgressIndicator(color = UISingleton.textColor2)
    }
}

@Composable
private fun UsosLoginView(authUrl: String) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val viewModel: LoginPageViewModel = viewModel<LoginPageViewModel>()
    val authUrl = authUrl
    val context: Context = LocalContext.current

    OAuthWebView(url = authUrl) { uri ->
        val oauthToken = uri.getQueryParameter("oauth_token")
        val oauthVerifier = uri.getQueryParameter("oauth_verifier")
        coroutineScope.launch {
            viewModel.onOAuthRedirect(oauthToken, oauthVerifier, context)
        }
    }
}

@Composable
private fun GoogleLoginView(onIdToken: (String) -> Unit) {
    val context = LocalContext.current
    val activity = context.findActivity()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (!idToken.isNullOrEmpty()) {
                onIdToken(idToken)
            } else {

            }
        } catch (e: ApiException) {

        } catch (e: Exception) {

        }
    }

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = UISingleton.color2,
            contentColor = UISingleton.textColor1
        ),
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
        onClick = {
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
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(6.dp)
        ) {
            Text(
                text = "Zaloguj przez Google",
                style = MaterialTheme.typography.titleMedium,
                color = UISingleton.textColor1,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_google),
                contentDescription = null,
                tint = UISingleton.textColor4,
                modifier = Modifier
                    .size(48.dp)
                    .background(UISingleton.color3, CircleShape)
                    .padding(8.dp)
            )
        }
    }
//    Button(
//        colors = ButtonDefaults.buttonColors(
//            contentColor = UISingleton.color3,
//            containerColor = UISingleton.color2,
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
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Sprawdzam połączenie z serwisami USOS...",
            textAlign = TextAlign.Center,
            color = UISingleton.textColor1,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        CircularProgressIndicator(color = UISingleton.textColor2)
    }
}

@Composable
private fun UsosLoginView(onClick: () -> Unit) {
    Button(
        colors = ButtonDefaults.buttonColors(
            contentColor = UISingleton.textColor1,
            containerColor = UISingleton.color2,
        ),
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(6.dp)
        ) {
            Text(
                text = "Zaloguj się do konta USOS",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                tint = UISingleton.textColor4,
                modifier = Modifier
                    .size(48.dp)
                    .background(UISingleton.color3, CircleShape)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun UsosRequestTokenView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Sprawdzam połączenie z serwisami USOS...",
            textAlign = TextAlign.Center,
            color = UISingleton.textColor1,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        CircularProgressIndicator(color = UISingleton.textColor2)
    }
}

@Composable
private fun UsosOauthVerifierView(viewModel: LoginPageViewModel) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    var oauthPin: String by rememberSaveable { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Wprowadź PIN podany na stronie",
            color = UISingleton.textColor1,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(UISingleton.color2, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .padding(12.dp)
        ) {
            TextField(
                colors = TextFieldDefaults.colors(
                    focusedTextColor = UISingleton.textColor1,
                    unfocusedTextColor = UISingleton.textColor1,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = UISingleton.textColor1,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
                textStyle = MaterialTheme.typography.titleMedium,
                value = oauthPin,
                leadingIcon = { Icon(imageVector = Icons.Rounded.Lock, contentDescription = "lockIcon", tint = UISingleton.textColor2) },
                placeholder = {
                    Text(
                        text = "Wprowadź PIN",
                        style = MaterialTheme.typography.titleMedium,
                        color = UISingleton.textColor2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                onValueChange = {
                    if (it.length <= 8 && it.all { char -> char.isDigit() }) {
                        oauthPin = it
                    }
                }
            )
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = UISingleton.textColor1,
                    containerColor = UISingleton.color2,
                ),
                onClick = {
                    if (oauthPin.length == 8) {
                        coroutineScope.launch {
                            viewModel.getAccessToken(oauthPin, context)
                        }
                    }
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = "Authorize",
                    tint = UISingleton.textColor4,
                    modifier = Modifier
                        .size(48.dp)
                        .background(UISingleton.textColor2, CircleShape)
                        .padding(8.dp)
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
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Jeszcze chwilka...",
            color = UISingleton.textColor1,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        CircularProgressIndicator(color = UISingleton.textColor2)
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
        color = UISingleton.textColor1,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    val currentScreen: Screens = Screens.LOGIN
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1)
            .padding(12.dp)
    ) {
        AnimatedContent(targetState = currentScreen) { target ->
            if (currentScreen == target) {
                LoginPageView()
            }
        }
    }
}