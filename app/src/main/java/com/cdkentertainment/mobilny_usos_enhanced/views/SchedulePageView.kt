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
import com.cdkentertainment.mobilny_usos_enhanced.view_models.SchedulePageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import kotlinx.coroutines.delay
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SchedulePageView() {
    val schedulePageViewModel: SchedulePageViewModel = viewModel<SchedulePageViewModel>()
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(150)
        showElements = true
        if (schedulePageViewModel.schedule == null) {
            schedulePageViewModel.fetchWeekData(LocalDate.of(2025, 5, 13))
        }
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
                    text = "Rozkład zajęć",
                    style = MaterialTheme.typography.headlineLarge,
                    color = UISingleton.color4.primaryColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
        stickyHeader {
            AnimatedVisibility(showElements, enter = enterTransition(1)) {
                ScheduleDaySelectorView(schedulePageViewModel)
            }
        }
        item {
            TimetableView(schedulePageViewModel)
        }
        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SchedulePagePreview() {
    OAuthSingleton.setTestAccessToken()
    val currentScreen: Screens = Screens.GRADES
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1.primaryColor)
            .padding(12.dp)
    ) {
        AnimatedContent(targetState = currentScreen) { target ->
            if (currentScreen == target) {
                SchedulePageView()
            }
        }
    }
}