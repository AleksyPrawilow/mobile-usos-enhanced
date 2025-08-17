package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton

@Composable
fun AttendanceTypePopupView(
    value: MutableState<Int>,
    onDismissExtra: () -> Unit,
    sex: Int = 0
) {
    var wasPresent: Boolean by rememberSaveable { mutableStateOf(true) }
    var absenceJustified: Boolean by rememberSaveable { mutableStateOf(true) }
    val onDismiss: () -> Unit = {
        println("OK!")
        if (wasPresent) {
            value.value = 1
        }
        if (!wasPresent && !absenceJustified) {
            value.value = 2
        }
        if (!wasPresent && absenceJustified) {
            value.value = 3
        }
    }

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
                .background(UISingleton.color2.primaryColor, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .border(5.dp, UISingleton.color1.primaryColor, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(12.dp)
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = "Zaznacz obecność",
                    style = MaterialTheme.typography.headlineMedium,
                    color = UISingleton.color4.primaryColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                HorizontalDivider(thickness = 5.dp, color = UISingleton.color3.primaryColor, modifier = Modifier.clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = UISingleton.color1.primaryColor,
                        disabledContainerColor = UISingleton.color1.primaryColor,
                        contentColor = UISingleton.color4.primaryColor,
                        disabledContentColor = UISingleton.color4.primaryColor
                    ),
                    shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .padding(12.dp)
                    ) {
                        SwitchSettingView(
                            color1 = UISingleton.color1.oppositeColor,
                            color2 = UISingleton.color2.oppositeColor,
                            color3 = UISingleton.color3.oppositeColor,
                            color4 = UISingleton.color3.oppositeColor,
                            checked = wasPresent,
                            text = if (sex == 0) "Obecny" else "Obecna",
                            onSwitchChange = {
                                wasPresent = it
                                absenceJustified = true
                            }
                        )
                        if (!wasPresent) {
                            SwitchSettingView(
                                color1 = UISingleton.color1.oppositeColor,
                                color2 = UISingleton.color2.oppositeColor,
                                color3 = UISingleton.color3.oppositeColor,
                                color4 = UISingleton.color3.oppositeColor,
                                checked = absenceJustified,
                                text = "Usprawiedliwiona",
                                onSwitchChange = {
                                    absenceJustified = it
                                }
                            )
                        }
                    }
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UISingleton.color1.primaryColor,
                        contentColor = UISingleton.color4.primaryColor,
                        disabledContainerColor = UISingleton.color1.primaryColor,
                        disabledContentColor = UISingleton.color4.primaryColor
                    ),
                    onClick = {
                        onDismiss()
                        onDismissExtra()
                    },
                    shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Zatwierdź",
                        style = MaterialTheme.typography.titleLarge,
                        color = UISingleton.color4.primaryColor
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