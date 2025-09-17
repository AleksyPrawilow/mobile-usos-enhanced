package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.models.Lesson
import com.cdkentertainment.mobilny_usos_enhanced.models.Schedule
import com.cdkentertainment.mobilny_usos_enhanced.view_models.HomePageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomePageView() {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val viewModel: HomePageViewModel = viewModel<HomePageViewModel>()
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    val scaleEnterTransition: (Int) -> EnterTransition = UIHelper.scaleEnterTransition
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }
    var showActivityDetails: Lesson? by remember { mutableStateOf(null) }
    val paddingModifier: Modifier = Modifier.padding(horizontal = UISingleton.horizontalPadding, vertical = 8.dp)
    val density: Density = LocalDensity.current
    val context: Context = LocalContext.current
    val insets = WindowInsets.systemBars
    val topInset = insets.getTop(density)
    val bottomInset = insets.getBottom(density)
    val topPadding = with(density) { topInset.toDp() }
    val bottomPadding = with(density) { bottomInset.toDp() }
    val schedule: Schedule? = viewModel.todaySchedule
    val scheduleKeysEmpty: Boolean = schedule?.lessons?.keys?.isEmpty() ?: true
    val textMeasurer = rememberTextMeasurer()
    val cardLabels: List<Pair<String, ImageVector>> = listOf(
        Pair(stringResource(R.string.latest_grades), ImageVector.vectorResource(R.drawable.rounded_star_24)),
        Pair(stringResource(R.string.notifications), ImageVector.vectorResource(R.drawable.rounded_notifications_24)),
        Pair(stringResource(R.string.payments), ImageVector.vectorResource(R.drawable.rounded_payments_24)),
        Pair(stringResource(R.string.attendance), ImageVector.vectorResource(R.drawable.rounded_alarm_24))
    )
    val cardLabelStyle: TextStyle = MaterialTheme.typography.titleLarge
    val maxCardWidth: Int = remember(cardLabels, cardLabelStyle) {
        cardLabels.maxOf {
            textMeasurer.measure(
                text = AnnotatedString(it.first),
                style = cardLabelStyle
            ).size.width
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchClasstypes()
        viewModel.fetchSchedule()
        delay(150)
        showElements = true
    }

    showActivityDetails?.let { ActivityInfoPopupView(data = it, onDismissRequest = { showActivityDetails = null }) }

    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding, bottom = bottomPadding)
    ) {
        item {
            PageHeaderView("${stringResource(R.string.greeting)}, ${OAuthSingleton.userData?.basicInfo?.first_name}!")
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(1)) {
                UserDataView(paddingModifier)
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(2), modifier = paddingModifier) {
                Text(
                    text = stringResource(R.string.stay_up_to_date),
                    color = UISingleton.textColor1,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        item {
            FlowRow(
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp)
            ) {
                AnimatedVisibility(showElements, enter = scaleEnterTransition(3)) {
                    LatestSomethingView(
                        cardLabels[0].second,
                        cardLabels[0].first,
                        maxCardWidth,
                        badge = "2!"
                    )
                }
                AnimatedVisibility(showElements, enter = scaleEnterTransition(5)) {
                    LatestSomethingView(
                        cardLabels[1].second,
                        cardLabels[1].first,
                        maxCardWidth
                    )
                }
                AnimatedVisibility(showElements, enter = scaleEnterTransition(6)) {
                    LatestSomethingView(
                        cardLabels[2].second,
                        cardLabels[2].first,
                        maxCardWidth,
                        badge = "3000 zł"
                    )
                }
                AnimatedVisibility(showElements, enter = scaleEnterTransition(4)) {
                    LatestSomethingView(
                        cardLabels[3].second,
                        cardLabels[3].first,
                        maxCardWidth,
                        badge = "99!"
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(7), modifier = paddingModifier) {
                Text(
                    text = stringResource(R.string.todays_schedule),
                    color = UISingleton.textColor1,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        item {
            AnimatedVisibility(showElements && schedule == null && viewModel.scheduleFetchSuccess) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator(
                        color = UISingleton.textColor2,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
        item {
            AnimatedVisibility(!viewModel.scheduleFetchSuccess && showElements, enter = enterTransition(8)) {
                TextAndIconCardView(
                    stringResource(R.string.failed_to_fetch),
                    paddingModifier
                ) {
                    coroutineScope.launch {
                        viewModel.fetchSchedule()
                    }
                }
            }
        }
        item {
            AnimatedVisibility(showElements && scheduleKeysEmpty && schedule != null, enter = enterTransition(8)) {
                TextAndIconCardView(
                    title = stringResource(R.string.no_activities),
                    icon = Icons.Rounded.Done,
                    modifier = paddingModifier,
                    backgroundColor = UISingleton.color2,
                )
            }
        }
        item {
            if (schedule != null) {
                AnimatedVisibility(showElements && !scheduleKeysEmpty, enter = enterTransition(8)) {
                    GroupedContentContainerView(
                        title = stringResource(R.string.activities),
                        modifier = paddingModifier
                    ) {
                        for (day in schedule.lessons.keys) {
                            for (activity in schedule.lessons[day]!!) {
                                val time: String = "${viewModel.getTimeFromDate(activity.start_time)}-${viewModel.getTimeFromDate(activity.end_time)}"
                                TextAndBottomTextContainerView(
                                    title = activity.course_name.getLocalized(context),
                                    highlightedText = time,
                                    bottomFirstText = activity.classtype_id,
                                    bottomSecondText = activity.room_number,
                                    backgroundColor = UISingleton.color1
                                ) {
                                    showActivityDetails = activity
                                }
                            }
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(88.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    OAuthSingleton.setTestAccessToken()
    val currentScreen: Screens = Screens.HOME
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1)
            .padding(12.dp)
    ) {
        AnimatedContent(targetState = currentScreen) { target ->
            if (currentScreen == target) {
                HomePageView()
            }
        }
    }
}