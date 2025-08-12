package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.view_models.HomePageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import com.cdkentertainment.mobilny_usos_enhanced.view_models.VisibleItemsViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomePageView(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    visibleItemsViewModel: VisibleItemsViewModel = viewModel<VisibleItemsViewModel>(),
    visibleIndex: Int = 1
) {
    val homePageViewModel: HomePageViewModel = viewModel<HomePageViewModel>()
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

    LaunchedEffect(Unit) {
        homePageViewModel.fetchData()
        delay(50)
        visibleItemsViewModel.setVisibleState(visibleIndex, true)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(
            visible = isVisible[visibleIndex],
            enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay), easingForShows), slideTweenSpec(slideDuration, delayBetweenShows, easingForShows)),
            exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay), easingForShows), slideTweenSpec(slideDuration, delayBetweenShows, easingForShows))
        ) {
            Text(
                text = "Cześć, ${homePageViewModel.userInfo?.basicInfo?.first_name}!",
                style = MaterialTheme.typography.headlineLarge,
                color = UISingleton.color4.primaryColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        AnimatedVisibility(
            visible = isVisible[visibleIndex],
            enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 2, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 2, easingForShows)),
            exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 2, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 2, easingForShows))
        ) {
            UserDataView(homePageViewModel)
        }
        AnimatedVisibility(
            visible = isVisible[visibleIndex],
            enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 3, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 3, easingForShows)),
            exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 3, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 3, easingForShows))
        ) {
            Text(
                text = "Plan na dzisiaj:",
                color = UISingleton.color4.primaryColor,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        AnimatedVisibility(
            visible = isVisible[visibleIndex],
            enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 4, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 4, easingForShows)),
            exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 4, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 4, easingForShows))
        ) {
            Text(
                text = "Brak zajęć.",
                color = UISingleton.color4.primaryColor,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    OAuthSingleton.setTestAccessToken()
    val currentScreen: Screens = Screens.HOME
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1.primaryColor)
            .padding(12.dp)
    ) {
        SharedTransitionLayout {
            AnimatedContent(targetState = currentScreen) { target ->
                if (currentScreen == target) {
                    HomePageView(this@SharedTransitionLayout, this@AnimatedContent)
                }
            }
        }
    }
}