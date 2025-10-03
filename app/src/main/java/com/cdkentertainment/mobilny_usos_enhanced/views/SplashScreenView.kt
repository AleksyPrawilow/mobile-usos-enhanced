package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.scaleIndependent
import kotlinx.coroutines.delay

@Composable
fun SplashScreenView(modifier: Modifier) {
    var animateTitleHeight: Boolean by rememberSaveable { mutableStateOf(false) }
    var animateIconOffset: Boolean by rememberSaveable { mutableStateOf(false) }
    var showEnhancedLabel: Boolean by rememberSaveable { mutableStateOf(false) }
    val density: Density = LocalDensity.current
    val textMeasurer: TextMeasurer = rememberTextMeasurer()
    val appName: String = "Mobilny\nUSOS"
    val titleStyle: TextStyle = TextStyle(
        fontSize = 48.sp.scaleIndependent,
        color = UISingleton.textColor1,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Start
    )
    val titleHeight: Int = remember(appName, titleStyle) {
        textMeasurer.measure(
            text = appName,
            style = titleStyle,
        ).size.height
    }
    val titleHeightDp: Dp = with(density) { titleHeight.toDp() }
    val iconSize: Dp = 64.dp
    val iconOffset: Float by animateFloatAsState(
        targetValue = if (animateIconOffset) 0f else 1f,
        spring(stiffness = Spring.StiffnessLow)
    )
    val iconHeight: Dp by animateDpAsState(
        targetValue = if (animateTitleHeight) titleHeightDp else iconSize,
        spring(stiffness = Spring.StiffnessMediumLow)
    )
    val iconWidth: Dp by animateDpAsState(
        targetValue = if (animateTitleHeight) 24.dp else iconSize,
        spring(stiffness = Spring.StiffnessMediumLow)
    )
    val iconRotation: Float by animateFloatAsState(
        targetValue = if (animateTitleHeight) 1f else 0f
    )

    LaunchedEffect(Unit) {
        animateIconOffset = true
        delay(1000)
        animateTitleHeight = true
        delay(1000)
        showEnhancedLabel = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AnimatedVisibility(
                visible = iconHeight != titleHeightDp,
                exit = fadeOut(tween(700, 800)) + shrinkHorizontally(tween(700, 800), shrinkTowards = Alignment.Start)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(iconWidth)
                        .height(iconHeight)
                        .offset(x = iconOffset.dp * -50)
                        .graphicsLayer(
                            rotationZ = (1f - iconOffset) * 360,
                            alpha = 1f - iconOffset
                        )
                        .background(UISingleton.color4, CircleShape)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.rounded_school_24),
                        contentDescription = null,
                        tint = UISingleton.textColor4,
                        modifier = Modifier
                            .width(iconWidth)
                            .height(iconSize)
                            .graphicsLayer(
                                rotationZ = 360f * iconRotation,
                                scaleX = 1f - iconRotation,
                                scaleY = 1f - iconRotation
                            )
                            .background(UISingleton.color4, CircleShape)
                            .padding(6.dp)
                    )
                }
            }
            AnimatedVisibility(
                visible = iconHeight == titleHeightDp,
                enter = expandHorizontally(tween(500, 250)),
            ) {
                Box(
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                ) {
                    Text(
                        text = appName,
                        style = titleStyle
                    )
                    androidx.compose.animation.AnimatedVisibility(
                        visible = showEnhancedLabel,
                        enter = fadeIn(tween(1000, 1000)) + slideInHorizontally(tween(1000, 700)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = titleHeightDp)
                    ) {
                        Text(
                            text = "Enhanced",
                            fontSize = 24.sp.scaleIndependent,
                            color = UISingleton.textColor2,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Right,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}