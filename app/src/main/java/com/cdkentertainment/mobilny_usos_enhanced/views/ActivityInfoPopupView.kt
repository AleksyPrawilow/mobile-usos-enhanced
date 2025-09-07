package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Lesson
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import com.cdkentertainment.mobilny_usos_enhanced.view_models.SchedulePageViewModel

@Composable
fun ActivityInfoPopupView(
    data: Lesson,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        val viewModel: SchedulePageViewModel = viewModel<SchedulePageViewModel>()
        val address: String = data.building_name.pl
        val room: String = "Sala: ${data.room_number}"
        val time: String = "${viewModel.getTimeFromDate(data.start_time)} - ${viewModel.getTimeFromDate(data.end_time)}"

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                //.fillMaxSize()
                .fillMaxWidth()
                .shadow(10.dp, shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .background(UISingleton.color2, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .border(5.dp, UISingleton.color1, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = data.course_name.pl,
                    style = MaterialTheme.typography.headlineSmall,
                    color = UISingleton.textColor1,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
                Text(
                    text = data.classtype_id,
                    style = MaterialTheme.typography.titleLarge,
                    color = UISingleton.textColor2,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                        .background(UISingleton.color1, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                        .padding(12.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Prowadzący",
                            style = MaterialTheme.typography.titleLarge,
                            color = UISingleton.textColor1
                        )
                        HorizontalDivider(
                            thickness = 5.dp,
                            color = UISingleton.textColor2,
                            modifier = Modifier
                                .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                        )
                        repeat(2) { index ->
                            GroupLecturerCardView(SharedDataClasses.Human(index.toString(), "Jan", "Kowalski"))
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                        .background(UISingleton.color1, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                        .padding(12.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Informacja",
                            style = MaterialTheme.typography.titleLarge,
                            color = UISingleton.textColor1
                        )
                        HorizontalDivider(
                            thickness = 5.dp,
                            color = UISingleton.textColor2,
                            modifier = Modifier
                                .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                        )
                        val categories: Map<String, Pair<ImageVector, String>> = mapOf(
                            "Time" to (ImageVector.vectorResource(R.drawable.rounded_alarm_24) to time),
                            "Address" to (Icons.Rounded.LocationOn to address),
                            "Room" to (ImageVector.vectorResource(R.drawable.rounded_door_open_24) to room)
                        )
                        for ((category, iconAndText) in categories) {
                            if (iconAndText.second.isEmpty()) continue
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(UISingleton.color2, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                                    .padding(12.dp)
                            ) {
                                Icon(
                                    imageVector = iconAndText.first,
                                    contentDescription = category,
                                    tint = UISingleton.textColor4,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(UISingleton.color3, CircleShape)
                                        .padding(12.dp)
                                )
                                Text(
                                    text = iconAndText.second,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = UISingleton.textColor1
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            DismissPopupButtonView(onDismissRequest = onDismissRequest, modifier = Modifier.align(Alignment.TopEnd))
        }
    }
}