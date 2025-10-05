package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cdkentertainment.mobilny_usos_enhanced.R
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
                    .verticalScroll(rememberScrollState())
            ) {
                PopupHeaderView(
                    title = data.type.description.getLocalized(context)
                )
                GroupedContentContainerView(
                    title = stringResource(R.string.description),
                    backgroundColor = UISingleton.color1,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp)
                ) {
                    Text(
                        text = data.description.getLocalized(context),
                        style = MaterialTheme.typography.titleMedium,
                        color = UISingleton.textColor1
                    )
                }
                PaymentInfoView(data)
                Spacer(modifier = Modifier.height(12.dp))
                if (data.state == "unpaid") {
                    GroupedContentContainerView(
                        title = stringResource(R.string.bank_account),
                        backgroundColor = UISingleton.color1,
                        onClick = {
                            clipboard.setText(AnnotatedString(data.account_number))
                        },
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                    ) {
                        Text(
                            text = data.account_number,
                            style = MaterialTheme.typography.titleMedium,
                            color = UISingleton.textColor1
                        )
                    }
                }
            }
            DismissPopupButtonView(onDismissRequest = onDismissRequest, modifier = Modifier.align(Alignment.TopEnd))
        }
    }
}