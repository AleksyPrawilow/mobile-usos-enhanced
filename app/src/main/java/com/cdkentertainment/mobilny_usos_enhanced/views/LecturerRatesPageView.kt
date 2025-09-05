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
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LecturerRatesPageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LecturerRatesPageView() {
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    val lecturerRatesPageViewModel: LecturerRatesPageViewModel = viewModel<LecturerRatesPageViewModel>()
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }
    var showDetails: Boolean by rememberSaveable { mutableStateOf(false) }

    val onPopupDismiss: () -> Unit = {
        UISingleton.dropBlurContent()
        showDetails = false
    }

    LaunchedEffect(Unit) {
        delay(150)
        showElements = true
    }

    if (showDetails) {
        LecturerInfoPopupView(lecturerRatesPageViewModel.selectedLecturer!!, onDismissRequest = onPopupDismiss)
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
                    text = "Prowadzący",
                    style = MaterialTheme.typography.headlineLarge,
                    color = UISingleton.textColor1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
        for (index in 0 until 5) {
            item {
                val lecturer: SharedDataClasses.Human = SharedDataClasses.Human(
                    id = "$index",
                    first_name = "Jan",
                    last_name = "Kowalski"
                )
                AnimatedVisibility(showElements, enter = enterTransition(1 + index)) {
                    LecturerCardView(index, lecturer) {
                        UISingleton.blurContent()
                        lecturerRatesPageViewModel.selectLecturer(lecturer)
                        showDetails = true
                    }
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
fun LecturerRatesPagePreview() {
    OAuthSingleton.setTestAccessToken()
    val currentScreen: Screens = Screens.LECTURERS
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1)
            .padding(12.dp)
    ) {
        AnimatedContent(targetState = currentScreen) { target ->
            if (currentScreen == target) {
                LecturerRatesPageView()
            }
        }
    }
}