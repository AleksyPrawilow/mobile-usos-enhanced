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
import com.cdkentertainment.muniversity.Lecturer
import com.cdkentertainment.muniversity.PeopleSingleton
import com.cdkentertainment.muniversity.R
import com.cdkentertainment.muniversity.UIHelper
import com.cdkentertainment.muniversity.UISingleton
import com.cdkentertainment.muniversity.getLocalized
import com.cdkentertainment.muniversity.models.LessonGroup
import com.cdkentertainment.muniversity.view_models.LecturerRatesPageViewModel
import com.cdkentertainment.muniversity.view_models.LessonGroupPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ClassGroupPopupView(
    data: LessonGroup,
    groupKey: String,
    onDismissRequest: () -> Unit
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    val viewModel: LessonGroupPageViewModel = viewModel<LessonGroupPageViewModel>()
    val lecturersViewModel: LecturerRatesPageViewModel = viewModel<LecturerRatesPageViewModel>()
    val classType: String = data.class_type_id
    var lecturersFetched: Boolean by rememberSaveable { mutableStateOf(false) }
    var lecturersFetchError: Boolean by rememberSaveable { mutableStateOf(false) }
    var participantsFetching: Boolean by rememberSaveable { mutableStateOf(false) }
    var participantsFetchError: Boolean by rememberSaveable { mutableStateOf(false) }
    val loadLecturersInfo: () -> Unit = {
        lecturersFetched = false
        lecturersFetchError = false
        lecturersViewModel.getLecturersRatings(
            lecturerIds = data.lecturers.map { it.id.toString() },
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
    val loadParticipantsInfo: () -> Unit = {
        coroutineScope.launch {
            try {
                if (viewModel.groupDetails[groupKey]?.participants == null) {
                    participantsFetching = true
                    participantsFetchError = false
                    viewModel.groupDetails[groupKey]?.participants = viewModel.fetchParticipants(data.group_number.toString(), data.course_unit_id.toString())
                    participantsFetching = false
                    participantsFetchError = false
                }
            } catch (e: Exception) {
                participantsFetching = false
                participantsFetchError = true
            }
        }
    }

    LaunchedEffect(Unit) {
        loadLecturersInfo()
        loadParticipantsInfo()
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
                    PopupHeaderView(data.course_name.getLocalized(context))
                }
                item {
                    GroupedContentContainerView(
                        title = stringResource(R.string.subject),
                        backgroundColor = UISingleton.color1,
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp)
                    ) {
                        GradeCardView(
                            courseName = stringResource(R.string.group),
                            grade = "${data.group_number}",
                            showArrow = false,
                            backgroundColor = UISingleton.color2
                        )
                        TextAndIconCardView(
                            title = UIHelper.classTypeIds[classType]?.name?.getLocalized(context) ?: classType,
                            icon = ImageVector.vectorResource(UIHelper.activityTypeIconMapping[classType] ?: UIHelper.otherIcon),
                            iconSize = 40.dp,
                            iconPadding = 6.dp,
                            elevation = 0.dp
                        )
                    }
                }
                item {
                    AnimatedVisibility(
                        visible = lecturersFetched && !lecturersFetchError,
                        enter = UIHelper.slideEnterTransition(1)
                    ) {
                        GroupedContentContainerView(
                            title = stringResource(R.string.lecturers),
                            backgroundColor = UISingleton.color1,
                            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp)
                        ) {
                            for (lecturer in data.lecturers) {
                                val lecturerData: Lecturer? = PeopleSingleton.lecturers[lecturer.id]
                                if (lecturerData != null) {
                                    GroupLecturerCardView(
                                        lecturer = PeopleSingleton.lecturers[lecturer.id]!!
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    AnimatedVisibility(
                        visible = !lecturersFetched && !lecturersFetchError,
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
                        visible = lecturersFetchError,
                        enter = UIHelper.slideEnterTransition(1)
                    ) {
                        TextAndIconCardView(
                            title = stringResource(R.string.failed_to_fetch),
                            icon = Icons.Rounded.Refresh,
                            iconSize = 40.dp,
                            iconPadding = 6.dp,
                            backgroundColor = UISingleton.color1,
                            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp)
                        ) {
                            loadLecturersInfo()
                        }
                    }
                }
                if (viewModel.groupDetails[groupKey]?.participants != null) {
                    val participantsSize: Int = viewModel.groupDetails[groupKey]?.participants!!.participants.size
                    item {
                        Text(
                            text = stringResource(R.string.participants),
                            color = UISingleton.textColor1,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                                .shadow(3.dp, RoundedCornerShape(topStart = UISingleton.uiElementsCornerRadius.dp, topEnd = UISingleton.uiElementsCornerRadius.dp, 0.dp, 0.dp))
                                .background(UISingleton.color1, RoundedCornerShape(topStart = UISingleton.uiElementsCornerRadius.dp, topEnd = UISingleton.uiElementsCornerRadius.dp, 0.dp, 0.dp))
                                .padding(12.dp)
                                .animateItem()
                        )
                    }
                    items(participantsSize, key = { index -> viewModel.groupDetails[groupKey]!!.participants!!.participants[index].id }) { index ->
                        GroupParticipantCardView(
                            index = index,
                            participant = viewModel.groupDetails[groupKey]!!.participants!!.participants[index],
                            participantsSize = participantsSize,
                            modifier = Modifier.animateItem()
                        )
                    }
                }
                item {
                    AnimatedVisibility(
                        visible = participantsFetching && !participantsFetchError,
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
                        visible = participantsFetchError,
                        enter = UIHelper.slideEnterTransition(1)
                    ) {
                        TextAndIconCardView(
                            title = stringResource(R.string.failed_to_fetch),
                            icon = Icons.Rounded.Refresh,
                            iconSize = 40.dp,
                            iconPadding = 6.dp,
                            backgroundColor = UISingleton.color1,
                            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp)
                        ) {
                            loadParticipantsInfo()
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