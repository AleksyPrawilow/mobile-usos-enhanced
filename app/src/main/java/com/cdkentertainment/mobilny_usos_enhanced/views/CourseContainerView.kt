package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup

@Composable
fun CourseContainerView(
    courseUnits: List<LessonGroup>,
    renderCourseUnit: @Composable (LessonGroup) -> Unit = { unit ->
        AttendanceClassGroupView(data = unit)
    }
) {
    val context: Context = LocalContext.current
    GroupedContentContainerView(
        title = courseUnits.first().course_name.getLocalized(context)
    ) {
        courseUnits.forEach { renderCourseUnit(it) }
    }
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(UISingleton.color2, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
//            )
//    ) {
//        Column(
//            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
//            modifier = Modifier
//                .padding(12.dp)
//                .fillMaxWidth()
//        ) {
//            Text(
//                text = courseUnits.first().course_name.pl,
//                color = UISingleton.textColor1,
//                style = MaterialTheme.typography.titleLarge
//            )
//            HorizontalDivider(
//                thickness = 5.dp,
//                color = UISingleton.textColor2,
//                modifier = Modifier
//                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
//            )
//        }
//    }
}