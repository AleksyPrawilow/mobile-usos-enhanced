package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Course
import com.cdkentertainment.mobilny_usos_enhanced.models.CourseUnitIds
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import com.cdkentertainment.mobilny_usos_enhanced.models.TermGrade


@Composable
fun CourseGradesView(
    data: Course,
    nameMap: Map<String, CourseUnitIds>,
    classtypeIdInfo: Map<String, SharedDataClasses.IdAndName>?
) {
    var showDetails: Boolean by remember { mutableStateOf(false) }
    var popupGrade: TermGrade? by remember { mutableStateOf(null) }

    if (showDetails && popupGrade != null) {
        GradePopupView(popupGrade!!) {
            UISingleton.dropBlurContent()
            showDetails = false
            popupGrade = null
        }
    }

    Card(
        colors = CardColors(
            contentColor = UISingleton.textColor1,
            containerColor = UISingleton.color2,
            disabledContainerColor = UISingleton.color2,
            disabledContentColor = UISingleton.textColor1
        ),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = nameMap[data.courseGrades.course_units_grades.keys.first()]?.course_name ?: "N/A",
                color = UISingleton.textColor1,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
//            HorizontalDivider(
//                thickness = 5.dp,
//                color = UISingleton.textColor2,
//                modifier = Modifier
//                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
//            )
            for (courseUnit in data.courseGrades.course_units_grades.keys) {
                val unitClassType: String = nameMap[courseUnit]?.classtype_id ?: "N/A"
                val condition: Boolean = data.courseGrades.course_units_grades[courseUnit] != null && data.courseGrades.course_units_grades[courseUnit]!![0]["1"] != null
                GradeCardView(
                    classtypeIdInfo?.get(unitClassType)?.name?.pl ?: "N/A",
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
}