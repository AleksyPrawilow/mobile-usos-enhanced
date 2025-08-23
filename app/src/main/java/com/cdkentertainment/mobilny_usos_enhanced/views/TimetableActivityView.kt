package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Lesson
import com.cdkentertainment.mobilny_usos_enhanced.view_models.SchedulePageViewModel
import kotlinx.coroutines.delay

@Composable
fun TimetableActivityView(
    minutesDp: Dp,
    data: Lesson?,
    viewModel: SchedulePageViewModel?,
    index: Int
) {
    val minutesFromStart: Int = 7 * 60
    val startTime: String = viewModel!!.getTimeFromDate(data!!.start_time)
    val (hoursStr, minutesStr) = startTime.split(":")
    val startTimeMinutes: Int = hoursStr.toInt() * 60 + minutesStr.toInt()
    val endTime: String = viewModel!!.getTimeFromDate(data!!.end_time)
    val (endHoursStr, endMinutesStr) = endTime.split(":")
    val endTimeMinutes: Int = endHoursStr.toInt() * 60 + endMinutesStr.toInt()
    val durationMinutes: Int = endTimeMinutes - startTimeMinutes

    var show: Boolean by rememberSaveable { mutableStateOf(false) }
    val appearFactor: Float by animateFloatAsState(
        if (show) 1f else 0f,
        tween(300, 150 * index, EaseOutBack)
    )

    LaunchedEffect(Unit) {
        delay(20)
        show = true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(minutesDp * (durationMinutes / 15))
            .padding(horizontal = 6.dp)
            .offset(x = (-40).dp * (1f - appearFactor), y = minutesDp * ((startTimeMinutes - minutesFromStart) / 15))
            .padding(start = 60.dp)
            .graphicsLayer(
                alpha = appearFactor
            )
            .shadow(5.dp, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            .background(
                brush = verticalGradient(
                    0.0f to UISingleton.color2.primaryColor,
                    0.5f to UISingleton.color2.primaryColor,
                    0.5f to UISingleton.color1.primaryColor
                ),
                shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = "$startTime - $endTime",
                    color = UISingleton.color4.primaryColor,
                    style = MaterialTheme.typography.titleMedium,
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
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) {
                Text(
                    text = data.course_name.pl,
                    color = UISingleton.color4.primaryColor,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}