package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.cdkentertainment.mobilny_usos_enhanced.models.StudentsPoints
import com.cdkentertainment.mobilny_usos_enhanced.models.SubjectTestContainer
import com.cdkentertainment.mobilny_usos_enhanced.models.TaskNodeDetails

@Composable
fun NodeTaskView(
    nodeName: String,
    taskNodeDetails: TaskNodeDetails,
    studentsPoints: StudentsPoints? = null,
    subnodes: List<SubjectTestContainer?>?,
    nodeId: Int,
    onClick: () -> Unit = {}
) {
    var showDetails: Boolean by rememberSaveable { mutableStateOf(false) }
    val clickedNoSubnodes: () -> Unit = {
        showDetails = true
    }
    if (showDetails && studentsPoints?.points != null) {
        TestTaskPopupView(
            title = nodeName,
            grade = studentsPoints,
            nodeId = nodeId,
            type = "TaskNode"
        ) {
            showDetails = false
        }
    }
    val clicked: () -> Unit = if (!subnodes.isNullOrEmpty()) onClick else clickedNoSubnodes
    GradeCardView(
        courseName = nodeName,
        grade = studentsPoints?.points.toString(),
        showGrade = studentsPoints != null && studentsPoints.points != null,
        showArrow = !subnodes.isNullOrEmpty(),
        onClick = clicked
    )
}