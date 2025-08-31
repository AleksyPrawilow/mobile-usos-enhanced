package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LessonGroupPageViewModel

@Composable
fun ClassGroupView(
    data: LessonGroup,
) {
    val viewModel: LessonGroupPageViewModel = viewModel<LessonGroupPageViewModel>()
    val groupKey: String = "${data.course_unit_id}-${data.group_number}"
    val onClick: () -> Unit = {
        viewModel.groupDetails[groupKey]?.showDetails = true
    }
    val onDismissRequest: () -> Unit = {
        viewModel.groupDetails[groupKey]?.showDetails = false
    }
    if (viewModel.groupDetails[groupKey]?.showDetails == true) {
        ClassGroupPopupView(
            data = data,
            viewModel = viewModel,
            groupKey = groupKey,
            onDismissRequest = onDismissRequest
        )
    }
    ClassGroupCardView(
        data = data,
        onClick = onClick
    )
}