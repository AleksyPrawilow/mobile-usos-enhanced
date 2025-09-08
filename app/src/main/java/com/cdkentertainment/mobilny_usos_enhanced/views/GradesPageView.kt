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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Course
import com.cdkentertainment.mobilny_usos_enhanced.models.Season
import com.cdkentertainment.mobilny_usos_enhanced.view_models.GradesPageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GradesPageView() {
    val gradesPageViewModel: GradesPageViewModel = viewModel<GradesPageViewModel>()
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }
    val paddingModifier: Modifier = Modifier.padding(horizontal = UISingleton.horizontalPadding)

    LaunchedEffect(Unit) {
        if (gradesPageViewModel.userGrades == null) {
            gradesPageViewModel.fetchUserGrades()
        }
        delay(150)
        showElements = true
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            PageHeaderView("Oceny")
        }
        item {
            AnimatedVisibility(gradesPageViewModel.userGrades == null, modifier = paddingModifier) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(color = UISingleton.textColor2, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
        if (gradesPageViewModel.userGrades != null) {
            for (iteration in gradesPageViewModel.userGrades!!.size - 1 downTo  0) {
                val season: Season = gradesPageViewModel.userGrades!![iteration]
                val subjectCount: Int = season.courseList.size
                stickyHeader {
                    AnimatedVisibility(showElements, enter = enterTransition(1), modifier = paddingModifier.then(Modifier.padding(top = UISingleton.verticalPadding))){
                        SemesterCardView(season.seasonId)
                    }
                }
                item {
                    AnimatedVisibility(showElements, enter = enterTransition(2), modifier = paddingModifier) {
                        GradeAverageView(season.avgGrade)
                    }
                }
                items(subjectCount) { courseIndex ->
                    val course: Course = season.courseList[courseIndex]
                    AnimatedVisibility(showElements, enter = enterTransition(3 + courseIndex), modifier = paddingModifier){
                        CourseGradesView(course, gradesPageViewModel.userSubjects!!, gradesPageViewModel.classtypeIdInfo)
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            item {
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GradesPagePreview() {
    OAuthSingleton.setTestAccessToken()
    val currentScreen: Screens = Screens.GRADES
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1)
            .padding(12.dp)
    ) {
        AnimatedContent(targetState = currentScreen) { target ->
            if (currentScreen == target) {
                GradesPageView()
            }
        }
    }
}