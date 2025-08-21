package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.ScreenManagerViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import com.cdkentertainment.mobilny_usos_enhanced.view_models.VisibleItemsViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ScreenManager(
    currentScreen: Screens,
    screenManagerViewModel: ScreenManagerViewModel,
    visibleItemsViewModel: VisibleItemsViewModel = viewModel<VisibleItemsViewModel>()
) {
    SharedTransitionLayout() {
        AnimatedContent(
            transitionSpec = { fadeIn() + scaleIn() togetherWith fadeOut() + slideOutHorizontally(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) },
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
                Screens.HOME -> HomePageView(
                    sharedTransitionScope = sharedContentState,
                    animatedVisibilityScope = animatedVisibilityScope,
                    visibleItemsViewModel = visibleItemsViewModel,
                    visibleIndex = 1
                )
                Screens.GRADES -> GradesPageView(
                    sharedTransitionScope = sharedContentState,
                    animatedVisibilityScope = animatedVisibilityScope,
                    visibleItemsViewModel = visibleItemsViewModel,
                    visibleIndex = 2
                )
                Screens.TESTS -> HomePageView(
                    sharedTransitionScope = sharedContentState,
                    animatedVisibilityScope = animatedVisibilityScope,
                    visibleItemsViewModel = visibleItemsViewModel,
                    visibleIndex = 3
                )
                Screens.CALENDAR -> SchedulePageView(
                    sharedTransitionScope = sharedContentState,
                    animatedVisibilityScope = animatedVisibilityScope,
                    visibleItemsViewModel = visibleItemsViewModel,
                    visibleIndex = 4
                )
                Screens.GROUPS -> ClassGroupsPageView(
                    sharedTransitionScope = sharedContentState,
                    animatedVisibilityScope = animatedVisibilityScope,
                    visibleItemsViewModel = visibleItemsViewModel,
                    visibleIndex = 5
                )
                Screens.PAYMENTS -> PaymentsPageView(
                    sharedTransitionScope = sharedContentState,
                    animatedVisibilityScope = animatedVisibilityScope
                )
                Screens.ATTENDANCE -> AttendancePageView(
                    sharedTransitionScope = sharedContentState,
                    animatedVisibilityScope = animatedVisibilityScope,
                    visibleItemsViewModel = visibleItemsViewModel,
                    visibleIndex = 7
                )
                Screens.SETTINGS -> SettingsPageView(
                    sharedTransitionScope = sharedContentState,
                    animatedVisibilityScope = animatedVisibilityScope,
                    visibleItemsViewModel = visibleItemsViewModel,
                    visibleIndex = 8
                )
            }
        }
    }
}