package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import com.cdkentertainment.mobilny_usos_enhanced.view_models.SettingsPageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.VisibleItemsViewModel
import kotlinx.coroutines.delay

@Composable
fun SettingsPageView(
    visibleItemsViewModel: VisibleItemsViewModel = viewModel<VisibleItemsViewModel>(),
    visibleIndex: Int = 8
) {
    val settingsPageViewModel: SettingsPageViewModel = viewModel<SettingsPageViewModel>()
    val isVisible: List<Boolean> by visibleItemsViewModel.visibleStates.collectAsState()
    val delayBetweenShows: Int = 150
    val fadeDelay: Int = 50
    val fadeDuration: Int = 500
    val slideDuration: Int = 750
    val easingForShows: Easing = EaseInOutBack
    val fadeTweenSpec: (Int, Int, Easing) -> TweenSpec<Float> = { duration, delay, easing -> TweenSpec<Float>(durationMillis = duration, delay = delay, easing = easing) }
    val slideTweenSpec: (Int, Int, Easing) -> TweenSpec<IntOffset> = { duration, delay, easing -> TweenSpec<IntOffset>(durationMillis = duration, delay = delay, easing = easing) }
    val enterTrans: (TweenSpec<Float>, TweenSpec<IntOffset>) -> EnterTransition = { fadeSpec, slideSpec -> fadeIn(fadeSpec) + slideInHorizontally(slideSpec) }
    val exitTrans: (TweenSpec<Float>, TweenSpec<IntOffset>) -> ExitTransition = { fadeSpec, slideSpec -> fadeOut(fadeSpec) + slideOutHorizontally(slideSpec) }

    val color1: Color by animateColorAsState(UISingleton.color1.primaryColor)
    val color2: Color by animateColorAsState(UISingleton.color2.primaryColor)
    val color3: Color by animateColorAsState(UISingleton.color3.primaryColor)
    val color4: Color by animateColorAsState(UISingleton.color4.primaryColor)

    val checkLambda: (Boolean) -> Unit

    LaunchedEffect(Unit) {
        delay(50)
        visibleItemsViewModel.setVisibleState(visibleIndex, true)
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
            AnimatedVisibility(
                visible = isVisible[visibleIndex],
                enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay), easingForShows), slideTweenSpec(slideDuration, delayBetweenShows, easingForShows)),
                exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay), easingForShows), slideTweenSpec(slideDuration, delayBetweenShows, easingForShows))
            ) {
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
            AnimatedVisibility(
                visible = isVisible[visibleIndex],
                enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 2, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 2, easingForShows)),
                exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 2, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 2, easingForShows))
            ) {
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
//        AnimatedVisibility(
//            visible = isVisible[visibleIndex],
//            enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 3, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 3, easingForShows)),
//            exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 3, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 3, easingForShows))
//        ) {
//        }
//        AnimatedVisibility(
//            visible = isVisible[visibleIndex],
//            enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 4, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 4, easingForShows)),
//            exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 4, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 4, easingForShows))
//        ) {
//        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPagePreview() {
    val color1: Color by animateColorAsState(UISingleton.color1.primaryColor)
    val color2: Color by animateColorAsState(UISingleton.color2.primaryColor)
    val color3: Color by animateColorAsState(UISingleton.color3.primaryColor)
    val color4: Color by animateColorAsState(UISingleton.color4.primaryColor)
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