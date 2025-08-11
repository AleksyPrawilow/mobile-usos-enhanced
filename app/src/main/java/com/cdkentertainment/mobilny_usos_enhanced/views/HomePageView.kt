package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.view_models.HomePageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomePageView(sharedTransitionScope: SharedTransitionScope, animatedVisibilityScope: AnimatedVisibilityScope) {
    val homePageViewModel: HomePageViewModel = viewModel<HomePageViewModel>()
    val visibleStates by homePageViewModel.visibleStates.collectAsState()

    val itemsToAnimate: Int = 4
    val enterTransition: EnterTransition = fadeIn() + slideInHorizontally()
    val exitTransition: ExitTransition = fadeOut() + slideOutHorizontally()

    LaunchedEffect(Unit) {
        homePageViewModel.initList(itemsToAnimate)
        homePageViewModel.fetchData()
        for (index in 0 until itemsToAnimate) {
            homePageViewModel.setVisible(index, true)
            delay(150)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AnimatedVisibility(
            visible = visibleStates.getOrNull(0) == true,
            enter = enterTransition,
            exit = exitTransition
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
            visible = visibleStates.getOrNull(1) == true,
            enter = enterTransition,
            exit = exitTransition
        ) {
            UserDataView(homePageViewModel)
        }
        AnimatedVisibility(
            visible = visibleStates.getOrNull(2) == true,
            enter = enterTransition,
            exit = exitTransition
        ) {
            Text(
                text = "Plan na dzisiaj:",
                color = UISingleton.color4.primaryColor,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        AnimatedVisibility(
            visible = visibleStates.getOrNull(3) == true,
            enter = enterTransition,
            exit = exitTransition
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