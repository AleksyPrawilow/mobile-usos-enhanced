package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton

@Composable
fun SquishyIconView(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = UISingleton.textColor4,
    squishScale: Float = 0.3f
) {
    var currentIcon by remember { mutableStateOf(icon) }
    var triggerAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(icon) {
        triggerAnimation = true
        currentIcon = icon
    }

    val scale by animateFloatAsState(
        targetValue = if (triggerAnimation) squishScale else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium),
        finishedListener = {
            triggerAnimation = false
        }
    )

    Icon(
        imageVector = currentIcon,
        contentDescription = null,
        tint = tint,
        modifier = modifier.graphicsLayer(
            scaleX = scale,
            scaleY = scale
        )
    )
}