package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.view_models.AttendancePageViewModel

@Composable
fun AttendancePopupView(
    viewModel: AttendancePageViewModel,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .shadow(10.dp, shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .background(UISingleton.color2.primaryColor, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
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
                        text = "${viewModel.popupData?.classGroupData?.course_name?.pl ?: "N/A"} - ${viewModel.popupData?.classGroupData?.class_type_id ?: "N/A"}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = UISingleton.color4.primaryColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                item {
                    Text(
                        text = "Grupa ${viewModel.popupData?.classGroupData?.group_number ?: "N/A"}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = UISingleton.color3.primaryColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
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
                                text = "Obecność",
                                color = UISingleton.color4.primaryColor,
                                style = MaterialTheme.typography.titleLarge
                            )
                            HorizontalDivider(
                                thickness = 5.dp,
                                color = UISingleton.color3.primaryColor,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                            )
                            AttendanceStatCardView("Frekwencja", "100%")
                            AttendanceStatCardView("Nieuspr. nieobecności", "0")
                        }
                    }
                }
                if (true) {
                    item {
                        Text(
                            text = "Spotkania",
                            color = UISingleton.color4.primaryColor,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                                .background(UISingleton.color1.primaryColor, RoundedCornerShape(topStart = UISingleton.uiElementsCornerRadius.dp, topEnd = UISingleton.uiElementsCornerRadius.dp, 0.dp, 0.dp))
                                .padding(12.dp)
                                .animateItem()
                        )
                    }
                    item {
                        HorizontalDivider(
                            thickness = 5.dp,
                            color = UISingleton.color3.primaryColor,
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .background(UISingleton.color1.primaryColor)
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
                                .background(UISingleton.color1.primaryColor)
                        )
                    }

                    items(5, key = { it }) { index ->
                        AttendanceDateCardView(
                            index = index,
                            date = "12 Października 2025",
                            viewModel = viewModel,
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
                                color = UISingleton.color3.primaryColor,
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