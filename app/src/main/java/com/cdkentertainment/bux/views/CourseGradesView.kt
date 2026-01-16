package com.cdkentertainment.bux.views

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import com.cdkentertainment.bux.UIHelper
import com.cdkentertainment.bux.UISingleton
import com.cdkentertainment.bux.getLocalized
import com.cdkentertainment.bux.models.Course
import com.cdkentertainment.bux.models.CourseUnitData
import com.cdkentertainment.bux.models.TermGrade


@Composable
fun CourseGradesView(
    data: Course,
    nameMap: Map<String, CourseUnitData>,
    modifier: Modifier = Modifier
) {
    val context: Context = LocalContext.current
    var showDetails: Boolean by remember { mutableStateOf(false) }
    var popupGrade: TermGrade? by remember { mutableStateOf(null) }

    if (showDetails && popupGrade != null) {
        GradePopupView(
            grade = popupGrade!!,
            title = nameMap[data.courseGrades.course_units_grades.keys.first()]?.course_name?.getLocalized(context) ?: "N/A"
        ) {
            UISingleton.dropBlurContent()
            showDetails = false
            popupGrade = null
        }
    }

    GroupedContentContainerView(
        title = nameMap[data.courseGrades.course_units_grades.keys.first()]?.course_name?.getLocalized(context) ?: "N/A",
        modifier = modifier
    ) {
        for (courseUnit in data.courseGrades.course_units_grades.keys) {
            val unitClassType: String = nameMap[courseUnit]?.classtype_id ?: "N/A"
            val condition: Boolean = data.courseGrades.course_units_grades[courseUnit] != null && data.courseGrades.course_units_grades[courseUnit]?.first()["1"] != null
            GradeCardView(
                courseName = UIHelper.classTypeIds[unitClassType]?.name?.getLocalized(context) ?: "N/A",
                grade = if (condition) data.courseGrades.course_units_grades[courseUnit]?.first()["1"]?.value_symbol ?: "—" else "—",
                showArrow = condition,
                backgroundColor = UISingleton.color1,
                sideIcon = ImageVector.vectorResource(UIHelper.activityTypeIconMapping[nameMap[courseUnit]?.classtype_id] ?: UIHelper.otherIcon)
            ) {
                UISingleton.blurContent()
                popupGrade = data.courseGrades.course_units_grades[courseUnit]?.first()["1"]
                showDetails = popupGrade != null
            }
        }
    }
}