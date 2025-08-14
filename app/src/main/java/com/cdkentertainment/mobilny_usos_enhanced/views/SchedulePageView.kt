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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.view_models.GradesPageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import com.cdkentertainment.mobilny_usos_enhanced.view_models.VisibleItemsViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class)
@Composable
fun SharedTransitionScope.SchedulePageView(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    visibleItemsViewModel: VisibleItemsViewModel = viewModel<VisibleItemsViewModel>(),
    visibleIndex: Int = 4
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
    val daysOfWeek: List<String> = listOf("pn", "wt", "śr", "cz", "pt")

    LaunchedEffect(Unit) {
        if (gradesPageViewModel.userGrades == null) {
            gradesPageViewModel.fetchUserGrades()
        }
        delay(50)
        visibleItemsViewModel.setVisibleState(visibleIndex, true)
    }

    if (gradesPageViewModel.userGrades == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(5.dp, RoundedCornerShape(UISingleton.uiElementsCornerRadius))
                            .background(
                                brush = verticalGradient(
                                    0.0f to UISingleton.color2.primaryColor,
                                    0.5f to UISingleton.color2.primaryColor,
                                    0.5f to UISingleton.color2.oppositeColor
                                ),
                                shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius))
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .horizontalScroll(rememberScrollState())
                            ) {
                                Button(
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = UISingleton.color4.primaryColor,
                                        containerColor = UISingleton.color1.primaryColor
                                    ),
                                    onClick = {

                                    }
                                ) {
                                    Text(
                                        text = "Aktualny tydzień",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                                Button(
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = UISingleton.color4.primaryColor,
                                        containerColor = UISingleton.color1.primaryColor
                                    ),
                                    onClick = {

                                    }
                                ) {
                                    Text(
                                        text = "Inny tydzień",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .horizontalScroll(rememberScrollState())
                            ) {
                                for (dayIndex in 0 until daysOfWeek.size) {
                                    Button(
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = UISingleton.color4.oppositeColor,
                                            containerColor = UISingleton.color1.oppositeColor
                                        ),
                                        onClick = {

                                        }
                                    ) {
                                        Text(
                                            text = daysOfWeek[dayIndex],
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
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