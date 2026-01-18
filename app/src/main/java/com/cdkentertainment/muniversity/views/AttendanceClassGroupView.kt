package com.cdkentertainment.muniversity.views

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import com.cdkentertainment.muniversity.UIHelper
import com.cdkentertainment.muniversity.getLocalized
import com.cdkentertainment.muniversity.models.LessonGroup

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
        onClick = onClick,
        sideIcon = ImageVector.vectorResource(UIHelper.activityTypeIconMapping[data.class_type_id] ?: UIHelper.otherIcon)
    )
}