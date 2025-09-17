package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup

// TODO: Remove this
@Composable
fun AttendanceClassGroupView(
    data: LessonGroup,
    onClick: () -> Unit = {}
) {
    val context: Context = LocalContext.current
    GradeCardView(
        courseName = UIHelper.classTypeIds[data.class_type_id]?.name?.getLocalized(context) ?: data.class_type_id,
        grade = "100%",
        showArrow = true,
        showGrade = true,
        onClick = onClick
    )
}