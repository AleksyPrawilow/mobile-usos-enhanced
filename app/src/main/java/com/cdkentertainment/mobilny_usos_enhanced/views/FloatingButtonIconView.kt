package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.view_models.FloatingButtonViewModel

@Composable
fun FloatingButtonIconView(viewModel: FloatingButtonViewModel, color: Color) {
    val offset: Int by animateIntAsState(
        targetValue = if (viewModel.expanded) 24 else 0,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = if (viewModel.expanded) 150 else 350,
            easing = EaseInOutBack
        )
    )
    val opacity: Float by animateFloatAsState(
        targetValue = if (viewModel.expanded) 0f else 1f,
        animationSpec = tween(
            durationMillis = 750,
            delayMillis = if (viewModel.expanded) 250 else 350
        )
    )
    val offset2: Int by animateIntAsState(
        targetValue = if (viewModel.expanded) 24 else 0,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = if (viewModel.expanded) 300 else 500,
            easing = EaseInOutBack
        )
    )
    val crossAngle: Float by animateFloatAsState(
        targetValue = if (viewModel.expanded) 45f else 0f,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = if (viewModel.expanded) 250 else 0,
            easing = EaseInOutBack
        )
    )
    Box(
        modifier = Modifier
            .size((72).dp)
            .graphicsLayer(
                rotationZ = crossAngle * 2
            )
            .padding(horizontal = (72 / 4).dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = opacity
                }
        ) {
            HorizontalDivider(
                thickness = 5.dp,
                color = color,
                modifier = Modifier
                    .offset(offset.dp, 12.dp)
                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                    .align(Alignment.Center)
            )
            HorizontalDivider(
                thickness = 5.dp,
                color = color,
                modifier = Modifier
                    .offset(offset2.dp, -12.dp)
                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                    .align(Alignment.Center)
            )
        }
        for (index in 0..1) {
            HorizontalDivider(
                thickness = 5.dp,
                color = color,
                modifier = Modifier
                    .graphicsLayer(
                        rotationZ = if (index == 0) crossAngle else -crossAngle
                    )
                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FloatingButtonIconPreview() {
    val viewModel: FloatingButtonViewModel = viewModel<FloatingButtonViewModel>()
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1.primaryColor)
    ) {
        FloatingButtonIconView(viewModel, UISingleton.color4.primaryColor)
        Button(
            onClick = {
                viewModel.changeExpanded(!viewModel.expanded)
            },
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Text(
                text = "change"
            )
        }
    }
}