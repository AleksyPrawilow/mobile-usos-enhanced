package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LessonGroupPageViewModel

@Composable
fun ClassGroupView(
    data: LessonGroup,
    viewModel: LessonGroupPageViewModel
) {
    val groupKey: String = "${data.course_unit_id}-${data.group_number}"
    val onClick: () -> Unit = {
        viewModel.groupDetails[groupKey]?.showDetails = true
    }
    val onDismissRequest: () -> Unit = {
        viewModel.groupDetails[groupKey]?.showDetails = false
    }
    if (viewModel.groupDetails[groupKey]?.showDetails == true) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            LaunchedEffect(Unit) {
                if (viewModel.groupDetails[groupKey]?.participants == null) {
                    viewModel.groupDetails[groupKey]?.participants = viewModel.fetchParticipants(data.group_number.toString(), data.course_unit_id.toString())
                }
            }
            Card(
                colors = CardColors(
                    contentColor = UISingleton.color4.primaryColor,
                    containerColor = UISingleton.color2.primaryColor,
                    disabledContainerColor = UISingleton.color2.primaryColor,
                    disabledContentColor = UISingleton.color4.primaryColor
                ),
                shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(10.dp, shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding()
                ) {
                    item {
                        Text(
                            text = "${data.course_name.pl} - ${data.class_type_id}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = UISingleton.color4.primaryColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        )
                    }
                    item {
                        Text(
                            text = "Grupa ${data.group_number}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = UISingleton.color3.primaryColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        )
                    }
                    item {
                        Card(
                            colors = CardColors(
                                contentColor = UISingleton.color4.primaryColor,
                                containerColor = UISingleton.color1.primaryColor,
                                disabledContainerColor = UISingleton.color1.primaryColor,
                                disabledContentColor = UISingleton.color4.primaryColor
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
                                    color = UISingleton.color4.primaryColor,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                HorizontalDivider(
                                    thickness = 5.dp,
                                    color = UISingleton.color3.primaryColor,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                                )
                                for (lecturer in data.lecturers) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                UISingleton.color2.primaryColor,
                                                RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
                                            )
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = "${lecturer.first_name} ${lecturer.last_name}",
                                            color = UISingleton.color4.primaryColor,
                                            style = MaterialTheme.typography.titleLarge,
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (viewModel.groupDetails[groupKey]?.participants != null) {
                        items(viewModel.groupDetails[groupKey]?.participants!!.participants.size, key = { index -> viewModel.groupDetails[groupKey]!!.participants!!.participants[index].id }) { index ->
                            Card(
                                colors = CardColors(
                                    contentColor = UISingleton.color4.primaryColor,
                                    containerColor = UISingleton.color1.primaryColor,
                                    disabledContainerColor = UISingleton.color1.primaryColor,
                                    disabledContentColor = UISingleton.color4.primaryColor
                                ),
                                shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                                    .animateItem()
                            ) {
                                Text(
                                    text = "${viewModel.groupDetails[groupKey]!!.participants!!.participants[index].first_name} ${viewModel.groupDetails[groupKey]!!.participants!!.participants[index].last_name}",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = UISingleton.color4.primaryColor,
                                    modifier = Modifier
                                        .padding(12.dp)
                                )
                            }
                        }
                    } else {
                        item {
                            CircularProgressIndicator(
                                color = UISingleton.color3.primaryColor,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                    item {
                        Button(
                            onClick = onDismissRequest
                        ) {
                            Text(
                                text = "Dismiss"
                            )
                        }
                    }
                }
            }
        }
    }

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
            .clickable(onClick = onClick)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = data.course_name.pl,
                color = UISingleton.color4.primaryColor,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            HorizontalDivider(
                thickness = 5.dp,
                color = UISingleton.color3.primaryColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            )
            Text(
                text = "${data.class_type_id}, grupa ${data.group_number}",
                style = MaterialTheme.typography.titleMedium,
                color = UISingleton.color3.primaryColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}