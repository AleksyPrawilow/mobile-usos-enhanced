package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Test

// TODO: Combine this into a separate class while changing the ClassGroupCardView.kt to something more universal
@Composable
fun TestCardView(
    testData: Test
) {
    var expanded: Boolean by rememberSaveable { mutableStateOf(false) }
    val onClick: () -> Unit = { expanded = !expanded }

    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // TODO: This definitely can be moved into another file
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .clickable(onClick = onClick)
                .zIndex(1f)
        ) {
            Text(
                text = testData.course_edition?.course_name?.pl ?: "N/A",
                color = UISingleton.color4.primaryColor,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        UISingleton.color2.primaryColor,
                        RoundedCornerShape(
                            topStart = UISingleton.uiElementsCornerRadius.dp,
                            topEnd = UISingleton.uiElementsCornerRadius.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .padding(12.dp)
            )
            Text(
                text = "Wszyscy uczestnicy",
                style = MaterialTheme.typography.titleMedium,
                color = UISingleton.color1.primaryColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        UISingleton.color3.primaryColor,
                        RoundedCornerShape(
                            bottomStart = UISingleton.uiElementsCornerRadius.dp,
                            bottomEnd = UISingleton.uiElementsCornerRadius.dp,
                            topStart = 0.dp,
                            topEnd = 0.dp
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
        AnimatedVisibility(expanded, enter = expandVertically(), exit = shrinkVertically(), modifier = Modifier.offset(y = -UISingleton.uiElementsCornerRadius.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        UISingleton.color2.primaryColor,
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = UISingleton.uiElementsCornerRadius.dp,
                            bottomEnd = UISingleton.uiElementsCornerRadius.dp
                        )
                    )
                    .zIndex(0.75f)
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp + UISingleton.uiElementsCornerRadius.dp, bottom = 12.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val subFields: List<String> = listOf("folder", "folder", "grade")

                    TestNameDescriptionView(testData.name.pl, testData.description.pl)
                    for (node in subFields) {
                        when(node) {
                            "folder" -> {
                                NodeFolderView()
                            }
                            "task" -> {
                                NodeTaskView()
                            }
                            "grade" -> {
                                NodeGradeView()
                            }
                        }
                    }
                }
            }
        }
    }
}