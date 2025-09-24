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
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LessonGroupPageViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClassGroupsPageView() {
    val groupsPageViewModel: LessonGroupPageViewModel = viewModel<LessonGroupPageViewModel>()
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }

    val density: Density = LocalDensity.current
    val insets = WindowInsets.systemBars
    val topInset = insets.getTop(density)
    val bottomInset = insets.getBottom(density)
    val topPadding = with(density) { topInset.toDp() }
    val bottomPadding = with(density) { bottomInset.toDp() }
    val paddingModifier: Modifier = Modifier.padding(horizontal = UISingleton.horizontalPadding, vertical = 8.dp)

    LaunchedEffect(Unit) {
        groupsPageViewModel.fetchLessonGroups()
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
                text = stringResource(R.string.class_groups_page_header),
                icon = ImageVector.vectorResource(R.drawable.rounded_group_24)
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            AnimatedVisibility(groupsPageViewModel.lessonGroups == null, modifier = paddingModifier) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(color = UISingleton.textColor2, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
        if (groupsPageViewModel.lessonGroups != null) {
            for (seasonId in groupsPageViewModel.lessonGroups!!.groups.keys.reversed()) {
                val season: Map<String, List<LessonGroup>>? =
                    groupsPageViewModel.lessonGroups!!.groups[seasonId]
                var groupCount: Int = 0
                val courses: List<List<LessonGroup>> = season?.values?.toList() ?: emptyList()
                for (course in courses) {
                    groupCount += course.size
                }
                stickyHeader {
                    AnimatedVisibility(
                        showElements,
                        enter = enterTransition(1),
                    ) {
                        SemesterCardView(seasonId, modifier = paddingModifier)
                    }
                }
                if (season != null) {
                    items(courses.size) { courseIndex ->
                        val courseUnits: List<LessonGroup> = courses[courseIndex]
                        AnimatedVisibility(
                            showElements,
                            enter = enterTransition(2 + courseIndex),
                        ) {
                            CourseContainerView(courseUnits, modifier = paddingModifier) { unit ->
                                ClassGroupView(unit)
                            }
                        }
                    }
                } else {
                    item {
                        AnimatedVisibility(
                            showElements,
                            enter = enterTransition(2),
                            modifier = paddingModifier
                        ) {
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
        }
        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}