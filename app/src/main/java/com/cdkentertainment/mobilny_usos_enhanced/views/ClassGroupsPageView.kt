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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LessonGroupPageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClassGroupsPageView() {
    val groupsPageViewModel: LessonGroupPageViewModel = viewModel<LessonGroupPageViewModel>()
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        groupsPageViewModel.fetchLessonGroups()
        delay(150)
        showElements = true
    }

    if (groupsPageViewModel.lessonGroups == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(color = UISingleton.textColor2, modifier = Modifier.align(Alignment.Center))
        }
    } else {
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
                        text = "Grupy zajęciowe",
                        style = MaterialTheme.typography.headlineLarge,
                        color = UISingleton.textColor1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
            for (seasonId in groupsPageViewModel.lessonGroups!!.groups.keys.reversed()) {
                val season: Map<String, List<LessonGroup>>? = groupsPageViewModel.lessonGroups!!.groups[seasonId]
                var groupCount: Int = 0
                val courses: List<List<LessonGroup>> = season?.values?.toList() ?: emptyList()
                for (course in courses) {
                    groupCount += course.size
                }
                stickyHeader {
                    AnimatedVisibility(showElements, enter = enterTransition(1)) {
                        SemesterCardView(seasonId)
                    }
                }
                if (season != null) {
                    items(courses.size) { courseIndex ->
                        val courseUnits: List<LessonGroup> = courses[courseIndex]
                        AnimatedVisibility(showElements, enter = enterTransition(2 + courseIndex)) {
                            CourseContainerView(courseUnits) { unit ->
                                ClassGroupView(unit)
                            }
                        }
                    }
                } else {
                    item {
                        AnimatedVisibility(showElements, enter = enterTransition(2)) {
                            Text(
                                text = "Brak grup zajęciowych w tym semestrze",
                                style = MaterialTheme.typography.titleLarge,
                                color = UISingleton.textColor1,
                            )
                        }
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
fun ClassGroupsPagePreview() {
    OAuthSingleton.setTestAccessToken()
    val currentScreen: Screens = Screens.GROUPS
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1)
            .padding(12.dp)
    ) {
        AnimatedContent(targetState = currentScreen) { target ->
            if (currentScreen == target) {
                ClassGroupsPageView()
            }
        }
    }
}