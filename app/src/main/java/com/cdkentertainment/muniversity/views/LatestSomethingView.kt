package com.cdkentertainment.muniversity.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.cdkentertainment.muniversity.UISingleton

@Composable
fun LatestSomethingView(
    icon: ImageVector,
    title: String,
    maxWidth: Int,
    badge: String? = null,
    maxLines: Int = 1,
    backgroundColor: Color = UISingleton.color2,
    disabledBackgroundColor: Color = UISingleton.color2,
    textColor: Color = UISingleton.textColor1,
    disabledTextColor: Color = UISingleton.textColor1,
    enabled: Boolean = true,
    loading: Boolean = false,
    loadingError: Boolean = false,
    onClick: () -> Unit = {}
) {
    val cardShape: RoundedCornerShape = remember { RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp) }
    val density: Density = LocalDensity.current
    val cardColor: Color by animateColorAsState(
        if (enabled) backgroundColor else disabledBackgroundColor,
    )
    val fontColor: Color by animateColorAsState(
        if (enabled) textColor else disabledTextColor,
    )
    Card(
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
            disabledContainerColor = cardColor,
            contentColor = fontColor,
            disabledContentColor = fontColor
        ),
        elevation = CardDefaults.cardElevation(3.dp, disabledElevation = 12.dp),
        enabled = enabled,
        shape = cardShape,
        onClick = onClick,
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
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.defaultMinSize(48.dp, 48.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = maxLines,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .width(with(density) { maxWidth.toDp() })
                )
            }
            if (badge != null) {
                Badge(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding((UISingleton.uiElementsCornerRadius / 4).dp)
                ) {
                    if (!loading) {
                        Text(
                            text = if (!loadingError) badge else "?",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}