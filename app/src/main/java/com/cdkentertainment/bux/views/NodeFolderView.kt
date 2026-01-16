package com.cdkentertainment.bux.views

import androidx.compose.runtime.Composable
import com.cdkentertainment.bux.models.StudentsPoints

@Composable
fun NodeFolderView(
    nodeName: String,
    studentsPoints: StudentsPoints? = null,
    onClick: () -> Unit = {}
) {
    GradeCardView(
        courseName = nodeName,
        grade = studentsPoints?.points.toString(),
        showGrade = studentsPoints != null && studentsPoints.points != null,
        showArrow = true,
        onClick = onClick
    )
}