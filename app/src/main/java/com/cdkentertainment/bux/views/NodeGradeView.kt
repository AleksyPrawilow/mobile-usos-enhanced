package com.cdkentertainment.bux.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.cdkentertainment.bux.models.GradeNodeDetails

@Composable
fun NodeGradeView(
    courseName: String,
    data: GradeNodeDetails,
    nodeId: Int
) {
    var showDetails: Boolean by rememberSaveable { mutableStateOf(false) }
    if (showDetails && data.students_grade != null) {
        TestGradePopupView(
            title = courseName,
            grade = data.students_grade,
            nodeId = nodeId,
            type = "GradeNode"
        ) {
            showDetails = false
        }
    }
    GradeCardView(
        courseName = courseName,
        grade = data.students_grade?.grade_value?.symbol ?: "—",
        onClick = {
            showDetails = true
        }
    )
}