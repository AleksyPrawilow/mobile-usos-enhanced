package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Payment

@Composable
fun PaymentView(
    payment: Payment
) {
    Column(
        modifier = Modifier
            .drawBehind {
                drawRoundRect(
                    UISingleton.color3,
                    cornerRadius = CornerRadius(UISingleton.uiElementsCornerRadius.dp.toPx(), UISingleton.uiElementsCornerRadius.dp.toPx())
                )
            }
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = UISingleton.color2,
                disabledContainerColor = UISingleton.color2,
                contentColor = UISingleton.textColor1,
                disabledContentColor = UISingleton.textColor1
            ),
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
            Text(
                text = payment.description.pl,
                style = MaterialTheme.typography.titleLarge,
                color = UISingleton.textColor1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(12.dp)
            )
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
                text = "${"%.2f".format(payment.total_amount)} zł",
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