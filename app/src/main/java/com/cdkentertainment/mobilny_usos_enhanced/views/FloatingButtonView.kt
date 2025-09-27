package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.spToDp
import com.cdkentertainment.mobilny_usos_enhanced.view_models.FloatingButtonViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.ScreenManagerViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun FloatingButtonView(
    fabViewModel: FloatingButtonViewModel,
    screenManagerViewModel: ScreenManagerViewModel,
    modifier: Modifier = Modifier
) {
    val buttonSize = 72
    val subButtonSize = 58
    val circleRadius = 110.0
    val iconRotation: Float by animateFloatAsState(
        targetValue = if (fabViewModel.expanded) -45.0f else 0.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    val iconYRotation: Float by animateFloatAsState(
        targetValue = if (screenManagerViewModel.authorized) 360f else 0f,
        animationSpec = tween(
            durationMillis = 750,
            easing = EaseOut
        )
    )
    val color2: Color =UISingleton.color2
    val color4: Color = UISingleton.textColor1
    val buttonScale: Float by animateFloatAsState(if (fabViewModel.expanded) 1.25f else 1.0f)
    val subButtonDelayNormal: Int = if (fabViewModel.expanded) 150 else 0
    val subButtonDelayIncrement: Int = 25
    val subButtonAnimDuration: Int = 250
    val easing: Easing = EaseOutQuad
    val subButtonTargetValue: Float = if (fabViewModel.expanded) 1f else 0f
    val subButtonOffset1: Float by animateFloatAsState(subButtonTargetValue, tween(subButtonAnimDuration, if (fabViewModel.expanded) subButtonDelayNormal + subButtonDelayIncrement * 0 else 0, easing = easing))
    val subButtonOffset2: Float by animateFloatAsState(subButtonTargetValue, tween(subButtonAnimDuration, if (fabViewModel.expanded) subButtonDelayNormal + subButtonDelayIncrement * 1 else 0, easing = easing))
    val subButtonOffset3: Float by animateFloatAsState(subButtonTargetValue, tween(subButtonAnimDuration, if (fabViewModel.expanded) subButtonDelayNormal + subButtonDelayIncrement * 2 else 0, easing = easing))
    val subButtonOffset4: Float by animateFloatAsState(subButtonTargetValue, tween(subButtonAnimDuration, if (fabViewModel.expanded) subButtonDelayNormal + subButtonDelayIncrement * 3 else 0, easing = easing))
    val subButtonOffset5: Float by animateFloatAsState(subButtonTargetValue, tween(subButtonAnimDuration, if (fabViewModel.expanded) subButtonDelayNormal + subButtonDelayIncrement * 4 else 0, easing = easing))
    val subButtonOffset6: Float by animateFloatAsState(subButtonTargetValue, tween(subButtonAnimDuration, if (fabViewModel.expanded) subButtonDelayNormal + subButtonDelayIncrement * 5 else 0, easing = easing))
    val subButtonOffset7: Float by animateFloatAsState(subButtonTargetValue, tween(subButtonAnimDuration, if (fabViewModel.expanded) subButtonDelayNormal + subButtonDelayIncrement * 6 else 0, easing = easing))
    val subButtonOffset8: Float by animateFloatAsState(subButtonTargetValue, tween(subButtonAnimDuration, if (fabViewModel.expanded) subButtonDelayNormal + subButtonDelayIncrement * 7 else 0, easing = easing))
    val subButtonOffset9: Float by animateFloatAsState(subButtonTargetValue, tween(subButtonAnimDuration, if (fabViewModel.expanded) subButtonDelayNormal + subButtonDelayIncrement * 8 else 0, easing = easing))
    val subButtonOffsetsRatios: List<Float> = listOf(subButtonOffset1, subButtonOffset2, subButtonOffset3, subButtonOffset4, subButtonOffset5, subButtonOffset6, subButtonOffset7, subButtonOffset8, subButtonOffset9)

    val context: Context = LocalContext.current

    repeat(9) { index ->
        val angle: Double = index * 40.0 - 90.0
        val x = cos(toRadians(angle)) * circleRadius
        val y = sin(toRadians(angle)) * circleRadius
        val image: ImageVector = when(index) {
            0 -> ImageVector.vectorResource(R.drawable.rounded_home_24)
            1 -> ImageVector.vectorResource(R.drawable.rounded_star_24)
            2 -> ImageVector.vectorResource(R.drawable.rounded_assignment_24)
            3 -> ImageVector.vectorResource(R.drawable.rounded_calendar_month_24)
            4 -> ImageVector.vectorResource(R.drawable.rounded_group_24)
            5 -> ImageVector.vectorResource(R.drawable.rounded_payments_24)
            6 -> ImageVector.vectorResource(R.drawable.rounded_alarm_24)
            7 -> ImageVector.vectorResource(R.drawable.rounded_school_24)
            else -> ImageVector.vectorResource(R.drawable.rounded_settings_24)
        }

        Box(
            modifier = Modifier
                .offset(x = x.dp * subButtonOffsetsRatios[index], y = y.dp * subButtonOffsetsRatios[index])
                .graphicsLayer(
                    transformOrigin = TransformOrigin.Center,
                    scaleX = subButtonOffsetsRatios[index] * if (screenManagerViewModel.selectedScreen.ordinal == index + 1) 1.15f else 1.0f,
                    scaleY = subButtonOffsetsRatios[index] * if (screenManagerViewModel.selectedScreen.ordinal == index + 1) 1.15f else 1.0f
                )
                .size(subButtonSize.dp)
                .shadow(5.dp, CircleShape, clip = true)
                .background(UISingleton.color2, CircleShape)
                .clickable(onClick = {
                    fabViewModel.changeExpanded(false)
                    screenManagerViewModel.changeScreen(Screens.fromOrdinal(index + 1)!!, context)
                })
                .border(5.dp, UISingleton.textColor1, CircleShape)
                .then(modifier)
        ) {
            Icon(
                imageVector = image,
                contentDescription = "Section ${index + 1}",
                tint = UISingleton.textColor1,
                modifier = Modifier
                    .padding(16.dp)
                    .size((subButtonSize * 0.5).dp)
                    .align(Alignment.Center)
            )
        }
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .offset(x = x.dp * subButtonOffsetsRatios[index], y = y.dp * subButtonOffsetsRatios[index] + 26.dp)
                .graphicsLayer(
                    transformOrigin = TransformOrigin.Center,
                    scaleX = subButtonOffsetsRatios[index] * if (screenManagerViewModel.selectedScreen.ordinal == index + 1) 1.15f else 1.0f,
                    scaleY = subButtonOffsetsRatios[index] * if (screenManagerViewModel.selectedScreen.ordinal == index + 1) 1.15f else 1.0f
                )
                .defaultMinSize(minWidth = subButtonSize.dp)
                .width(intrinsicSize = IntrinsicSize.Min)
                .then(modifier)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .height(spToDp(10.sp) * 2)
                    .background(
                        color = color4,
                        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
                    )
            )
            Text(
                text = stringResource(Screens.fromOrdinal(index + 1)!!.pageName),
                fontWeight = if (screenManagerViewModel.selectedScreen.ordinal == index + 1) FontWeight.ExtraBold else FontWeight.SemiBold,
                color = if (screenManagerViewModel.selectedScreen.ordinal == index + 1) UISingleton.textColor4 else UISingleton.textColor3,
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
            )
        }
    }

    IconButton(
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = UISingleton.textColor1,
            containerColor = color2
        ),
        onClick = {
            fabViewModel.changeExpanded(!fabViewModel.expanded)
        },
        modifier = Modifier
            .padding(16.dp)
            .graphicsLayer(
                scaleX = buttonScale,
                scaleY = buttonScale,
                transformOrigin = TransformOrigin.Center
            )
            .size(buttonSize.dp)
            .then(other = modifier)
            .shadow(5.dp, CircleShape)
            .border(5.dp, color4, CircleShape)
    ) {
        FloatingButtonIconView(fabViewModel, color4)
    }
}