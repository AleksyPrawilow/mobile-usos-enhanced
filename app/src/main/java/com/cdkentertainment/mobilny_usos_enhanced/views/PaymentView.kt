package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.models.Payment

@Composable
fun PaymentView(
    payment: Payment
) {
    val context: Context = LocalContext.current
    var showDetails: Boolean by rememberSaveable { mutableStateOf(false) }
    if (showDetails) {
        PaymentInfoPopupView(data = payment, onDismissRequest = { showDetails = false })
    }
    Column(
        modifier = Modifier
            .drawBehind {
                drawRoundRect(
                    UISingleton.color3,
                    cornerRadius = CornerRadius(UISingleton.uiElementsCornerRadius.dp.toPx(), UISingleton.uiElementsCornerRadius.dp.toPx())
                )
            }
            .shadow(3.dp, shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            .clickable(onClick = {
                showDetails = true
            })
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = UISingleton.color2,
                disabledContainerColor = UISingleton.color2,
                contentColor = UISingleton.textColor1,
                disabledContentColor = UISingleton.textColor1
            ),
            //elevation = CardDefaults.cardElevation(3.dp),
            shape = RoundedCornerShape(
                bottomStart = 0.dp,
                topEnd = UISingleton.uiElementsCornerRadius.dp,
                topStart = UISingleton.uiElementsCornerRadius.dp,
                bottomEnd = 0.dp//UISingleton.uiElementsCornerRadius.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = payment.description.getLocalized(context),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = UISingleton.textColor1,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = "More",
                    tint = UISingleton.textColor1,
                    modifier = Modifier
                )
            }
//            Text(
//                text = payment.description.pl,
//                style = MaterialTheme.typography.titleLarge,
//                color = UISingleton.textColor1,
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
//                modifier = Modifier.padding(12.dp)
//            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = payment.payment_deadline,
                style = MaterialTheme.typography.titleMedium,
                color = UISingleton.textColor4,
                modifier = Modifier
                    .background(
                        UISingleton.color4,
                        RoundedCornerShape(
                            bottomStart = UISingleton.uiElementsCornerRadius.dp,
                            bottomEnd = UISingleton.uiElementsCornerRadius.dp,
                            topStart = 0.dp,
                            topEnd = 0.dp
                        )
                    )
                    .zIndex(0.75f)
                    .padding(top = 4.dp, bottom = 4.dp, start = 12.dp, end = 12.dp)
            )
            Text(
                text = "${"%.2f".format(if (payment.state == "unpaid") payment.total_amount - payment.saldo_amount else payment.total_amount)} zł",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.ExtraBold,
                color = UISingleton.textColor4,
                modifier = Modifier
                    .offset(x = -UISingleton.uiElementsCornerRadius.dp)
                    .weight(1f)
                    .background(
                        UISingleton.color3,
                        RoundedCornerShape(
                            bottomStart = 0.dp,
                            bottomEnd = UISingleton.uiElementsCornerRadius.dp,
                            topStart = 0.dp,
                            topEnd = 0.dp
                        )
                    )
                    .zIndex(0.5f)
                    .padding(top = 4.dp, bottom = 4.dp, start = (12 + UISingleton.uiElementsCornerRadius).dp, end = 12.dp)
            )
        }
    }
}