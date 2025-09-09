package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.runtime.Composable
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup

@Composable
fun CourseContainerView(
    courseUnits: List<LessonGroup>,
    renderCourseUnit: @Composable (LessonGroup) -> Unit = { unit ->
        AttendanceClassGroupView(data = unit)
    }
) {
    GroupedContentContainerView(
        title = courseUnits.first().course_name.pl
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