package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    var showDetails: Boolean by rememberSaveable { mutableStateOf(false) }
    var popupGrade: TermGrade? by rememberSaveable { mutableStateOf(null) }

    if (showDetails && popupGrade != null) {
        GradePopupView(popupGrade!!) {
            showDetails = false
            popupGrade = null
        }
    }

    Card(
        colors = CardColors(
            contentColor = UISingleton.color4.primaryColor,
            containerColor = UISingleton.color2.primaryColor,
            disabledContainerColor = UISingleton.color2.primaryColor,
            disabledContentColor = UISingleton.color4.primaryColor
        ),
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
                text = nameMap[data.userGrades.course_units_grades.keys.first()]?.course_name ?: "N/A",
                color = UISingleton.color4.primaryColor,
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider(
                thickness = 5.dp,
                color = UISingleton.color3.primaryColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            )
            for (courseUnit in data.userGrades.course_units_grades.keys) {
                val unitClassType: String = nameMap[courseUnit]?.classtype_id ?: "N/A"
                val condition: Boolean = data.userGrades.course_units_grades[courseUnit] != null && data.userGrades.course_units_grades[courseUnit]!![0]["1"] != null
                GradeCardView(
                    classtypeIdInfo?.get(unitClassType)?.name?.pl ?: "N/A",
                    if (condition) data.userGrades.course_units_grades[courseUnit]!![0]["1"]!!.value_symbol else "-"
                ) {
                    println("Hello?")
                    showDetails = true
                    popupGrade = data.userGrades.course_units_grades[courseUnit]!![0]["1"]
                }
            }
        }
    }
}