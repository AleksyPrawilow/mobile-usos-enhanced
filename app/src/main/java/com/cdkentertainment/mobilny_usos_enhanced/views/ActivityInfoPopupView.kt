package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.PeopleSingleton
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.models.Lesson
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LecturerRatesPageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.SchedulePageViewModel

@Composable
fun ActivityInfoPopupView(
    data: Lesson,
    onDismissRequest: () -> Unit
) {
    val context: Context = LocalContext.current
    val viewModel: SchedulePageViewModel = viewModel<SchedulePageViewModel>()
    val lecturersViewModel: LecturerRatesPageViewModel = viewModel<LecturerRatesPageViewModel>()
    val classType: String = data.classtype_id
    val address: String = data.building_name.getLocalized(context)
    val room: String = data.room_number
    val time: String = "${viewModel.getTimeFromDate(data.start_time)} - ${viewModel.getTimeFromDate(data.end_time)}"
    var lecturersFetched: Boolean by rememberSaveable { mutableStateOf(false) }
    var lecturersFetchError: Boolean by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        lecturersViewModel.getLecturersRatings(
            lecturerIds = data.lecturer_ids.map { it.toString() },
            onSuccess = {
                lecturersFetched = true
                lecturersFetchError = false
            },
            onError = {
                lecturersFetchError = true
                lecturersFetched = false
            }
        )
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
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
                PopupHeaderView(
                    title = data.course_name.getLocalized(context)
                )
                GroupedContentContainerView(
                    title = stringResource(R.string.subject),
                    backgroundColor = UISingleton.color1,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 0.dp)
                ) {
                    TextAndIconCardView(
                        title = UIHelper.classTypeIds[classType]?.name?.getLocalized(context) ?: classType,
                        icon = ImageVector.vectorResource(UIHelper.activityTypeIconMapping[classType] ?: UIHelper.otherIcon),
                        iconSize = 40.dp,
                        iconPadding = 6.dp,
                        elevation = 0.dp
                    )
                }
                AnimatedVisibility(
                    visible = lecturersFetched && !lecturersFetchError,
                    enter = UIHelper.slideEnterTransition(1)
                ) {
                    GroupedContentContainerView(
                        title = stringResource(R.string.lecturers),
                        backgroundColor = UISingleton.color1,
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 0.dp)
                    ) {
                        for (lecturer in data.lecturer_ids) {
                            GroupLecturerCardView(
                                lecturer = PeopleSingleton.lecturers[lecturer.toString()]!!
                            )
                        }
                    }
                }
                AnimatedVisibility(
                    visible = lecturersFetchError,
                    enter = UIHelper.slideEnterTransition(1)
                ) {
                    TextAndIconCardView(
                        title = stringResource(R.string.failed_to_fetch),
                        icon = Icons.Rounded.Refresh,
                        iconSize = 40.dp,
                        iconPadding = 6.dp,
                    )
                }
                GroupedContentContainerView(
                    title = stringResource(R.string.information),
                    backgroundColor = UISingleton.color1,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 0.dp)
                ) {
                    val categories: Map<String, Pair<ImageVector, String>> = mapOf(
                        "Time" to (ImageVector.vectorResource(R.drawable.rounded_alarm_24) to time),
                        "Address" to (ImageVector.vectorResource(R.drawable.rounded_location_on_24) to address),
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
                Spacer(modifier = Modifier.height(12.dp))
            }
            DismissPopupButtonView(onDismissRequest = onDismissRequest, modifier = Modifier.align(Alignment.TopEnd))
        }
    }
}