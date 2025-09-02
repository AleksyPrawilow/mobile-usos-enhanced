package com.cdkentertainment.mobilny_usos_enhanced.views

import android.graphics.Color.TRANSPARENT
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.view_models.SchedulePageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDaySelectorView(schedulePageViewModel: SchedulePageViewModel) {
    val daysOfWeek: List<String> = listOf("pn", "wt", "śr", "cz", "pt")
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(Unit) {
        schedulePageViewModel.selectDay(min(LocalDate.now().dayOfWeek.value - 1, 4))
    }

    AnimatedVisibility(schedulePageViewModel.displayDateSelector) {
        DatePickerDialog(
//            colors = DatePickerDefaults.colors(
//
//            ),
            onDismissRequest = { },
            confirmButton = {
                TextButton(
                    onClick = {
                        println(datePickerState.selectedDateMillis)
                        val localDate = datePickerState.selectedDateMillis?.let {
                            Instant.ofEpochMilli(it)
                                .atZone(ZoneId.of("Europe/Warsaw"))
                                .toLocalDate()
                        }
                        if (localDate != null) {
                            CoroutineScope(Dispatchers.IO).launch {
                                schedulePageViewModel.resetSchedule()
                                schedulePageViewModel.fetchWeekData(localDate)
                            }
                        }
                        schedulePageViewModel.setDateSelectorVisibility(false)
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        schedulePageViewModel.setDateSelectorVisibility(false)
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
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
                shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius)
            )
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
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                modifier = Modifier
                    .fillMaxHeight()
                    .horizontalScroll(rememberScrollState())
            ) {
                for (weekOptionIndex in 0..1) {
                    Text(
                        text = if (weekOptionIndex == 0) "Aktualny tydzień" else "Inny tydzień",
                        color = UISingleton.color4.primaryColor,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .background(if (schedulePageViewModel.selectedWeekOption == weekOptionIndex) UISingleton.color1.primaryColor else Color(TRANSPARENT), CircleShape)
                            .clip(CircleShape)
                            .clickable(onClick = {
                                if (weekOptionIndex == 0) {
                                    if (schedulePageViewModel.selectedWeekOption != weekOptionIndex) {
                                        schedulePageViewModel.resetSchedule()
                                        schedulePageViewModel.selectDay(min(LocalDate.now().dayOfWeek.value - 1, 4))
                                        CoroutineScope(Dispatchers.IO).launch {
                                            schedulePageViewModel.fetchWeekData(LocalDate.of(2025, 5, 13))
                                        }
                                    }
                                } else {
                                    schedulePageViewModel.setDateSelectorVisibility(true)
                                }
                                schedulePageViewModel.selectWeekOption(weekOptionIndex)
                            })
                            .padding(horizontal = 16.dp, 12.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                modifier = Modifier
                    .fillMaxHeight()
                    .horizontalScroll(rememberScrollState())
            ) {
                for (dayIndex in 0 until daysOfWeek.size) {
                    Text(
                        text = daysOfWeek[dayIndex],
                        style = MaterialTheme.typography.titleMedium,
                        color = UISingleton.color4.oppositeColor,
                        modifier = Modifier
                            .background(if (schedulePageViewModel.selectedDay == dayIndex) UISingleton.color1.oppositeColor else Color(TRANSPARENT), CircleShape)
                            .clip(CircleShape)
                            .clickable(
                                onClick = {
                                    schedulePageViewModel.selectDay(dayIndex)
                                }
                            )
                            .padding(horizontal = 16.dp, 12.dp)
                    )
//                    Button(
//                        colors = ButtonDefaults.buttonColors(
//                            contentColor = UISingleton.color4.oppositeColor,
//                            containerColor = if (schedulePageViewModel.selectedDay == dayIndex) UISingleton.color1.oppositeColor else Color(TRANSPARENT)
//                        ),
//                        onClick = {
//                            schedulePageViewModel.selectDay(dayIndex)
//                        }
//                    ) {
//                        Text(
//                            text = daysOfWeek[dayIndex],
//                            style = MaterialTheme.typography.titleMedium
//                        )
//                    }
                }
            }
        }
    }
}