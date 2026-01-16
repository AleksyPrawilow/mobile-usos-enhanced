package com.cdkentertainment.bux.views

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.bux.R
import com.cdkentertainment.bux.UIHelper
import com.cdkentertainment.bux.UISingleton
import com.cdkentertainment.bux.UserDataSingleton
import com.cdkentertainment.bux.getLocalized
import com.cdkentertainment.bux.models.Lesson
import com.cdkentertainment.bux.models.Schedule
import com.cdkentertainment.bux.view_models.AttendancePageViewModel
import com.cdkentertainment.bux.view_models.GradesPageViewModel
import com.cdkentertainment.bux.view_models.HomePageViewModel
import com.cdkentertainment.bux.view_models.PaymentsPageViewModel
import com.cdkentertainment.bux.view_models.ScreenManagerViewModel
import com.cdkentertainment.bux.view_models.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomePageView() {
    val viewModel: HomePageViewModel = viewModel<HomePageViewModel>()
    val gradesViewModel: GradesPageViewModel = viewModel<GradesPageViewModel>()
    val attendancePageViewModel: AttendancePageViewModel = viewModel<AttendancePageViewModel>()
    val screenManagerViewModel: ScreenManagerViewModel = viewModel<ScreenManagerViewModel>()
    val paymentsPageViewModel: PaymentsPageViewModel = viewModel<PaymentsPageViewModel>()

    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val enterTransition     : (Int) -> EnterTransition = UIHelper.slideEnterTransition
    val scaleEnterTransition: (Int) -> EnterTransition = UIHelper.scaleEnterTransition

    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }
    var showActivityDetails: Lesson? by remember { mutableStateOf(null) }
    var loading: Boolean by rememberSaveable { mutableStateOf(false) }
    var loadingError: Boolean by rememberSaveable { mutableStateOf(false) }

    val paddingModifier: Modifier = Modifier.padding(
        horizontal = UISingleton.horizontalPadding,
        vertical = 8.dp
    )

    val context: Context = LocalContext.current

    val schedule: Schedule? = viewModel.todaySchedule
    val scheduleKeysEmpty: Boolean = schedule?.lessons?.keys?.isEmpty() ?: true

    val textMeasurer = rememberTextMeasurer()
    val cardLabels: List<Pair<String, ImageVector>> = listOf(
        Pair(stringResource(R.string.latest_grades), ImageVector.vectorResource(R.drawable.rounded_star_24)),
        Pair(stringResource(R.string.tests_page), ImageVector.vectorResource(R.drawable.rounded_assignment_24)),
        Pair(stringResource(R.string.payments), ImageVector.vectorResource(R.drawable.rounded_payments_24)),
        Pair(stringResource(R.string.lecturers), ImageVector.vectorResource(R.drawable.rounded_school_24))
    )

    val cardLabelStyle: TextStyle = MaterialTheme.typography.titleMedium
    val unpaidSum: Float = paymentsPageViewModel.unpaidSum
    val unpaidSumFormatted: String? = if (unpaidSum == 0f) null else "${"%.2f".format(unpaidSum)} zł"

    val maxCardWidth: Int = remember(cardLabels, cardLabelStyle) {
        cardLabels.maxOf {
            textMeasurer.measure(
                text = AnnotatedString(it.first),
                style = cardLabelStyle
            ).size.width
        }
    }

    val onStart: () -> Unit = {
        coroutineScope.launch {
            showElements = false
            loading = true
            loadingError = false

            paymentsPageViewModel.fetchPayments()
            gradesViewModel.fetchLatestGrades()
            gradesViewModel.fetchSemesterGrades(UIHelper.termIds.last().id)
            loadingError = !viewModel.fetchSchedule()

            loading = false
            delay(150)
            showElements = true
            screenManagerViewModel.showFab = !loadingError
        }
    }

    LaunchedEffect(Unit) {
        onStart()
    }

    showActivityDetails?.let {
        ActivityInfoPopupView(
            data = it,
            onDismissRequest = { showActivityDetails = null }
        )
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        item {
            PageHeaderView("${stringResource(R.string.greeting)}, ${UserDataSingleton.userData?.first_name}!")
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            AnimatedVisibility(loading, enter = enterTransition(1)) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(
                        color = UISingleton.textColor2,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        item {
            AnimatedVisibility(loadingError && showElements, enter = enterTransition(1)) {
                TextAndIconCardView(
                    stringResource(R.string.failed_to_fetch),
                    paddingModifier
                ) {
                    onStart()
                }
            }
        }

        item {
            AnimatedVisibility(showElements && !loadingError, enter = enterTransition(1)) {
                UserDataView(paddingModifier)
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            AnimatedVisibility(showElements && !loadingError, enter = enterTransition(2), modifier = paddingModifier) {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
            ) {
                AnimatedVisibility(showElements && !loadingError, enter = scaleEnterTransition(3)) {
                    LatestSomethingView(
                        icon = cardLabels[0].second,
                        title = cardLabels[0].first,
                        maxWidth = maxCardWidth,
                        badge = if (gradesViewModel.latestGrades.isNullOrEmpty()) null else gradesViewModel.latestGrades?.size.toString() + "!"
                    ) {
                        screenManagerViewModel.changeScreen(Screens.GRADES, context)
                    }
                }

                AnimatedVisibility(showElements && !loadingError, enter = scaleEnterTransition(5)) {
                    LatestSomethingView(
                        icon = cardLabels[1].second,
                        title = cardLabels[1].first,
                        maxWidth = maxCardWidth
                    ) {
                        screenManagerViewModel.changeScreen(Screens.TESTS, context)
                    }
                }

                AnimatedVisibility(showElements && !loadingError, enter = scaleEnterTransition(6)) {
                    LatestSomethingView(
                        icon = cardLabels[2].second,
                        title = cardLabels[2].first,
                        maxWidth = maxCardWidth,
                        badge = unpaidSumFormatted,
                        loading = paymentsPageViewModel.loading,
                        loadingError = paymentsPageViewModel.error
                    ) {
                        screenManagerViewModel.changeScreen(Screens.PAYMENTS, context)
                    }
                }

                AnimatedVisibility(showElements && !loadingError, enter = scaleEnterTransition(4)) {
                    LatestSomethingView(
                        icon = cardLabels[3].second,
                        title = cardLabels[3].first,
                        maxWidth = maxCardWidth,
                    ) {
                        screenManagerViewModel.changeScreen(Screens.LECTURERS, context)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            AnimatedVisibility(showElements && !loadingError, enter = enterTransition(7), modifier = paddingModifier) {
                Text(
                    text = stringResource(R.string.todays_schedule),
                    color = UISingleton.textColor1,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            AnimatedVisibility(showElements && scheduleKeysEmpty && schedule != null && !loadingError, enter = enterTransition(8)) {
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
                AnimatedVisibility(showElements && !scheduleKeysEmpty && !loadingError, enter = enterTransition(8)) {
                    GroupedContentContainerView(
                        title = stringResource(R.string.activities),
                        modifier = paddingModifier
                    ) {
                        for (day in schedule.lessons.keys) {
                            val activities: List<Lesson>? = schedule.lessons[day]
                            if (activities == null || activities.isEmpty()) {
                                continue
                            }

                            for (activity in activities) {
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

        item { Spacer(modifier = Modifier.height(88.dp)) }
    }
}