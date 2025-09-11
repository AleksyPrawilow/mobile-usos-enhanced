package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup
import com.cdkentertainment.mobilny_usos_enhanced.view_models.AttendancePageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AttendanceClassGroupView(
    data: LessonGroup,
) {
    val viewModel: AttendancePageViewModel = viewModel<AttendancePageViewModel>()
    val context: Context = LocalContext.current
    val onClick: () -> Unit = {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.showPopup(data, context)
        }
    }
    GradeCardView(
        courseName = UIHelper.classTypeIds[data.class_type_id]?.name?.getLocalized(context) ?: data.class_type_id,
        grade = data.group_number.toString(),
        showArrow = true,
        onClick = onClick
    )
}