package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedContent
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

@Composable
fun ScreenManager(
    currentScreen: Screens,
    screenManagerViewModel: ScreenManagerViewModel,
    visibleItemsViewModel: VisibleItemsViewModel = viewModel<VisibleItemsViewModel>()
) {
    AnimatedContent(
        transitionSpec = { fadeIn() + scaleIn() togetherWith fadeOut() + slideOutHorizontally(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) },
        targetState = currentScreen
    ) { target ->
        when (target) {
            Screens.LOGIN -> LoginPageView(
                screenManagerViewModel = screenManagerViewModel
            )
            Screens.HOME -> HomePageView(
                visibleItemsViewModel = visibleItemsViewModel,
                visibleIndex = 1
            )
            Screens.GRADES -> GradesPageView(
                visibleItemsViewModel = visibleItemsViewModel,
                visibleIndex = 2
            )
            Screens.TESTS -> TestsPageView()
            Screens.CALENDAR -> SchedulePageView(
                visibleItemsViewModel = visibleItemsViewModel,
                visibleIndex = 4
            )
            Screens.GROUPS -> ClassGroupsPageView(
                visibleItemsViewModel = visibleItemsViewModel,
                visibleIndex = 5
            )
            Screens.PAYMENTS -> PaymentsPageView()
            Screens.ATTENDANCE -> AttendancePageView(
                visibleItemsViewModel = visibleItemsViewModel,
                visibleIndex = 7
            )
            Screens.SETTINGS -> SettingsPageView(
                visibleItemsViewModel = visibleItemsViewModel,
                visibleIndex = 8
            )
        }
    }
}