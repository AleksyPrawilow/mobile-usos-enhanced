package com.cdkentertainment.bux.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.bux.R
import com.cdkentertainment.bux.UISingleton
import com.cdkentertainment.bux.view_models.SchedulePageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDaySelectorView(
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier,
    onDaySelected: (Int) -> Unit = {}
) {
    val daysOfWeek: List<String> = listOf(
        "pn",
        "wt",
        "śr",
        "cz",
        "pt"
    )
    val schedulePageViewModel: SchedulePageViewModel = viewModel<SchedulePageViewModel>()
    val datePickerState = rememberDatePickerState(Instant.now().toEpochMilli())

    LaunchedEffect(Unit) {
        schedulePageViewModel.selectDay(min(LocalDate.now().dayOfWeek.value - 1, 4))
    }

    if (schedulePageViewModel.displayDateSelector) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors(
                containerColor = UISingleton.color3,
            ),
            shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
            onDismissRequest = { },
            confirmButton = {
                TextButton(
                    onClick = {
                        val localDate = datePickerState.selectedDateMillis?.let {
                            Instant.ofEpochMilli(it)
                                .atZone(ZoneId.of("Europe/Warsaw"))
                                .toLocalDate()
                        }
                        if (localDate != null) {
                            CoroutineScope(Dispatchers.IO).launch {
                                schedulePageViewModel.resetSchedule()
                                schedulePageViewModel.fetchWeekData(localDate)
                                schedulePageViewModel.selectDay(min(localDate.dayOfWeek.value - 1, 4))
                            }
                        }
                        schedulePageViewModel.setDateSelectorVisibility(false)
                    }
                ) {
                    Text(
                        text = "Ok",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = UISingleton.textColor4
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        schedulePageViewModel.setDateSelectorVisibility(false)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = UISingleton.textColor4
                    )
                }
            },
            modifier = Modifier
                .shadow(3.dp, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .border(5.dp, UISingleton.color1, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = UISingleton.color3,
                    dayInSelectionRangeContainerColor = UISingleton.color3,
                    disabledSelectedDayContainerColor = UISingleton.color3,
                    containerColor = UISingleton.color2,
                    titleContentColor = UISingleton.textColor1,
                    headlineContentColor = UISingleton.textColor1,
                    weekdayContentColor = UISingleton.textColor1,
                    subheadContentColor = UISingleton.textColor1,
                    todayDateBorderColor = UISingleton.textColor1,
                    dayContentColor = UISingleton.textColor1,
                    selectedDayContentColor = UISingleton.textColor4,
                    todayContentColor = UISingleton.textColor1,
                    currentYearContentColor = UISingleton.textColor1,
                    yearContentColor = UISingleton.textColor1,
                    navigationContentColor = UISingleton.textColor1,
                    selectedYearContentColor = UISingleton.textColor4,
                    selectedYearContainerColor = UISingleton.color3,
                    dividerColor = UISingleton.color1
                ),
                showModeToggle = false,

                modifier = Modifier.verticalScroll(rememberScrollState())
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .then(modifier)
            .shadow(5.dp, RoundedCornerShape(UISingleton.uiElementsCornerRadius))
            .background(
                UISingleton.color2,
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
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
            ) {
                for (weekOptionIndex in 0..1) {
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = weekOptionIndex,
                            count = 2,
                        ),
                        onClick = {
                            if (schedulePageViewModel.loading) {
                                return@SegmentedButton
                            }
                            if (weekOptionIndex == 0) {
                                if (schedulePageViewModel.selectedWeekOption != weekOptionIndex) {
                                    val dayIndex: Int = min(LocalDate.now().dayOfWeek.value - 1, 4)
                                    schedulePageViewModel.resetSchedule()
                                    schedulePageViewModel.selectDay(dayIndex)
                                    coroutineScope.launch {
                                        withContext(Dispatchers.IO) {
                                            schedulePageViewModel.fetchWeekData(LocalDate.now())
                                            onDaySelected(dayIndex)
                                        }
                                    }
                                }
                            } else {
                                schedulePageViewModel.setDateSelectorVisibility(true)
                            }
                            schedulePageViewModel.selectWeekOption(weekOptionIndex)
                        },
                        colors = SegmentedButtonDefaults.colors(
                            activeContentColor = UISingleton.textColor4,
                            activeContainerColor = UISingleton.color3,
                            activeBorderColor = UISingleton.color3,
                            inactiveContentColor = UISingleton.textColor1,
                            inactiveContainerColor = UISingleton.color1,
                            inactiveBorderColor = UISingleton.color1
                        ),
                        selected = weekOptionIndex == schedulePageViewModel.selectedWeekOption,
                    ) {
                        Text(
                            text = if (weekOptionIndex == 0) "Ten tydzień" else "Inny tydzień",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            SingleChoiceSegmentedButtonRow {
                daysOfWeek.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = daysOfWeek.size,
                        ),
                        onClick = {
                            schedulePageViewModel.selectDay(index)
                            onDaySelected(index)
                        },
                        colors = SegmentedButtonDefaults.colors(
                            activeContentColor = UISingleton.textColor4,
                            activeContainerColor = UISingleton.color3,
                            activeBorderColor = UISingleton.color3,
                            inactiveContentColor = UISingleton.textColor1,
                            inactiveContainerColor = UISingleton.color1,
                            inactiveBorderColor = UISingleton.color1
                        ),
                        selected = index == schedulePageViewModel.selectedDay,
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
        }
    }
}