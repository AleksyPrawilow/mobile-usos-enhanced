package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton

@Composable
fun DismissPopupButtonView(
    onDismissRequest: () -> Unit = {},
    modifier: Modifier
) {
    val iconBackgroundBrush = linearGradient(
        colorStops = arrayOf(
            0.0f to UISingleton.color1.primaryColor,
            0.5f to UISingleton.color1.primaryColor,
            0.5f to UISingleton.color2.primaryColor
        ),
        start = Offset(Float.POSITIVE_INFINITY, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )
    Icon(
        imageVector = Icons.Rounded.Close,
        contentDescription = "Close",
        tint = UISingleton.color4.primaryColor,
        modifier = Modifier
            .size(48.dp)
            .shadow(5.dp, shape = RoundedCornerShape(topStart = 0.dp, topEnd = UISingleton.uiElementsCornerRadius.dp, bottomStart = UISingleton.uiElementsCornerRadius.dp, bottomEnd = 0.dp))
            .clickable(onClick = onDismissRequest)
            .background(UISingleton.color1.primaryColor)
            .then(modifier)
    )
}