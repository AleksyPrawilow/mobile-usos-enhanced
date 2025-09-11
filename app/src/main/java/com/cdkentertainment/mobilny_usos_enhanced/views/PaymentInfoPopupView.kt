package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.models.Payment

@Composable
fun PaymentInfoPopupView(
    data: Payment,
    onDismissRequest: () -> Unit
) {
    val clipboard = LocalClipboardManager.current
    val context: Context = LocalContext.current
    val toast: Toast = Toast.makeText(context, "Skopiowano do schowka", Toast.LENGTH_SHORT)
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                //.fillMaxSize()
                .fillMaxWidth()
                .shadow(10.dp, shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .background(UISingleton.color2, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .border(5.dp, UISingleton.color1, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = data.description.getLocalized(context),
                    style = MaterialTheme.typography.headlineSmall,
                    color = UISingleton.textColor1,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                PaymentInfoView(data)
                if (data.state == "unpaid") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                            .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                            .background(UISingleton.color1)
                            .clickable(onClick = {
                                clipboard.setText(AnnotatedString(data.account_number))
                                toast.show()
                            })
                            .padding(12.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Konto bankowe",
                                style = MaterialTheme.typography.titleLarge,
                                color = UISingleton.textColor1
                            )
                            HorizontalDivider(
                                thickness = 5.dp,
                                color = UISingleton.textColor2,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                            )
                            Text(
                                text = data.account_number,
                                style = MaterialTheme.typography.titleMedium,
                                color = UISingleton.textColor1
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            DismissPopupButtonView(onDismissRequest = onDismissRequest, modifier = Modifier.align(Alignment.TopEnd))
        }
    }
}