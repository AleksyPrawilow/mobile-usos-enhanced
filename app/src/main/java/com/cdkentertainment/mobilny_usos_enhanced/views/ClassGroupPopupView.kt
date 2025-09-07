package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LessonGroupPageViewModel

@Composable
fun ClassGroupPopupView(
    data: LessonGroup,
    viewModel: LessonGroupPageViewModel,
    groupKey: String,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        LaunchedEffect(Unit) {
            if (viewModel.groupDetails[groupKey]?.participants == null) {
                viewModel.groupDetails[groupKey]?.participants = viewModel.fetchParticipants(data.group_number.toString(), data.course_unit_id.toString())
            }
        }
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .shadow(10.dp, shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .background(UISingleton.color2, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .border(5.dp, UISingleton.color1, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    Spacer(modifier = Modifier.height(48.dp))
                }
                item {
                    Text(
                        text = "${data.course_name.pl} - ${data.class_type_id}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = UISingleton.textColor1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                item {
                    Text(
                        text = "Grupa ${data.group_number}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = UISingleton.textColor2,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                    )
                }
                item {
                    Card(
                        colors = CardColors(
                            contentColor = UISingleton.textColor1,
                            containerColor = UISingleton.color1,
                            disabledContainerColor = UISingleton.color1,
                            disabledContentColor = UISingleton.textColor1
                        ),
                        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Prowadzący",
                                color = UISingleton.textColor1,
                                style = MaterialTheme.typography.titleLarge
                            )
                            HorizontalDivider(
                                thickness = 5.dp,
                                color = UISingleton.textColor2,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                            )
                            for (lecturer in 0 until data.lecturers.size) {
                                //TODO: Can this be replaced with GroupParticipantCardView.kt?
                                //The answer is NO, it cannot!
                                GroupLecturerCardView(data.lecturers[lecturer])
                            }
                        }
                    }
                }
                if (viewModel.groupDetails[groupKey]?.participants != null) {
                    val participantsSize: Int = viewModel.groupDetails[groupKey]?.participants!!.participants.size
                    item {
                        Text(
                            text = "Uczestnicy",
                            color = UISingleton.textColor1,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                                .background(UISingleton.color1, RoundedCornerShape(topStart = UISingleton.uiElementsCornerRadius.dp, topEnd = UISingleton.uiElementsCornerRadius.dp, 0.dp, 0.dp))
                                .padding(12.dp)
                                .animateItem()
                        )
                    }
                    item {
                        HorizontalDivider(
                            thickness = 5.dp,
                            color = UISingleton.textColor2,
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .background(UISingleton.color1)
                                .padding(horizontal = 12.dp)
                                .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                        )
                    }
                    item {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .padding(horizontal = 12.dp)
                                .background(UISingleton.color1)
                        )
                    }

                    items(participantsSize, key = { index -> viewModel.groupDetails[groupKey]!!.participants!!.participants[index].id }) { index ->
                        GroupParticipantCardView(
                            index = index,
                            participant = viewModel.groupDetails[groupKey]!!.participants!!.participants[index],
                            viewModel = viewModel,
                            participantsSize = participantsSize,
                            modifier = Modifier.animateItem()
                        )
                    }
                } else {
                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(0.dp, alignment = Alignment.CenterHorizontally),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                color = UISingleton.textColor2,
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            DismissPopupButtonView(onDismissRequest = onDismissRequest, modifier = Modifier.align(Alignment.TopEnd))
        }
    }
}