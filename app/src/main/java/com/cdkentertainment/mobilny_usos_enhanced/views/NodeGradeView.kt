package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.runtime.Composable
import com.cdkentertainment.mobilny_usos_enhanced.models.GradeNodeDetails

@Composable
fun NodeGradeView(
    courseName: String,
    data: GradeNodeDetails
) {
    GradeCardView(
        courseName = courseName,
        grade = data.students_grade?.grade_value?.symbol ?: "-",
        onClick = {
            // Popup view with grade stats
        }
    )
}