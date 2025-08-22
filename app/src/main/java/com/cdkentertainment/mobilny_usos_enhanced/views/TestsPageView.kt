package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import com.cdkentertainment.mobilny_usos_enhanced.view_models.TestsPageViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestsPageView() {
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition

    val testsPageViewModel: TestsPageViewModel = viewModel<TestsPageViewModel>()
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        testsPageViewModel.fetchTests()
        delay(150)
        showElements = true
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(0)) {
                Text(
                    text = "Sprawdziany",
                    style = MaterialTheme.typography.headlineLarge,
                    color = UISingleton.color4.primaryColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
        item {
            Spacer(Modifier.height(16.dp))
        }
        stickyHeader {
            AnimatedVisibility(showElements, enter = enterTransition(1)) {
                SemesterCardView("2025/SL")
            }
        }
        for (index in 0 until 5) {
            item {
                AnimatedVisibility(showElements, enter = enterTransition(2 + index)) {
                    TestCardView()
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestsPagePreview() {
    OAuthSingleton.setTestAccessToken()
    val currentScreen: Screens = Screens.HOME
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1.primaryColor)
            .padding(12.dp)
    ) {
        AnimatedContent(targetState = currentScreen) { target ->
            if (currentScreen == target) {
                TestsPageView()
            }
        }
    }
}