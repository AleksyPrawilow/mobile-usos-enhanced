package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import com.cdkentertainment.mobilny_usos_enhanced.view_models.ScreenManagerViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ScreenManager(currentScreen: Screens, screenManagerViewModel: ScreenManagerViewModel) {
    SharedTransitionLayout() {
        AnimatedContent(
            transitionSpec = { fadeIn() + scaleIn() togetherWith fadeOut() },
            targetState = currentScreen
        ) { target ->
            val animatedVisibilityScope: AnimatedVisibilityScope = this@AnimatedContent
            val sharedContentState: SharedTransitionScope = this@SharedTransitionLayout
            when (target) {
                Screens.LOGIN -> LoginPageView(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedContent,
                    screenManagerViewModel = screenManagerViewModel
                )
                Screens.HOME -> HomePageView(sharedContentState, animatedVisibilityScope)
                Screens.GRADES -> HomePageView(sharedContentState, animatedVisibilityScope)
                Screens.TESTS -> HomePageView(sharedContentState, animatedVisibilityScope)
                Screens.CALENDAR -> HomePageView(sharedContentState, animatedVisibilityScope)
                Screens.GROUPS -> HomePageView(sharedContentState, animatedVisibilityScope)
                Screens.PAYMENTS -> HomePageView(sharedContentState, animatedVisibilityScope)
                Screens.ATTENDANCE -> HomePageView(sharedContentState, animatedVisibilityScope)
                Screens.SETTINGS -> HomePageView(sharedContentState, animatedVisibilityScope)
            }
        }
    }
}