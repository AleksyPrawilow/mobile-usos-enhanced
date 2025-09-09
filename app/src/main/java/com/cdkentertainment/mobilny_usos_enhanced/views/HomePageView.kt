package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import kotlinx.coroutines.delay

@Composable
fun HomePageView() {
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }
    val paddingModifier: Modifier = Modifier.padding(horizontal = UISingleton.horizontalPadding)
    val density: Density = LocalDensity.current
    val insets = WindowInsets.systemBars
    val topInset = insets.getTop(density)
    val bottomInset = insets.getBottom(density)
    val topPadding = with(LocalDensity.current) { topInset.toDp() }
    val bottomPadding = with(LocalDensity.current) { bottomInset.toDp() }

    LaunchedEffect(Unit) {
        delay(150)
        showElements = true
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding, bottom = bottomPadding)
            .verticalScroll(rememberScrollState())
    ) {
        PageHeaderView("Cześć, ${OAuthSingleton.userData?.basicInfo?.first_name}!")
        AnimatedVisibility(showElements, enter = enterTransition(1), modifier = paddingModifier) {
            UserDataView()
        }
        AnimatedVisibility(showElements, enter = enterTransition(2), modifier = paddingModifier) {
            Text(
                text = "Plan na dzisiaj:",
                color = UISingleton.textColor1,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        AnimatedVisibility(showElements, enter = enterTransition(3), modifier = paddingModifier) {
            Text(
                text = "Brak zajęć.",
                color = UISingleton.textColor1,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    OAuthSingleton.setTestAccessToken()
    val currentScreen: Screens = Screens.HOME
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1)
            .padding(12.dp)
    ) {
        AnimatedContent(targetState = currentScreen) { target ->
            if (currentScreen == target) {
                HomePageView()
            }
        }
    }
}