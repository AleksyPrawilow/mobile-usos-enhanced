package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton

@Composable
fun LatestSomethingView(
    icon: ImageVector,
    title: String,
    badge: String? = null
) {
    val cardShape: RoundedCornerShape = remember { RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp) }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = UISingleton.color2,
            disabledContainerColor = UISingleton.color2,
            contentColor = UISingleton.textColor1,
            disabledContentColor = UISingleton.textColor1
        ),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = cardShape,
        onClick = {

        },
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .defaultMinSize(minWidth = 128.dp, minHeight = 96.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterVertically),
                modifier = Modifier.padding(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = UISingleton.textColor1,
                    modifier = Modifier.defaultMinSize(48.dp, 48.dp)
                )
                Text(
                    text = title,
                    color = UISingleton.textColor1,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            if (badge != null) {
                Badge(
                    //containerColor = UISingleton.color3,
                    //contentColor = UISingleton.textColor4,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding((UISingleton.uiElementsCornerRadius / 4).dp)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}