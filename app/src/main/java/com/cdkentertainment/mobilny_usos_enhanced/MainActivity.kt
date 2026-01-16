package com.cdkentertainment.mobilny_usos_enhanced

import android.app.Activity
import android.content.Intent
import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import android.view.Window
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton.color1
import com.cdkentertainment.mobilny_usos_enhanced.ui.theme.MobilnyUSOSEnhancedTheme
import com.cdkentertainment.mobilny_usos_enhanced.view_models.FloatingButtonViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LoginPageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.ScreenManagerViewModel
import com.cdkentertainment.mobilny_usos_enhanced.views.FloatingButtonView
import com.cdkentertainment.mobilny_usos_enhanced.views.ScreenManager
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private val loginViewModel: LoginPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking {
            UserDataSingleton.readSettings(this@MainActivity)
        }
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb())
        )
        setContent {
            MobilnyUSOSEnhancedTheme {
                ContentView()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        intent.data?.let { uri ->
            if (
                uri.scheme == "mobile-usos-enhanced" &&
                uri.host == "login"
            ) {
                loginViewModel.handleRedirect(uri)
            }
        }
    }
}

@Composable
fun SetStatusBarIconsLight(window: Window, lightIcons: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val insetsController = WindowInsetsControllerCompat(window, view)
        insetsController.isAppearanceLightStatusBars = !lightIcons
        insetsController.isAppearanceLightNavigationBars = !lightIcons
    }
}

@Composable
fun currentWindow(): Window? {
    val context = LocalContext.current
    return (context as? Activity)?.window
}

@Composable
fun ContentView() {
    val window: Window? = currentWindow()
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
        targetValue = if (screenManagerViewModel.showFab) 0f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 100f
        )
    )
    val bgOverlayColor: Color by animateColorAsState(targetValue = if (fabViewModel.expanded) Color(0x32000000) else Color(TRANSPARENT))
    if (window != null) {
        SetStatusBarIconsLight(window, lightIcons = UISingleton.isDarkTheme)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = color1)
    ) {
        ScreenManager(screenManagerViewModel.selectedScreen, screenManagerViewModel)
        if (fabViewModel.expanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
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