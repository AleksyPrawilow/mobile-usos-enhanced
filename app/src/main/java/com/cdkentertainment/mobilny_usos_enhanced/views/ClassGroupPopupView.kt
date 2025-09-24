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
import androidx.compose.runtime.LaunchedEffect
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
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LessonGroupPageViewModel

@Composable
fun ClassGroupPopupView(
    data: LessonGroup,
    groupKey: String,
    onDismissRequest: () -> Unit
) {
    val context: Context = LocalContext.current
    val viewModel: LessonGroupPageViewModel = viewModel<LessonGroupPageViewModel>()
    val classType: String = data.class_type_id
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
                    GroupedContentContainerView(
                        title = stringResource(R.string.lecturers),
                        backgroundColor = UISingleton.color1,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        for (lecturer in 0 until data.lecturers.size) {
                            //Can this be replaced with GroupParticipantCardView.kt?
                            //The answer is NO, it cannot!
                            GroupLecturerCardView(data.lecturers[lecturer])
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
                                .padding(horizontal = 12.dp)
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