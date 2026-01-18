package com.cdkentertainment.muniversity.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cdkentertainment.muniversity.UISingleton
import com.cdkentertainment.muniversity.getLocalized
import com.cdkentertainment.muniversity.models.Payment

@Composable
fun PaymentView(
    payment: Payment,
    modifier: Modifier = Modifier
) {
    val context: Context = LocalContext.current
    var showDetails: Boolean by rememberSaveable { mutableStateOf(false) }
    if (showDetails) {
        PaymentInfoPopupView(data = payment, onDismissRequest = { showDetails = false })
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = UISingleton.color2,
            disabledContainerColor = UISingleton.color2,
            contentColor = UISingleton.textColor1,
            disabledContentColor = UISingleton.textColor1
        ),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
        onClick = { showDetails = true },
        modifier = modifier
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = payment.type.description.getLocalized(context),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
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
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = payment.payment_deadline,
                    style = MaterialTheme.typography.titleMedium,
                    color = UISingleton.textColor4,
                    modifier = Modifier
                        .background(
                            UISingleton.color3,
                            RoundedCornerShape(
                                bottomStart = UISingleton.uiElementsCornerRadius.dp,
                            )
                        )
                        .background(
                            UISingleton.color4,
                            RoundedCornerShape(
                                bottomStart = UISingleton.uiElementsCornerRadius.dp,
                                bottomEnd = UISingleton.uiElementsCornerRadius.dp,
                            )
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
                Text(
                    text = "${"%.2f".format(if (payment.state == "unpaid") payment.total_amount - payment.saldo_amount else payment.total_amount)} zł",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.ExtraBold,
                    color = UISingleton.textColor4,
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            UISingleton.color3,
                            RoundedCornerShape(
                                bottomEnd = UISingleton.uiElementsCornerRadius.dp,
                            )
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}