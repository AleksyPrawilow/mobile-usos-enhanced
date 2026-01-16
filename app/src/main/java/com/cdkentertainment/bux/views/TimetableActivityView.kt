package com.cdkentertainment.bux.views

import android.content.Context
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.bux.UIHelper
import com.cdkentertainment.bux.UISingleton
import com.cdkentertainment.bux.getLocalized
import com.cdkentertainment.bux.models.Lesson
import com.cdkentertainment.bux.view_models.SchedulePageViewModel
import kotlinx.coroutines.delay

@Composable
fun TimetableActivityView(
    minutesDp: Dp,
    data: Lesson,
    index: Int
) {
    val viewModel: SchedulePageViewModel = viewModel<SchedulePageViewModel>()
    val context: Context = LocalContext.current
    val minutesFromStart: Int = 7 * 60
    val startTime: String = viewModel.getTimeFromDate(data.start_time)
    val (hoursStr, minutesStr) = startTime.split(":")
    val startTimeMinutes: Int = hoursStr.toInt() * 60 + minutesStr.toInt()
    val endTime: String = viewModel.getTimeFromDate(data.end_time)
    val (endHoursStr, endMinutesStr) = endTime.split(":")
    val endTimeMinutes: Int = endHoursStr.toInt() * 60 + endMinutesStr.toInt()
    val durationMinutes: Int = endTimeMinutes - startTimeMinutes
    val imageVector: ImageVector = ImageVector.vectorResource(UIHelper.activityTypeIconMapping[data.classtype_id] ?: UIHelper.otherIcon)
    var showDetails: Boolean by rememberSaveable { mutableStateOf(false) }
    var show: Boolean by rememberSaveable { mutableStateOf(false) }
    val appearFactor: Float by animateFloatAsState(
        if (show) 1f else 0f,
        tween(300, 150 * index, EaseOutBack)
    )

    LaunchedEffect(Unit) {
        delay(20)
        show = true
    }

    if (showDetails) {
        ActivityInfoPopupView(data = data, onDismissRequest = { showDetails = false })
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(minutesDp * (durationMinutes / 15))
            .padding(horizontal = UISingleton.horizontalPadding)
            .offset(x = (-40).dp * (1f - appearFactor), y = minutesDp * ((startTimeMinutes - minutesFromStart) / 15))
            .padding(start = 60.dp)
            .graphicsLayer(
                alpha = appearFactor
            )
            .shadow(3.dp, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            .background(
                brush = verticalGradient(
                    0.0f to UISingleton.color2,
                    0.5f to UISingleton.color2,
                    0.5f to UISingleton.color1
                ),
            )
            .clickable(onClick = {
                showDetails = true
            })
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            VerticalDivider(
                color = UISingleton.color3,
                thickness = UISingleton.uiElementsCornerRadius.dp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(48.dp)
                    //.offset(y = -(UISingleton.uiElementsCornerRadius.dp))
                    .background(
                        UISingleton.color4,
                        RoundedCornerShape(
                            bottomStart = UISingleton.uiElementsCornerRadius.dp,
                            bottomEnd = 0.dp,
                            topStart = UISingleton.uiElementsCornerRadius.dp,
                            topEnd = UISingleton.uiElementsCornerRadius.dp
                        )
                    )
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(UISingleton.uiElementsCornerRadius.dp)
                        .offset(y = UISingleton.uiElementsCornerRadius.dp)
                        .background(
                            UISingleton.color4
                        )
                        .background(
                            UISingleton.color3,
                            RoundedCornerShape(
                                topEnd = UISingleton.uiElementsCornerRadius.dp
                            )
                        )
                )
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = UISingleton.textColor4,
                    modifier = Modifier.fillMaxSize().padding(6.dp)
                )
            }
            Box(
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(UISingleton.color3)
                ) {
                    Text(
                        text = data.classtype_id,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        color = UISingleton.textColor4,
                        modifier = Modifier
                            .background(
                                UISingleton.color4,
                                RoundedCornerShape(
                                    bottomStart = UISingleton.uiElementsCornerRadius.dp,
                                    bottomEnd = UISingleton.uiElementsCornerRadius.dp,
                                    topStart = 0.dp,
                                    topEnd = 0.dp
                                )
                            )
                            .zIndex(0.75f)
                            .padding(top = 4.dp, bottom = 4.dp, start = 12.dp, end = 12.dp)
                    )
                    Text(
                        text = data.room_number,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        color = UISingleton.textColor4,
                        modifier = Modifier
                            .offset(x = -UISingleton.uiElementsCornerRadius.dp)
                            .weight(1f)
                            .background(
                                UISingleton.color3
                            )
                            .zIndex(0.5f)
                            .padding(top = 4.dp, bottom = 4.dp, start = (12 + UISingleton.uiElementsCornerRadius).dp, end = 12.dp)
                    )
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.weight(1f).fillMaxWidth().padding(end = UISingleton.uiElementsCornerRadius.dp)
            ) {
                Text(
                    text = data.course_name.getLocalized(context),
                    color = UISingleton.textColor1,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = "$startTime - $endTime",
                    color = UISingleton.textColor1,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }
}