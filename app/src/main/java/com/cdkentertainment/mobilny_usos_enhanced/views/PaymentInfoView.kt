package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Payment

@Composable
fun PaymentInfoView(data: Payment) {
    GroupedContentContainerView(
        title = stringResource(R.string.payment_information),
        backgroundColor = UISingleton.color1,
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    UISingleton.color2,
                    RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = if (data.state == "paid") "Zapłacona w całości" else "Niezapłacona",
                style = MaterialTheme.typography.titleMedium,
                color = UISingleton.textColor1,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
            )
            Icon(
                imageVector = if (data.state == "paid") Icons.Rounded.Done else Icons.Rounded.Close,
                contentDescription = "Icon",
                tint = UISingleton.textColor4,
                modifier = Modifier
                    .size(48.dp)
                    .background(UISingleton.color3, CircleShape)
                    .padding(8.dp)
            )
        }
        if (data.state == "unpaid") {
            GradeCardView(
                courseName = "Do zapłaty",
                grade = "${"%.2f".format(data.total_amount - data.saldo_amount)} zł",
                showArrow = false,
                backgroundColor = UISingleton.color2
            )
            GradeCardView(
                courseName = "Termin płatności",
                grade = data.payment_deadline,
                showArrow = false,
                backgroundColor = UISingleton.color2
            )
        }
    }
}