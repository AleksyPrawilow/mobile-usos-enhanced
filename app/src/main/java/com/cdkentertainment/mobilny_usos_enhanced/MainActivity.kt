package com.cdkentertainment.mobilny_usos_enhanced

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.ui.theme.MobilnyUSOSEnhancedTheme
import com.cdkentertainment.mobilny_usos_enhanced.view_models.FloatingButtonViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.ScreenManagerViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.VisibleItemsViewModel
import com.cdkentertainment.mobilny_usos_enhanced.views.FloatingButtonView
import com.cdkentertainment.mobilny_usos_enhanced.views.ScreenManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobilnyUSOSEnhancedTheme {
                ContentView()
            }
        }
    }
}

@Composable
fun ContentView() {
    val screenManagerViewModel: ScreenManagerViewModel = viewModel<ScreenManagerViewModel>()
    val visibleItemsViewModel: VisibleItemsViewModel = viewModel<VisibleItemsViewModel>()
    screenManagerViewModel.visibleItemsViewModel = visibleItemsViewModel
    val fabViewModel: FloatingButtonViewModel = viewModel<FloatingButtonViewModel>()
    val fabHorizonalBias: Float by animateFloatAsState(
        targetValue = if (fabViewModel.expanded) 0f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 100f
        )
    )
    val fabVerticalBias: Float by animateFloatAsState(
        targetValue = if (fabViewModel.expanded) 0f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = 100f
        )
    )
    val fabOffsetRatio: Float by animateFloatAsState(
        targetValue = if (screenManagerViewModel.authorized) 0f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 100f
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = UISingleton.color1.primaryColor)
            .padding(12.dp)
    ) {
        ScreenManager(screenManagerViewModel.selectedScreen, screenManagerViewModel, visibleItemsViewModel)
        FloatingButtonView(
            fabViewModel = viewModel<FloatingButtonViewModel>(),
            screenManagerViewModel = viewModel<ScreenManagerViewModel>(),
            modifier = Modifier
                .align(
                    BiasAlignment(
                        horizontalBias = fabHorizonalBias,
                        verticalBias = fabVerticalBias
                    )
                )
                .offset(x = 96.dp * fabOffsetRatio)
                .graphicsLayer(scaleX = 1 - fabOffsetRatio, scaleY = 1 - fabOffsetRatio)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContentViewPreview() {
    ContentView()
}