package com.cdkentertainment.muniversity.views

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.muniversity.R
import com.cdkentertainment.muniversity.UIHelper
import com.cdkentertainment.muniversity.UISingleton
import com.cdkentertainment.muniversity.getLocalized
import com.cdkentertainment.muniversity.view_models.AttendancePageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AttendancePopupView(
    onDismissRequest: () -> Unit,
    onRemovePin: () -> Unit
) {
    var error: Boolean by rememberSaveable { mutableStateOf(false) }
    var loading: Boolean by rememberSaveable { mutableStateOf(false) }
    var loaded: Boolean by rememberSaveable { mutableStateOf(false) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val viewModel: AttendancePageViewModel = viewModel<AttendancePageViewModel>()
    val context: Context = LocalContext.current
    val classType: String? = viewModel.popupData?.classGroupData?.class_type_id
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

    val loadAttendanceDates: () -> Unit = {
        coroutineScope.launch {
            if (loaded) {
                return@launch
            }
            loading = true
            loaded = false
            error = false
            try {
                delay(1000)
                //todo loading
                error = false
                loading = false
                loaded = true
            } catch (e: Exception) {
                error = true
                loaded = false
                loading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadAttendanceDates()
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
                        TextAndIconCardView(
                            title = UIHelper.classTypeIds[classType]?.name?.getLocalized(context) ?: (classType ?: "N/A"),
                            icon = ImageVector.vectorResource(UIHelper.activityTypeIconMapping[classType] ?: UIHelper.otherIcon),
                            iconSize = 40.dp,
                            iconPadding = 6.dp,
                            elevation = 0.dp
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
                        AnimatedVisibility(
                            visible = loading,
                            enter = UIHelper.slideEnterTransition(1)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth().padding(12.dp)
                            ) {
                                CircularProgressIndicator(color = UISingleton.textColor2)
                            }
                        }
                        AnimatedVisibility(
                            visible = loaded,
                            enter = UIHelper.slideEnterTransition(1)
                        ) {
                            TextAndIconCardView(
                                title = "All meetings are up to date",
                                icon = Icons.Rounded.Done,
                                backgroundColor = UISingleton.color2,
                                showArrow = false,
                                elevation = 0.dp
                            )
                        }
                    }
                }
                item {
                    AnimatedVisibility(
                        visible = loading,
                        enter = UIHelper.slideEnterTransition(1)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth().padding(12.dp)
                        ) {
                            CircularProgressIndicator(color = UISingleton.textColor2)
                        }
                    }
                }
                item {
                    AnimatedVisibility(
                        visible = error,
                        enter = UIHelper.slideEnterTransition(1)
                    ) {
                        TextAndIconCardView(
                            title = stringResource(R.string.failed_to_fetch),
                            icon = Icons.Rounded.Refresh,
                            iconSize = 40.dp,
                            iconPadding = 6.dp,
                            backgroundColor = UISingleton.color1,
                            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                        ) {
                            loadAttendanceDates()
                        }
                    }
                }
                if (loaded) {
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
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            DismissPopupButtonView(onDismissRequest = onDismissRequest, modifier = Modifier.align(Alignment.TopEnd))
        }
    }
}