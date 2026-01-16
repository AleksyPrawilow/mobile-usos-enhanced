package com.cdkentertainment.bux.views

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
import com.cdkentertainment.bux.R
import com.cdkentertainment.bux.UIHelper
import com.cdkentertainment.bux.UISingleton
import com.cdkentertainment.bux.getLocalized
import com.cdkentertainment.bux.models.AttendanceDatesObject
import com.cdkentertainment.bux.view_models.AttendancePageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun UnpinnedCoursePopupView(
    onDismissRequest: () -> Unit,
    onAddPin: () -> Unit
) {
    var loading: Boolean by rememberSaveable { mutableStateOf(false) }
    var loaded: Boolean by rememberSaveable { mutableStateOf(false) }
    var error: Boolean by rememberSaveable { mutableStateOf(false) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val viewModel: AttendancePageViewModel = viewModel<AttendancePageViewModel>()
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    val context: Context = LocalContext.current
    val classType: String? = viewModel.popupData?.classGroupData?.class_type_id
    var showPinDialog: Boolean by rememberSaveable { mutableStateOf(false) }
    val meetings: List<AttendanceDatesObject>? = viewModel.unitMeetings[viewModel.popupData?.classGroupData?.course_unit_id.toString()]

    val loadMeetingsData: () -> Unit = {
        coroutineScope.launch {
            try {
                loading = true
                loaded = false
                error = false
                viewModel.readAllCourseMeetings(viewModel.popupData!!.classGroupData)
                loading = false
                error = false
                loaded = true
            } catch (e: Exception) {
                loading = false
                loaded = false
                error = true
            }
        }
    }

    LaunchedEffect(Unit) {
        loadMeetingsData()
    }

    if (showPinDialog) {
        ConfirmDialogPopupView(
            title = stringResource(R.string.pin_confirm),
            confirmTitle = stringResource(R.string.yes),
            dismissTitle = stringResource(R.string.no),
            onConfirm = {
                showPinDialog = false
                onAddPin()
            },
            onDismiss = { showPinDialog = false }
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
                    AnimatedVisibility(
                        visible = loaded,
                        enter = UIHelper.slideEnterTransition(1)
                    ) {
                        TextAndIconCardView(
                            title = stringResource(R.string.add_pin),
                            icon = ImageVector.vectorResource(R.drawable.rounded_pin_24),
                            modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp),
                            backgroundColor = UISingleton.color1
                        ) {
                            showPinDialog = true
                        }
                    }
                }
                item {
                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                }
                if (loaded && meetings != null) {
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
                    items(meetings.size, key = { it }) { index ->
                        AttendanceDateCardView(
                            index = index,
                            maxIndex = meetings.size,
                            date = meetings[index].startDateTime.toLocalDate().format(formatter),
                            icon = ImageVector.vectorResource(R.drawable.rounded_calendar_month_24),
                            enabled = false,
                            modifier = Modifier.animateItem()
                        )
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
                            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 0.dp)
                        ) {
                            loadMeetingsData()
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