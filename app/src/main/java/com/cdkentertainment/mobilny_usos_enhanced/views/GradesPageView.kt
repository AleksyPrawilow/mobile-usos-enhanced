package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.CourseUnitData
import com.cdkentertainment.mobilny_usos_enhanced.models.Season
import com.cdkentertainment.mobilny_usos_enhanced.view_models.GradesPageViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GradesPageView() {
    val gradesPageViewModel: GradesPageViewModel = viewModel<GradesPageViewModel>()
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }

    val paddingModifier: Modifier = Modifier.padding(
        horizontal = UISingleton.horizontalPadding,
        vertical = 8.dp
    )

    LaunchedEffect(Unit) {
        gradesPageViewModel.suspendFetchSemesterGrades(UIHelper.termIds.last().id)
        delay(150)
        showElements = true

        for (semester in UIHelper.termIds.reversed()) {
            gradesPageViewModel.fetchSemesterGrades(semester.id)
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        item {
            PageHeaderView(
                text = stringResource(R.string.grade_page),
                icon = ImageVector.vectorResource(R.drawable.rounded_star_24)
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            AnimatedVisibility(!showElements, modifier = paddingModifier) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        color = UISingleton.textColor2,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        for (semester in UIHelper.termIds.reversed()) {
            val season: Season? = gradesPageViewModel.userSubjects[semester.id]

            stickyHeader {
                AnimatedVisibility(showElements && season != null, enter = enterTransition(1)) {
                    SemesterCardView(
                        semester,
                        modifier = paddingModifier
                    )
                }
            }

            item {
                AnimatedVisibility(
                    visible = gradesPageViewModel.loadingMap[semester.id] == true && showElements
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            color = UISingleton.textColor2,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            item {
                AnimatedVisibility(
                    visible = gradesPageViewModel.errorMap[semester.id] == true && showElements
                ) {
                    TextAndIconCardView(
                        title = stringResource(R.string.failed_to_fetch),
                        icon = Icons.Rounded.Refresh,
                        showArrow = true,
                        modifier = paddingModifier
                    ) {
                        gradesPageViewModel.fetchSemesterGrades(semester.id)
                    }
                }
            }

            item {
                AnimatedVisibility(
                    visible = gradesPageViewModel.loadedMap[semester.id] == true && showElements,
                    enter = enterTransition(2)
                ) {
                    if (season != null) {
                        GradeAverageView(
                            season.avgGrade,
                            modifier = paddingModifier
                        )
                    }
                }
            }

            if (season != null) {
                for (course in 0 until season.courseList.size) {
                    item {
                        val nameMap: Map<String, CourseUnitData>? =
                            gradesPageViewModel.userSubjects[semester.id]?.courseUnitIds

                        AnimatedVisibility(
                            visible = gradesPageViewModel.loadedMap[semester.id] == true && showElements,
                            enter = enterTransition(3 + course)
                        ) {
                            if (nameMap != null) {
                                CourseGradesView(
                                    data = season.courseList[course],
                                    nameMap = nameMap,
                                    modifier = paddingModifier
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }

        item { Spacer(modifier = Modifier.height(64.dp)) }
    }
}