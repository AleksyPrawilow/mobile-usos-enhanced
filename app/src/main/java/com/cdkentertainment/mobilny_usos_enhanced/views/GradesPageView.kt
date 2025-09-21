package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Course
import com.cdkentertainment.mobilny_usos_enhanced.models.Season
import com.cdkentertainment.mobilny_usos_enhanced.view_models.GradesPageViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GradesPageView() {
    val gradesPageViewModel: GradesPageViewModel = viewModel<GradesPageViewModel>()
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }
    val paddingModifier: Modifier = Modifier.padding(horizontal = UISingleton.horizontalPadding, vertical = 8.dp)

    val density: Density = LocalDensity.current
    val insets = WindowInsets.systemBars
    val topInset = insets.getTop(density)
    val bottomInset = insets.getBottom(density)
    val topPadding = with(density) { topInset.toDp() }
    val bottomPadding = with(density) { bottomInset.toDp() }

    LaunchedEffect(Unit) {
        if (gradesPageViewModel.userGrades == null) {
            gradesPageViewModel.fetchUserGrades()
        }
        delay(150)
        showElements = true
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = topPadding,
                bottom = bottomPadding
            )
    ) {
        item {
            PageHeaderView(
                text = stringResource(R.string.grade_page),
                icon = ImageVector.vectorResource(R.drawable.rounded_star_24)
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
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
                    AnimatedVisibility(showElements, enter = enterTransition(1)) {
                        SemesterCardView(season.seasonId, modifier = paddingModifier)
                    }
                }
                item {
                    AnimatedVisibility(showElements, enter = enterTransition(2)) {
                        GradeAverageView(season.avgGrade, modifier = paddingModifier)
                    }
                }
                items(subjectCount) { courseIndex ->
                    val course: Course = season.courseList[courseIndex]
                    AnimatedVisibility(showElements, enter = enterTransition(3 + courseIndex)){
                        CourseGradesView(course, gradesPageViewModel.userSubjects!!, gradesPageViewModel.classtypeIdInfo, modifier = paddingModifier)
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