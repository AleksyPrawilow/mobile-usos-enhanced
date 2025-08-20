package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Lesson
import com.cdkentertainment.mobilny_usos_enhanced.view_models.SchedulePageViewModel
import kotlinx.coroutines.delay

@Composable
fun TimetableView(schedulePageViewModel: SchedulePageViewModel? = null) {
    val minutesDp: Dp = 24.dp
    val hoursDp: Dp = minutesDp * 4
    val startHour: Int = 7
    val endHour: Int = 22
    val totalHours: Int = endHour - startHour
    val totalHeight: Dp = hoursDp * totalHours
    var show: Boolean by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(150)
        show = true
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
                    modifier = Modifier.height(hoursDp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(60.dp)
                    ) {
                        androidx.compose.animation.AnimatedVisibility(
                            show,
                            enter = scaleIn(tween(300, delayMillis = 150 * (hour - startHour)))
                        ) {
                            Text(
                                text = "$hour:00",
                                fontSize = 18.sp.scaleIndependent(),
                                color = UISingleton.color3.primaryColor,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .graphicsLayer(
                                        rotationZ = 0f
                                    )
                                    .width(60.dp)
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = show,
                        enter = expandHorizontally(tween(300, delayMillis = 300 * (hour - startHour)), if ((hour - startHour) % 2 == 0) Alignment.End else Alignment.End),
                    ) {
                        Box(
                            contentAlignment = Alignment.TopStart
                        ) {
                            for (quarter in 0..1) {
                                HorizontalDivider(
                                    thickness = if (quarter == 0 || quarter == 1) 2.dp else 1.dp,
                                    color = UISingleton.color2.primaryColor,
                                    modifier = Modifier
                                        .offset(y = minutesDp * quarter * 4)
                                        .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
        if (schedulePageViewModel?.schedule != null) {
            if (schedulePageViewModel.schedule!!.lessons.containsKey(schedulePageViewModel.selectedDay)) {
                for (activityIndex in 0 until schedulePageViewModel.schedule!!.lessons[schedulePageViewModel.selectedDay]!!.size) {
                    TestActivityView(
                        minutesDp,
                        schedulePageViewModel.schedule!!.lessons[schedulePageViewModel.selectedDay]!![activityIndex],
                        schedulePageViewModel,
                        modifier = Modifier
                            .padding(start = 60.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TestActivityView(
    minutesDp: Dp,
    data: Lesson?,
    viewModel: SchedulePageViewModel?,
    modifier: Modifier
) {
    val minutesFromStart: Int = 7 * 60
    val startTime: String = viewModel!!.getTimeFromDate(data!!.start_time)
    val (hoursStr, minutesStr) = startTime.split(":")
    val startTimeMinutes: Int = hoursStr.toInt() * 60 + minutesStr.toInt()
    val endTime: String = viewModel!!.getTimeFromDate(data!!.end_time)
    val (endHoursStr, endMinutesStr) = endTime.split(":")
    val endTimeMinutes: Int = endHoursStr.toInt() * 60 + endMinutesStr.toInt()
    val durationMinutes: Int = endTimeMinutes - startTimeMinutes
    Card(
        colors = CardColors(
            contentColor = UISingleton.color4.primaryColor,
            containerColor = UISingleton.color2.primaryColor,
            disabledContainerColor = UISingleton.color2.primaryColor,
            disabledContentColor = UISingleton.color4.primaryColor
        ),
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(minutesDp * (durationMinutes / 15))
            .padding(6.dp)
            .offset(y = minutesDp * ((startTimeMinutes - minutesFromStart) / 15))
            .then(modifier)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "$startTime - $endTime",
                    color = UISingleton.color4.primaryColor,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .weight(1f)
                )
                Column {
                    Text(
                        text = data.classtype_id,
                        color = UISingleton.color3.primaryColor,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .align(Alignment.End)
                    )
                    Text(
                        text = "Sala: ${data.room_number}",
                        color = UISingleton.color3.primaryColor,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .align(Alignment.End)
                    )
                }
            }
            HorizontalDivider(
                thickness = 5.dp,
                color = UISingleton.color3.primaryColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            )
            Text(
                text = data.course_name.pl,
                color = UISingleton.color4.primaryColor,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
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
            .background(color = UISingleton.color1.primaryColor)
            .padding(12.dp)
    ) {
        TimetableView()
    }
}