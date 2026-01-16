package com.cdkentertainment.bux.views

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.bux.UISingleton
import com.cdkentertainment.bux.models.Schedule
import com.cdkentertainment.bux.scaleIndependent
import com.cdkentertainment.bux.spToDp
import com.cdkentertainment.bux.view_models.SchedulePageViewModel
import kotlinx.coroutines.delay

@Composable
fun TimetableView(
    minutesDp: Dp = spToDp(32.sp),
    startHour: Int = 7,
    endHour: Int = 23,
    schedule: Schedule? = null
) {
    val schedulePageViewModel: SchedulePageViewModel = viewModel<SchedulePageViewModel>()
    val hoursDp: Dp = minutesDp * 4
    val totalHours: Int = endHour - startHour
    val totalHeight: Dp = hoursDp * totalHours
    var show: Boolean by rememberSaveable { mutableStateOf(true) }
    var showDividers: Boolean by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(150)
        show = true
        delay(100)
        showDividers = true
    }
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier
                .height(totalHeight)
        ) {
            for (hour in startHour..endHour) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(-60.dp),
                    modifier = Modifier.height(hoursDp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(60.dp)
                            .height(hoursDp)
                    ) {
                        androidx.compose.animation.AnimatedVisibility(
                            show,
                            enter = scaleIn(tween(300, delayMillis = 150 * (hour - startHour)))
                        ) {
                            Text(
                                text = "$hour:00",
                                fontSize = 18.sp.scaleIndependent,
                                textAlign = TextAlign.Center,
                                color = UISingleton.textColor2,
                                modifier = Modifier
                                    .width(60.dp)
                            )
                        }
                    }
                    Box(
                        contentAlignment = Alignment.TopStart,
                        modifier = Modifier.height(hoursDp)
                    ) {
                        val fraction by animateFloatAsState(
                            if (showDividers) 1f else 0f,
                            tween(delayMillis = 150 * (hour - startHour))
                        )
                        val verticalFraction by animateFloatAsState(
                            if (showDividers) 1f else 0f,
                            tween(150, 150 * (hour - startHour), LinearEasing)
                        )
                        Box(
                            modifier = Modifier
                                .padding(start = 60.dp)
                                .fillMaxHeight(verticalFraction)
                                .width(2.dp)
                                .background(UISingleton.color2)
                        )
                        for (quarter in 0..1) {
                            Box(
                                modifier = Modifier
                                    .height(2.dp)
                                    .fillMaxWidth(fraction)
                                    .offset(y = minutesDp * quarter * 4)
                                    .background(UISingleton.color2, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                            )
                        }
                    }
                }
            }
        }
        if (schedule != null) {
            if (schedule.lessons.containsKey(schedulePageViewModel.selectedDay)) {
                for (activityIndex in 0 until schedule.lessons[schedulePageViewModel.selectedDay]!!.size) {
                    key("$activityIndex/${schedulePageViewModel.selectedDay}") {
                        TimetableActivityView(
                            minutesDp = minutesDp,
                            data = schedule.lessons[schedulePageViewModel.selectedDay]!![activityIndex],
                            index = activityIndex
                        )
                    }
                }
            }
        }
    }
}