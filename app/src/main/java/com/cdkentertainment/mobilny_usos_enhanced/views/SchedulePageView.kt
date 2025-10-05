package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Schedule
import com.cdkentertainment.mobilny_usos_enhanced.spToDp
import com.cdkentertainment.mobilny_usos_enhanced.view_models.SchedulePageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.min

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SchedulePageView() {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    val schedulePageViewModel: SchedulePageViewModel = viewModel<SchedulePageViewModel>()
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }
    val listState: LazyListState = rememberLazyListState()
    val density: Density = LocalDensity.current

    val insets = WindowInsets.systemBars
    val topInset = insets.getTop(density)
    val bottomInset = insets.getBottom(density)
    val topPadding = with(density) { topInset.toDp() }
    val bottomPadding = with(density) { bottomInset.toDp() }

    val paddingModifier: Modifier = Modifier.padding(
        horizontal = UISingleton.horizontalPadding,
        vertical = 8.dp
    )

    val minutesDp: Dp = spToDp(32.sp)
    val startHour: Int = 7
    val endHour: Int = 23
    val schedule: Schedule? = schedulePageViewModel.schedule

    val onDaySelected: (Int) -> Unit = { index ->
        if (schedule != null) {
            schedulePageViewModel.groupLessonsByHour(schedule.lessons[index])
        }
        try {
            val firstActivityHour: Int = schedulePageViewModel.groupedByHours.keys.sorted()[0]
            coroutineScope.launch {
                val targetPx = with(density) {
                    (minutesDp * 4 * (firstActivityHour - startHour - 1)).toPx().toInt()
                }
                val delta = targetPx - listState.firstVisibleItemScrollOffset
                listState.animateScrollBy(delta.toFloat())
            }
        } catch (e: Exception) { }
    }

    val onStart: () -> Unit = {
        coroutineScope.launch {
            schedulePageViewModel.fetchWeekData(LocalDate.now())
            delay(150)
            showElements = true
            delay(750)
            onDaySelected(min(LocalDate.now().dayOfWeek.value - 1, 4))
        }
    }

    LaunchedEffect(Unit) {
        onStart()
    }

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = topPadding,
                bottom = bottomPadding
            )
    ) {
        item {
            PageHeaderView(
                text = stringResource(R.string.schedule_page_header),
                icon = ImageVector.vectorResource(R.drawable.rounded_calendar_month_24)
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            AnimatedVisibility(
                visible = schedulePageViewModel.loading,
                modifier = paddingModifier
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        color = UISingleton.textColor2,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = schedulePageViewModel.error && showElements,
                enter = enterTransition(1)
            ) {
                TextAndIconCardView(
                    stringResource(R.string.failed_to_fetch),
                    paddingModifier
                ) {
                    onStart()
                }
            }
        }

        stickyHeader {
            AnimatedVisibility(
                showElements && schedulePageViewModel.loaded,
                enter = enterTransition(1)
            ) {
                ScheduleDaySelectorView(
                    coroutineScope = coroutineScope,
                    modifier = paddingModifier,
                    onDaySelected = onDaySelected
                )
            }
        }

        item {
            AnimatedVisibility(
                showElements && schedulePageViewModel.loaded,
                enter = enterTransition(2)
            ) {
                if (schedule?.startDay != null && schedule.endDay != null) {
                    val startLocalDate: LocalDate = LocalDate.parse(schedule.startDay)
                    val endLocalDate: LocalDate = LocalDate.parse(schedule.endDay)
                    val weekString: String =
                        "${startLocalDate.format(formatter)} — ${endLocalDate.format(formatter)}"

                    Text(
                        text = weekString,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = UISingleton.textColor1,
                        modifier = paddingModifier
                    )
                }
            }
        }

        item {
            AnimatedVisibility(
                showElements && schedulePageViewModel.loaded,
                enter = enterTransition(3)
            ) {
                TimetableView(
                    minutesDp = minutesDp,
                    startHour = startHour,
                    endHour = endHour,
                    schedule = schedule
                )
            }
        }

        item { Spacer(modifier = Modifier.height(64.dp)) }
    }
}