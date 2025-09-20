package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.models.Course
import com.cdkentertainment.mobilny_usos_enhanced.models.CourseUnitIds
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import com.cdkentertainment.mobilny_usos_enhanced.models.TermGrade


@Composable
fun CourseGradesView(
    data: Course,
    nameMap: Map<String, CourseUnitIds>,
    classtypeIdInfo: Map<String, SharedDataClasses.IdAndName>?,
    modifier: Modifier = Modifier
) {
    val context: Context = LocalContext.current
    var showDetails: Boolean by remember { mutableStateOf(false) }
    var popupGrade: TermGrade? by remember { mutableStateOf(null) }

    if (showDetails && popupGrade != null) {
        GradePopupView(
            popupGrade!!,
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
            val condition: Boolean = data.courseGrades.course_units_grades[courseUnit] != null && data.courseGrades.course_units_grades[courseUnit]!![0]["1"] != null
            GradeCardView(
                classtypeIdInfo?.get(unitClassType)?.name?.getLocalized(context) ?: "N/A",
                if (condition) data.courseGrades.course_units_grades[courseUnit]!![0]["1"]!!.value_symbol else "—",
                showArrow = condition,
                backgroundColor = UISingleton.color1
            ) {
                UISingleton.blurContent()
                showDetails = true
                popupGrade = data.courseGrades.course_units_grades[courseUnit]!![0]["1"]
            }
        }
    }
}