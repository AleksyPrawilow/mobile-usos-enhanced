package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton

@Composable
fun ContactInfoView(
    phoneNumber: String = "+48 xxx-xxx-xxx",
    email: String = "example@amu.edu.pl",
    address: String = "Uniwersytetu Poznańskiego 4, 61-614 Poznań, pokój B1-420"
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 12.dp)
            .background(UISingleton.color1, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Kontakt",
                style = MaterialTheme.typography.titleLarge,
                color = UISingleton.textColor1
            )
            HorizontalDivider(
                thickness = 5.dp,
                color = UISingleton.textColor2,
                modifier = Modifier
                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            )
            val categories: Map<String, Pair<ImageVector, String>> = mapOf(
                "Telefon" to (Icons.Rounded.Call to phoneNumber),
                "Email" to (Icons.Rounded.Email to email),
                "Adres" to (Icons.Rounded.LocationOn to address)
            )
            for ((category, iconAndText) in categories) {
                if (iconAndText.second.isEmpty()) continue
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(UISingleton.color2, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = iconAndText.first,
                        contentDescription = category,
                        tint = UISingleton.textColor4,
                        modifier = Modifier
                            .size(48.dp)
                            .background(UISingleton.color3, CircleShape)
                            .padding(12.dp)
                    )
                    Text(
                        text = iconAndText.second,
                        style = MaterialTheme.typography.titleMedium,
                        color = UISingleton.textColor1
                    )
                }
            }
        }
    }
}