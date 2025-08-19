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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.view_models.SchedulePageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import com.cdkentertainment.mobilny_usos_enhanced.view_models.VisibleItemsViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class)
@Composable
fun SharedTransitionScope.SchedulePageView(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    visibleItemsViewModel: VisibleItemsViewModel = viewModel<VisibleItemsViewModel>(),
    visibleIndex: Int = 4
) {
    val schedulePageViewModel: SchedulePageViewModel = viewModel<SchedulePageViewModel>()
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
        delay(50)
        visibleItemsViewModel.setVisibleState(visibleIndex, true)
        if (schedulePageViewModel.schedule == null) {
            schedulePageViewModel.fetchWeekData(LocalDate.of(2025, 5, 13))
        }
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
                    text = "Rozkład zajęć",
                    style = MaterialTheme.typography.headlineLarge,
                    color = UISingleton.color4.primaryColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
        stickyHeader {
            AnimatedVisibility(
                visible = isVisible[visibleIndex],
                enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 2, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 2, easingForShows)),
                exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 2, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 2, easingForShows))
            ) {
                ScheduleDaySelectorView(schedulePageViewModel)
            }
        }
//            item {
//                AnimatedVisibility(
//                    visible = isVisible[visibleIndex],
//                    enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 2, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 2, easingForShows)),
//                    exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 2, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 2, easingForShows))
//                ) {
//                    Card(
//                        colors = CardColors(
//                            contentColor = UISingleton.color4.primaryColor,
//                            containerColor = UISingleton.color3.primaryColor,
//                            disabledContainerColor = UISingleton.color3.primaryColor,
//                            disabledContentColor = UISingleton.color4.primaryColor
//                        ),
//                        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .border(5.dp, UISingleton.color4.primaryColor, RoundedCornerShape(UISingleton.uiElementsCornerRadius))
//                    ) {
//                        Text(
//                            text = "${(schedulePageViewModel.schedule?.startDay?.replace("-", ".") ?: "N/A")} - ${(schedulePageViewModel.schedule?.endDay?.replace("-", ".") ?: "N/A")}",
//                            style = MaterialTheme.typography.titleLarge,
//                            color = UISingleton.color1.primaryColor,
//                            modifier = Modifier
//                                .padding(12.dp)
//                        )
//                    }
//                }
//            }
//        item {
//            TimetableView(schedulePageViewModel)
//        }
        if (schedulePageViewModel.schedule == null) {
            item {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(color = UISingleton.color3.primaryColor, modifier = Modifier.align(Alignment.Center))
                }
            }
        } else {
            if (schedulePageViewModel.schedule!!.lessons.containsKey(schedulePageViewModel.selectedDay)) {
                items(schedulePageViewModel.schedule!!.lessons[schedulePageViewModel.selectedDay]!!.size, key = { it }) { activityIndex ->
                    ActivityView(
                        schedulePageViewModel = schedulePageViewModel,
                        activity = schedulePageViewModel.schedule!!.lessons[schedulePageViewModel.selectedDay]!![activityIndex],
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun SchedulePagePreview() {
    OAuthSingleton.setTestAccessToken()
    val currentScreen: Screens = Screens.GRADES
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1.primaryColor)
            .padding(12.dp)
    ) {
        SharedTransitionLayout {
            AnimatedContent(targetState = currentScreen) { target ->
                if (currentScreen == target) {
                    SchedulePageView(this@SharedTransitionLayout, this@AnimatedContent)
                }
            }
        }
    }
}