package com.cdkentertainment.mobilny_usos_enhanced.views

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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Course
import com.cdkentertainment.mobilny_usos_enhanced.models.Season
import com.cdkentertainment.mobilny_usos_enhanced.view_models.GradesPageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import com.cdkentertainment.mobilny_usos_enhanced.view_models.VisibleItemsViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GradesPageView(
    visibleItemsViewModel: VisibleItemsViewModel = viewModel<VisibleItemsViewModel>(),
    visibleIndex: Int = 2
) {
    val gradesPageViewModel: GradesPageViewModel = viewModel<GradesPageViewModel>()
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
        if (gradesPageViewModel.userGrades == null) {
            gradesPageViewModel.fetchUserGrades()
        }
        delay(50)
        visibleItemsViewModel.setVisibleState(visibleIndex, true)
    }

    if (gradesPageViewModel.userGrades == null) {
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
                        text = "Oceny",
                        style = MaterialTheme.typography.headlineLarge,
                        color = UISingleton.color4.primaryColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
            if (gradesPageViewModel.userGrades != null) {
                for (iteration in gradesPageViewModel.userGrades!!.size - 1 downTo  0) {
                    val season: Season = gradesPageViewModel.userGrades!![iteration]
                    val subjectCount: Int = season.courseList.size
                    stickyHeader {
                        AnimatedVisibility(
                            visible = isVisible[visibleIndex],
                            enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 2 * (iteration + 1), easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 2 * (iteration + 1), easingForShows)),
                            exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 2 * (iteration + 1), easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 2 * (iteration + 1), easingForShows))
                        ) {
                            Card(
                                colors = CardColors(
                                    contentColor = UISingleton.color4.primaryColor,
                                    containerColor = UISingleton.color1.primaryColor,
                                    disabledContainerColor = UISingleton.color1.primaryColor,
                                    disabledContentColor = UISingleton.color4.primaryColor
                                ),
                                shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius),
                                modifier = Modifier
                                    .border(5.dp, UISingleton.color2.primaryColor, RoundedCornerShape(
                                        UISingleton.uiElementsCornerRadius))
                                    .shadow(5.dp, RoundedCornerShape(UISingleton.uiElementsCornerRadius))
                            ) {
                                Text(
                                    text = season.seasonId,
                                    color = UISingleton.color4.primaryColor,
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier
                                        .padding(12.dp)
                                )
                            }
                        }
                    }
                    item {
                        AnimatedVisibility(
                            visible = isVisible[visibleIndex],
                            enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 3 * (iteration + 1), easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 3 * (iteration + 1), easingForShows)),
                            exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * 3 * (iteration + 1), easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * 3 * (iteration + 1), easingForShows))
                        ) {
                            GradeAverageView(season.avgGrade)
                        }
                    }
                    items(season.courseList.size) { courseIndex ->
                        val course: Course = season.courseList[courseIndex]
                        AnimatedVisibility(
                            visible = isVisible[visibleIndex],
                            enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * (4 + courseIndex) * (iteration + 1), easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * (4 + courseIndex) * (iteration + 1), easingForShows)),
                            exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * (4 + courseIndex) * (iteration + 1), easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * (4 + courseIndex) * (iteration + 1), easingForShows))
                        ) {
                            CourseGradesView(course, gradesPageViewModel.userSubjects!!, gradesPageViewModel.classtypeIdInfo)
                        }
                    }
                    item {
                        AnimatedVisibility(
                            visible = isVisible[visibleIndex],
                            enter = enterTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * (5 + subjectCount) * (iteration + 1), easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * (5 + subjectCount) * (iteration + 1), easingForShows)),
                            exit = exitTrans(fadeTweenSpec(fadeDuration, (delayBetweenShows + fadeDelay) * (5 + subjectCount) * (iteration + 1), easingForShows), slideTweenSpec(slideDuration, delayBetweenShows * (5 + subjectCount) * (iteration + 1), easingForShows))
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
}

@Preview(showBackground = true)
@Composable
fun GradesPagePreview() {
    OAuthSingleton.setTestAccessToken()
    val currentScreen: Screens = Screens.GRADES
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1.primaryColor)
            .padding(12.dp)
    ) {
        AnimatedContent(targetState = currentScreen) { target ->
            if (currentScreen == target) {
                GradesPageView()
            }
        }
    }
}