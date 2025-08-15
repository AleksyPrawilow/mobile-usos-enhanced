package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.ClasstypeIdInfo
import com.cdkentertainment.mobilny_usos_enhanced.models.Course
import com.cdkentertainment.mobilny_usos_enhanced.models.CourseUnitIds


@Composable
fun CourseGradesView(
    data: Course,
    nameMap: Map<String, CourseUnitIds>,
    classtypeIdInfo: Map<String, ClasstypeIdInfo>?
) {
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            UISingleton.color1.primaryColor,
                            RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
                        )
                        .padding(12.dp)
                ) {
                    val condition: Boolean =
                        data.userGrades.course_units_grades[courseUnit] != null && data.userGrades.course_units_grades[courseUnit]!![0]["1"] != null
                    Text(
                        text = classtypeIdInfo?.get(unitClassType)?.name?.pl ?: "N/A",
                        color = UISingleton.color3.primaryColor,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    //Spacer(modifier = Modifier.weight(1f))
                    Card(
                        colors = CardColors(
                            contentColor = UISingleton.color4.primaryColor,
                            containerColor = UISingleton.color1.primaryColor,
                            disabledContainerColor = UISingleton.color1.primaryColor,
                            disabledContentColor = UISingleton.color4.primaryColor
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .size(48.dp)
                            .border(5.dp, UISingleton.color2.primaryColor, CircleShape)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = if (condition) data.userGrades.course_units_grades[courseUnit]!![0]["1"]!!.value_symbol else "-",
                                color = UISingleton.color3.primaryColor,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}