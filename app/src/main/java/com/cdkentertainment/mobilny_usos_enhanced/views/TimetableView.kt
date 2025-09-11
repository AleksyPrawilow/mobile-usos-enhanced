package com.cdkentertainment.mobilny_usos_enhanced.views

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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.view_models.SchedulePageViewModel
import kotlinx.coroutines.delay

@Composable
fun TimetableView(schedulePageViewModel: SchedulePageViewModel? = null) {
    val minutesDp: Dp = spToDp(32.sp)
    val hoursDp: Dp = minutesDp * 4
    val startHour: Int = 7
    val endHour: Int = 22
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
        modifier = Modifier.fillMaxWidth()
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
                                fontSize = 18.sp.scaleIndependent(),
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
        if (schedulePageViewModel?.schedule != null) {
            if (schedulePageViewModel.schedule!!.lessons.containsKey(schedulePageViewModel.selectedDay)) {
                for (activityIndex in 0 until schedulePageViewModel.schedule!!.lessons[schedulePageViewModel.selectedDay]!!.size) {
                    key("$activityIndex/${schedulePageViewModel.selectedDay}") {
                        TimetableActivityView(
                            minutesDp,
                            schedulePageViewModel.schedule!!.lessons[schedulePageViewModel.selectedDay]!![activityIndex],
                            schedulePageViewModel,
                            activityIndex
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun spToDp(sp: TextUnit): Dp {
    val density = LocalDensity.current
    return with(density) { sp.toDp() }
}

@Composable
fun TextUnit.scaleIndependent(): TextUnit {
    return this / LocalDensity.current.fontScale
}

@Preview(showBackground = true)
@Composable
fun TimetablePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = UISingleton.color1)
            .padding(12.dp)
    ) {
        TimetableView()
    }
}