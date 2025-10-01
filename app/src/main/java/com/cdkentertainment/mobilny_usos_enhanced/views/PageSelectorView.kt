package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton.color2

@Composable
fun PageSelectorView(
    pageNum: String,
    faculty: Map<String, String>,
    selectedFaculty: String,
    showBack: Boolean,
    showNext: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onFacultySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape: RoundedCornerShape = remember { RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp) }
    var facultiesExpanded by remember { mutableStateOf(false) }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = UISingleton.color1,
            disabledContainerColor = UISingleton.color1,
            contentColor = UISingleton.textColor1,
            disabledContentColor = UISingleton.textColor1
        ),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = shape,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Min)
                .padding(6.dp)
        ) {
            TextButton(
                colors = ButtonColors(
                    contentColor = UISingleton.textColor1,
                    disabledContentColor = UISingleton.color2,
                    containerColor = UISingleton.color1,
                    disabledContainerColor = UISingleton.color1
                ),
                onClick = onBack,
                enabled = showBack,
                modifier = Modifier.weight(1f).padding(end = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                )
            }
            Text(
                text = pageNum,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = UISingleton.textColor1,
                textAlign = TextAlign.Center
            )
            TextButton(
                colors = ButtonColors(
                    contentColor = UISingleton.textColor1,
                    disabledContentColor = UISingleton.color2,
                    containerColor = UISingleton.color1,
                    disabledContainerColor = UISingleton.color1
                ),
                onClick = onNext,
                enabled = showNext,
                modifier = Modifier.weight(1f).padding(start = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = UISingleton.color1,
                    disabledContainerColor = UISingleton.color1,
                    contentColor = UISingleton.textColor1,
                    disabledContentColor = UISingleton.textColor1
                ),
                enabled = true, // faculties.size() > 0
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                shape = shape,
                onClick = { facultiesExpanded = !facultiesExpanded }
            ) {
                Text(
                    text = faculty[selectedFaculty] ?: "N/A",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = UISingleton.textColor1
                )
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = UISingleton.textColor1
                )
                DropdownMenu(
                    expanded = facultiesExpanded,
                    onDismissRequest = { facultiesExpanded = false },
                    containerColor = color2,
                    shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
                ) {
                    for ((facultyId, facultyName) in faculty) {
                        DropdownMenuItem(
                            contentPadding = PaddingValues(12.dp),
                            text = {
                                Text(
                                    text = facultyName,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = UISingleton.textColor1
                                )
                            },
                            onClick = {
                                facultiesExpanded = false
                                onFacultySelect(facultyId)
                            }
                        )
                    }
                }
            }
        }
    }
}