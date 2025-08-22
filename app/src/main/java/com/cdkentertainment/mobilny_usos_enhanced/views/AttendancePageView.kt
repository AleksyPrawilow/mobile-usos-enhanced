package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup
import com.cdkentertainment.mobilny_usos_enhanced.view_models.AttendancePageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import com.cdkentertainment.mobilny_usos_enhanced.view_models.VisibleItemsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AttendancePageView(
    visibleItemsViewModel: VisibleItemsViewModel = viewModel<VisibleItemsViewModel>(),
    visibleIndex: Int = 7
) {
    val attendancePageViewModel: AttendancePageViewModel = viewModel<AttendancePageViewModel>()
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

    val context: Context = LocalContext.current
    val onPopupDismissRequest: () -> Unit = {
        CoroutineScope(Dispatchers.IO).launch {
            attendancePageViewModel.dismissPopup(context)
        }
    }

    LaunchedEffect(Unit) {
        attendancePageViewModel.fetchLessonGroups()
        delay(50)
        visibleItemsViewModel.setVisibleState(visibleIndex, true)
    }

    if (attendancePageViewModel.showPopup) {
        AttendancePopupView(viewModel = attendancePageViewModel, onDismissRequest = onPopupDismissRequest)
    }

    if (attendancePageViewModel.lessonGroups == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(color = UISingleton.color3.primaryColor, modifier = Modifier.align(Alignment.Center))
        }
    } else {
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
                        text = "Obecność",
                        style = MaterialTheme.typography.headlineLarge,
                        color = UISingleton.color4.primaryColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
            for (seasonId in attendancePageViewModel.lessonGroups!!.groups.keys.reversed()) {
                val season: List<LessonGroup>? = attendancePageViewModel.lessonGroups!!.groups[seasonId]
                val groupCount: Int = season?.size ?: 0
                stickyHeader {
                    AnimatedVisibility(
                        visible = isVisible[visibleIndex],
                        enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 2, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 2, easingForShows)),
                        exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 2, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 2, easingForShows))
                    ) {
                        SemesterCardView(seasonId)
                    }
                }
                if (season != null) {
                    items(season.size) { groupIndex ->
                        val group: LessonGroup = season[groupIndex]
                        AnimatedVisibility(
                            visible = isVisible[visibleIndex],
                            enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * (3 + groupIndex), easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * (3 + groupIndex), easingForShows)),
                            exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * (3 + groupIndex), easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * (3 + groupIndex), easingForShows))
                        ) {
                            AttendanceClassGroupView(data = group, viewModel = attendancePageViewModel)
                        }
                    }
                } else {
                    item {
                        AnimatedVisibility(
                            visible = isVisible[visibleIndex],
                            enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 3, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 3, easingForShows)),
                            exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 3, easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 3, easingForShows))
                        ) {
                            Text(
                                text = "Brak grup zajęciowych w tym semestrze",
                                style = MaterialTheme.typography.titleLarge,
                                color = UISingleton.color4.primaryColor,
                            )
                        }
                    }
                }
                item {
                    AnimatedVisibility(
                        visible = isVisible[visibleIndex],
                        enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * (4 + groupCount), easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * (4 + groupCount), easingForShows)),
                        exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * (4 + groupCount), easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * (4 + groupCount), easingForShows))
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AttendancePagePreview() {
    OAuthSingleton.setTestAccessToken()
    val currentScreen: Screens = Screens.GROUPS
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1.primaryColor)
            .padding(12.dp)
    ) {
        AnimatedContent(targetState = currentScreen) { target ->
            if (currentScreen == target) {
                AttendancePageView()
            }
        }
    }
}