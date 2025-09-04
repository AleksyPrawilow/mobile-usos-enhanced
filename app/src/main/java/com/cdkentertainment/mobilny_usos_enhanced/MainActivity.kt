package com.cdkentertainment.mobilny_usos_enhanced

import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.ui.theme.MobilnyUSOSEnhancedTheme
import com.cdkentertainment.mobilny_usos_enhanced.view_models.FloatingButtonViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.ScreenManagerViewModel
import com.cdkentertainment.mobilny_usos_enhanced.views.FloatingButtonView
import com.cdkentertainment.mobilny_usos_enhanced.views.ScreenManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                UserDataSingleton.readSettings(context)
            }
            MobilnyUSOSEnhancedTheme {
                ContentView()
            }
        }
    }
}

@Composable
fun ContentView() {
    val screenManagerViewModel: ScreenManagerViewModel = viewModel<ScreenManagerViewModel>()
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
    val bgOverlayColor: Color by animateColorAsState(targetValue = if (fabViewModel.expanded) Color(0x32000000) else Color(TRANSPARENT))

//    val blurRadius: Dp by animateDpAsState(UISingleton.blurRadius)
    val color1: Color by animateColorAsState(UISingleton.color1.primaryColor)
    val color2: Color by animateColorAsState(UISingleton.color2.primaryColor)
    val color3: Color by animateColorAsState(UISingleton.color3.primaryColor)
    val color4: Color by animateColorAsState(UISingleton.color4.primaryColor)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = color1)
//            .blur(blurRadius)
            .padding(12.dp)
    ) {
        ScreenManager(screenManagerViewModel.selectedScreen, screenManagerViewModel)
        if (fabViewModel.expanded) {
            Box(
                modifier = Modifier
                    //TODO: What even is this kind of logic?? Extremelly hacky and needs to be fixed
                    .requiredWidth(LocalConfiguration.current.screenWidthDp.dp + 32.dp)
                    .requiredHeight(LocalConfiguration.current.screenHeightDp.dp + 128.dp)
                    .background(bgOverlayColor)
                    .clickable(
                        onClick = {
                            fabViewModel.changeExpanded(false)
                        }
                    )
            )
        }
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