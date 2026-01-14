package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.UserDataSingleton
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LecturerRatesPageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LoginPageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.ScreenManagerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginPageView(screenManagerViewModel: ScreenManagerViewModel = viewModel<ScreenManagerViewModel>()) {
    val coroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    val pageViewModel: LoginPageViewModel = viewModel<LoginPageViewModel>()
    val currentState: LoginPageViewModel.LoginState = pageViewModel.loginState
    var showLoginStuff: Boolean by rememberSaveable { mutableStateOf(false) }
    val loginWeight: Float by animateFloatAsState(
        targetValue = if (showLoginStuff) 0.3f else 0.01f,
        spring(stiffness = Spring.StiffnessLow)
    )

    LaunchedEffect(pageViewModel.errorMessage) {
        if (pageViewModel.errorMessage.isNotEmpty()) {
            Toast.makeText(context, pageViewModel.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(pageViewModel.loginState) {
        if (pageViewModel.loginState == LoginPageViewModel.LoginState.USOS_LOGIN && !showLoginStuff) {
            delay(500)
            showLoginStuff = true
        }
    }

    LaunchedEffect(Unit) {
        pageViewModel.readTokenAndUniversity(context)
        println(UserDataSingleton.selectedUniversity)
        if (UserDataSingleton.selectedUniversity != 0) {
            pageViewModel.tryAutoLogin(context)
            delay(3000)
            showLoginStuff = true
        }
    }

    BackHandler(
        enabled = pageViewModel.loginState == LoginPageViewModel.LoginState.USOS_LOGIN
    ) {
        showLoginStuff = false
        UserDataSingleton.selectedUniversity = 0
        pageViewModel.loginState = LoginPageViewModel.LoginState.SELECT_UNIVERSITY
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        SplashScreenView(
            modifier = Modifier.weight(1f)
        )
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .weight(loginWeight)
                .padding(
                    horizontal = UISingleton.horizontalPadding,
                    vertical = UISingleton.verticalPadding
                )
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = showLoginStuff,
                enter = expandVertically(spring(Spring.DampingRatioMediumBouncy), expandFrom = Alignment.Bottom),
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimatedContent(
                    transitionSpec = { fadeIn() + slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) togetherWith fadeOut() + slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) },
                    targetState = currentState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) { state ->
                    when (state) {
                        LoginPageViewModel.LoginState.SELECT_UNIVERSITY              -> {}
                        LoginPageViewModel.LoginState.USOS_AUTO_LOGIN                -> UsosAutoLoginView()
                        LoginPageViewModel.LoginState.USOS_LOGIN                     -> UsosLoginView(coroutineScope)
                        LoginPageViewModel.LoginState.USOS_RETREIVING_REQUEST_TOKEN  -> UsosRequestTokenView()
                        LoginPageViewModel.LoginState.USOS_RETREIVING_OAUTH_VERIFIER -> {}
                        LoginPageViewModel.LoginState.LAST_STEPS                     -> LastStepsView(coroutineScope)
                        LoginPageViewModel.LoginState.SUCCESS                        -> SuccessView()
                    }
                }
            }
        }
    }

    AnimatedVisibility(
        visible = pageViewModel.loginState == LoginPageViewModel.LoginState.SELECT_UNIVERSITY,
        enter = fadeIn(tween(1000)),
        exit = fadeOut(),
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        SelectUniversityView()
    }

    AnimatedVisibility(
        visible = pageViewModel.loginState == LoginPageViewModel.LoginState.USOS_RETREIVING_OAUTH_VERIFIER,
        enter = slideInHorizontally() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut(),
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(UISingleton.color3)
        ) {
            TextButton(
                onClick = {
                    pageViewModel.loginCancelled(context)
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = UISingleton.textColor4
                )
                Text(
                    text = stringResource(R.string.go_back),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = UISingleton.textColor4
                )
            }
            UsosOauthLoginView(
                authUrl = pageViewModel.oauthUrl,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun UsosOauthLoginView(authUrl: String, modifier: Modifier = Modifier) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val viewModel: LoginPageViewModel = viewModel<LoginPageViewModel>()
    val authUrl = authUrl
    val context: Context = LocalContext.current

    OAuthWebView(url = authUrl, modifier) { uri ->
        val oauthToken = uri.getQueryParameter("oauth_token")
        val oauthVerifier = uri.getQueryParameter("oauth_verifier")
        coroutineScope.launch {
            viewModel.onOAuthRedirect(oauthToken, oauthVerifier, context)
        }
    }
}

@Composable
private fun SelectUniversityView() {
    data class University(
        val id: Int,
        val name: SharedDataClasses.LangDict
    )

    val viewModel: LoginPageViewModel = viewModel<LoginPageViewModel>()
    var showContent: Boolean by rememberSaveable { mutableStateOf(false) }
    val context: Context = LocalContext.current
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    val universities: List<University> = listOf(
        University(1, SharedDataClasses.LangDict(
            pl = "Uniwersytet im. Adama Mickiewicza w Poznaniu",
            en = "Adam Mickiewicz University in Poznań"
        )),
        University(2, SharedDataClasses.LangDict(
            pl = "Politechnika Poznańska",
            en = "Poznań University of Technology"
        ))
    )

    val onUniversitySelect: (Int) -> Unit = { universityId ->
        viewModel.loginState = LoginPageViewModel.LoginState.USOS_LOGIN
        UserDataSingleton.selectedUniversity = universityId
    }

    LaunchedEffect(Unit) {
        showContent = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(UISingleton.color2.copy(alpha = 0.85f), RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(R.string.choose_your_university),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = UISingleton.textColor1
            )
            for (university in universities) {
                AnimatedVisibility(
                    visible = showContent,
                    enter = enterTransition(university.id)
                ) {
                    GradeCardView(
                        modifier = Modifier.fillMaxWidth(),
                        courseName = university.name.getLocalized(context),
                        showGrade = false,
                        showArrow = true,
                        sideIcon = ImageVector.vectorResource(R.drawable.rounded_school_24),
                        onClick = {
                            onUniversitySelect(university.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun UsosAutoLoginView() {
    val viewModel: LoginPageViewModel = viewModel<LoginPageViewModel>()
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
            text = stringResource(R.string.checking_the_connection),
            textAlign = TextAlign.Center,
            color = UISingleton.textColor1,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        CircularProgressIndicator(color = UISingleton.textColor2)
    }
}

@Composable
private fun UsosLoginView(coroutineScope: CoroutineScope) {
    val context: Context = LocalContext.current
    val viewModel: LoginPageViewModel = viewModel<LoginPageViewModel>()
    TextAndIconCardView(
        title = stringResource(R.string.sign_in),
        icon = ImageVector.vectorResource(R.drawable.rounded_login_24),
        elevation = 3.dp,
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    ) {
        coroutineScope.launch {
            viewModel.authorize(context)
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
            text = stringResource(R.string.checking_the_connection),
            textAlign = TextAlign.Center,
            color = UISingleton.textColor1,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        CircularProgressIndicator(color = UISingleton.textColor2)
    }
}

@Composable
private fun LastStepsView(coroutineScope: CoroutineScope) {
    val context: Context = LocalContext.current
    val viewModel: LoginPageViewModel = viewModel<LoginPageViewModel>()
    val lecturersViewModel: LecturerRatesPageViewModel = viewModel<LecturerRatesPageViewModel>()
    var tryingToSave: Boolean by rememberSaveable { mutableStateOf(false) }
    val lastSteps: () -> Unit = {
        coroutineScope.launch {
            if (!tryingToSave) {
                tryingToSave = true
                lecturersViewModel.fetchUserRates(
                    onSuccess = {
                        coroutineScope.launch {
                            viewModel.lastSteps(context)
                        }
                    },
                    onFailed = {
                        val message: SharedDataClasses.LangDict = SharedDataClasses.LangDict(
                            pl = "Nie udało się pobrać danych z serwera.",
                            en = "Failed to fetch data from server."
                        )
                        viewModel.loginState = LoginPageViewModel.LoginState.USOS_LOGIN
                        viewModel.errorMessage = message.getLocalized(context)
                    }
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        lastSteps()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.almost_there),
            color = UISingleton.textColor1,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        CircularProgressIndicator(color = UISingleton.textColor2)
    }
}

@Composable
private fun SuccessView() {
    val screenManagerViewModel: ScreenManagerViewModel = viewModel<ScreenManagerViewModel>()
    LaunchedEffect(Unit) {
        delay(1000)
        screenManagerViewModel.authorize()
    }
    Text(
        text = stringResource(R.string.logged_in),
        style = MaterialTheme.typography.titleLarge,
        color = UISingleton.textColor1,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}