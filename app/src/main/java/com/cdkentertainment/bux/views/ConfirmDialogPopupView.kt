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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.cdkentertainment.bux.UISingleton

@Composable
fun ConfirmDialogPopupView(
    title: String,
    confirmTitle: String,
    dismissTitle: String,
    description: String? = null,
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val shape: RoundedCornerShape = remember { RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp) }
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .shadow(
                    elevation = 10.dp,
                    shape = shape
                )
                .background(
                    color = UISingleton.color2,
                    shape = shape
                )
                .border(
                    width = 5.dp,
                    color = UISingleton.color1,
                    shape = shape
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                PopupHeaderView(title)
                if (description != null) {
                    Card(
                    colors = CardDefaults.cardColors(
                        containerColor = UISingleton.color1,
                        disabledContainerColor = UISingleton.color1,
                        contentColor = UISingleton.textColor1,
                        disabledContentColor = UISingleton.textColor1
                    ),
                    shape = shape,
                    elevation = CardDefaults.cardElevation(3.dp),
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.titleMedium,
                        color = UISingleton.textColor1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                }
            }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UISingleton.color3,
                            contentColor = UISingleton.textColor4
                        ),
                        shape = shape,
                        elevation = ButtonDefaults.buttonElevation(3.dp),
                        onClick = onConfirm
                    ) {
                        Text(
                            text = confirmTitle,
                            style = MaterialTheme.typography.titleLarge,
                            color = UISingleton.textColor4,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UISingleton.color1,
                            contentColor = UISingleton.textColor1
                        ),
                        shape = shape,
                        elevation = ButtonDefaults.buttonElevation(3.dp),
                        onClick = onDismiss
                    ) {
                        Text(
                            text = dismissTitle,
                            style = MaterialTheme.typography.titleLarge,
                            color = UISingleton.textColor1,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            DismissPopupButtonView(onDismissRequest = onDismiss, modifier = Modifier.align(alignment = Alignment.TopEnd))
        }
    }
}