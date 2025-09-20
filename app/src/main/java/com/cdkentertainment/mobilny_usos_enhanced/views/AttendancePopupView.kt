package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.view_models.AttendancePageViewModel

@Composable
fun AttendancePopupView(
    viewModel: AttendancePageViewModel,
    onDismissRequest: () -> Unit,
    onRemovePin: () -> Unit
) {
    val context: Context = LocalContext.current
    var showRemoveDialog: Boolean by rememberSaveable { mutableStateOf(false) }
    if (showRemoveDialog) {
        ConfirmDialogPopupView(
            title = stringResource(R.string.unpin_confirm),
            confirmTitle = stringResource(R.string.yes),
            dismissTitle = stringResource(R.string.no),
            description = stringResource(R.string.unpin_description),
            onConfirm = {
                showRemoveDialog = false
                onRemovePin()
            },
            onDismiss = { showRemoveDialog = false }
        )
    }
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
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
                    PopupHeaderView(viewModel.popupData?.classGroupData?.course_name?.getLocalized(context) ?: "N/A")
                }
                item {
                    GroupedContentContainerView(
                        title = stringResource(R.string.subject),
                        backgroundColor = UISingleton.color1,
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp)
                    ) {
                        GradeCardView(
                            courseName = stringResource(R.string.group),
                            grade = "${viewModel.popupData?.classGroupData?.group_number ?: "N/A"}",
                            showArrow = false,
                            backgroundColor = UISingleton.color2
                        )
                        GradeCardView(
                            courseName = UIHelper.classTypeIds[viewModel.popupData?.classGroupData?.class_type_id]?.name?.getLocalized(context) ?: "N/A",
                            showArrow = false,
                            showGrade = false,
                            backgroundColor = UISingleton.color2
                        )
                    }
                }
                item {
                    GroupedContentContainerView(
                        title = stringResource(R.string.attendance),
                        backgroundColor = UISingleton.color1,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        AttendanceStatCardView(stringResource(R.string.frequency), "100%")
                        AttendanceStatCardView(stringResource(R.string.absences), "0")
                    }
                }
                if (true) {
                    item {
                        Text(
                            text = stringResource(R.string.meetings),
                            color = UISingleton.textColor1,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                                .shadow(3.dp, RoundedCornerShape(topStart = UISingleton.uiElementsCornerRadius.dp, topEnd = UISingleton.uiElementsCornerRadius.dp, 0.dp, 0.dp))
                                .background(UISingleton.color1, RoundedCornerShape(topStart = UISingleton.uiElementsCornerRadius.dp, topEnd = UISingleton.uiElementsCornerRadius.dp, 0.dp, 0.dp))
                                .padding(12.dp)
                                .animateItem()
                        )
                    }

                    items(5, key = { it }) { index ->
                        AttendanceDateCardView(
                            index = index,
                            date = "12 Października 2025",
                            maxIndex = 5,
                            modifier = Modifier.animateItem()
                        )
                    }
                    item {
                        TextAndIconCardView(
                            title = stringResource(R.string.remove_pin),
                            icon = ImageVector.vectorResource(R.drawable.rounded_bookmark_remove_24),
                            modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp),
                            backgroundColor = UISingleton.color1
                        ) {
                            showRemoveDialog = true
                        }
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