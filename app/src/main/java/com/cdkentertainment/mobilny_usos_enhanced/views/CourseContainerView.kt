package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup

@Composable
fun CourseContainerView(
    courseUnits: List<LessonGroup>,
    renderCourseUnit: @Composable (LessonGroup) -> Unit = { unit ->
        AttendanceClassGroupView(data = unit)
    }
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(UISingleton.color2, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = courseUnits.first().course_name.pl,
                color = UISingleton.textColor1,
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider(
                thickness = 5.dp,
                color = UISingleton.textColor2,
                modifier = Modifier
                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            )
            courseUnits.forEach { renderCourseUnit(it) }
        }
    }
}