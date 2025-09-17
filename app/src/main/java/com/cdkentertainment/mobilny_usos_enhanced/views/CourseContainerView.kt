package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup

@Composable
fun CourseContainerView(
    courseUnits: List<LessonGroup>,
    modifier: Modifier = Modifier,
    renderCourseUnit: @Composable (LessonGroup) -> Unit = { unit ->
        AttendanceClassGroupView(data = unit)
    }
) {
    val context: Context = LocalContext.current
    GroupedContentContainerView(
        title = courseUnits.first().course_name.getLocalized(context),
        modifier = modifier
    ) {
        courseUnits.forEach { renderCourseUnit(it) }
    }
}