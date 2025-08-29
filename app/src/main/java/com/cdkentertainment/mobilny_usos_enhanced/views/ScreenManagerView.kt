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
import com.cdkentertainment.mobilny_usos_enhanced.view_models.ScreenManagerViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens

@Composable
fun ScreenManager(
    currentScreen: Screens,
    screenManagerViewModel: ScreenManagerViewModel
) {
    AnimatedContent(
        transitionSpec = { fadeIn() + scaleIn() togetherWith fadeOut() + slideOutHorizontally(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) },
        targetState = currentScreen
    ) { target ->
        when (target) {
            Screens.LOGIN -> LoginPageView(
                screenManagerViewModel = screenManagerViewModel
            )
            Screens.HOME -> HomePageView()
            Screens.GRADES -> GradesPageView()
            Screens.TESTS -> TestsPageView()
            Screens.CALENDAR -> SchedulePageView()
            Screens.GROUPS -> ClassGroupsPageView()
            Screens.PAYMENTS -> PaymentsPageView()
            Screens.ATTENDANCE -> AttendancePageView()
            Screens.LECTURERS -> HomePageView()
            Screens.SETTINGS -> SettingsPageView()
        }
    }
}