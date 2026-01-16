package com.cdkentertainment.bux.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.cdkentertainment.bux.R
import com.cdkentertainment.bux.UISingleton
import kotlin.math.max

@Composable
fun AttendanceTypePopupView(
    value: MutableState<Int>,
    onDismissExtra: () -> Unit
) {
    val radioOptions = listOf(
        Pair(stringResource(R.string.present), 1),
        Pair(stringResource(R.string.excused_absence), 2),
        Pair(stringResource(R.string.unexcused_absence), 3),
        Pair(stringResource(R.string.class_cancelled), 4)
    )
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[max(0, value.value - 1)]) }
    val onDismiss: () -> Unit = {
        value.value = selectedOption.second
    }
    val shape: RoundedCornerShape = remember { RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp) }

    Dialog(
        onDismissRequest = {
            onDismiss()
            onDismissExtra()
        },
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .shadow(10.dp, shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .background(UISingleton.color2, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .border(5.dp, UISingleton.color1, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                PopupHeaderView(title = stringResource(R.string.register_attendance))
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.selectableGroup().padding(horizontal = 12.dp)
                ) {
                    radioOptions.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .shadow(3.dp, shape = shape)
                                .clip(shape)
                                .selectable(
                                    selected = (text.first == selectedOption.first),
                                    onClick = { onOptionSelected(text) },
                                    role = Role.RadioButton
                                )
                                .background(UISingleton.color1, shape = shape)
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == selectedOption),
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = UISingleton.color4,
                                    unselectedColor = UISingleton.color2
                                )
                            )
                            Text(
                                text = text.first,
                                style = MaterialTheme.typography.titleMedium,
                                color = UISingleton.textColor1,
                                fontWeight = if (text == selectedOption) FontWeight.Normal else FontWeight.Light,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UISingleton.color3,
                        contentColor = UISingleton.textColor1,
                        disabledContainerColor = UISingleton.color3,
                        disabledContentColor = UISingleton.textColor1
                    ),
                    onClick = {
                        onDismiss()
                        onDismissExtra()
                    },
                    elevation = ButtonDefaults.buttonElevation(3.dp),
                    shape = shape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.confirm),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = UISingleton.textColor4
                    )
                }
            }
            DismissPopupButtonView(
                modifier = Modifier.align(Alignment.TopEnd),
                onDismissRequest = {
                    onDismiss()
                    onDismissExtra()
                }
            )
        }
    }
}