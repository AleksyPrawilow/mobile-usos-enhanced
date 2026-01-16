package com.cdkentertainment.bux.views

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.bux.UIHelper
import com.cdkentertainment.bux.getLocalized
import com.cdkentertainment.bux.models.LessonGroup
import com.cdkentertainment.bux.view_models.LessonGroupPageViewModel

@Composable
fun ClassGroupView(
    data: LessonGroup,
) {
    val context: Context = LocalContext.current
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
            groupKey = groupKey,
            onDismissRequest = onDismissRequest
        )
    }
    GradeCardView(
        courseName = UIHelper.classTypeIds[data.class_type_id]?.name?.getLocalized(context) ?: data.class_type_id,
        grade = data.group_number.toString(),
        showArrow = true,
        onClick = onClick,
        sideIcon = ImageVector.vectorResource(UIHelper.activityTypeIconMapping[data.class_type_id] ?: UIHelper.otherIcon)
    )
}