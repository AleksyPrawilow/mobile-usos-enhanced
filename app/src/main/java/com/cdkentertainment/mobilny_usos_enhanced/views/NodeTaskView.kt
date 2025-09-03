package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.runtime.Composable
import com.cdkentertainment.mobilny_usos_enhanced.models.StudentsPoints
import com.cdkentertainment.mobilny_usos_enhanced.models.SubjectTestContainer
import com.cdkentertainment.mobilny_usos_enhanced.models.TaskNodeDetails

@Composable
fun NodeTaskView(
    nodeName: String,
    taskNodeDetails: TaskNodeDetails,
    studentsPoints: StudentsPoints? = null,
    subnodes: List<SubjectTestContainer?>?,
    onClick: () -> Unit = {}
) {
    val clickedNoSubnodes: () -> Unit = {

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