package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import com.cdkentertainment.mobilny_usos_enhanced.view_models.SettingsPageViewModel
import kotlinx.coroutines.delay

@Composable
fun SettingsPageView() {
    val settingsPageViewModel: SettingsPageViewModel = viewModel<SettingsPageViewModel>()
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }

    val color1: Color by animateColorAsState(UISingleton.color1)
    val color2: Color by animateColorAsState(UISingleton.color2)
    val color3: Color by animateColorAsState(UISingleton.color3)
    val color4: Color by animateColorAsState(UISingleton.color4)

    val checkLambda: (Boolean) -> Unit

    LaunchedEffect(Unit) {
        delay(150)
        showElements = true
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(0)) {
                Text(
                    text = "Ustawienia",
                    style = MaterialTheme.typography.headlineLarge,
                    color = color4,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(1)) {
                SwitchSettingView(
                    color1, color2, color3, color4,
                    text = "Ciemny motyw",
                    checked = settingsPageViewModel.darkThemeChecked,
                    onSwitchChange = {
                        settingsPageViewModel.setDarkTheme(it)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPagePreview() {
    val color1: Color by animateColorAsState(UISingleton.color1)
    val color2: Color by animateColorAsState(UISingleton.color2)
    val color3: Color by animateColorAsState(UISingleton.color3)
    val color4: Color by animateColorAsState(UISingleton.color4)
    OAuthSingleton.setTestAccessToken()
    val currentScreen: Screens = Screens.HOME
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color1)
            .padding(12.dp)
    ) {
        AnimatedContent(targetState = currentScreen) { target ->
            if (currentScreen == target) {
                SettingsPageView()
            }
        }
    }
}