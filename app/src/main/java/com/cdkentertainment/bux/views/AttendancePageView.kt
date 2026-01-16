package com.cdkentertainment.bux.views

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.bux.R
import com.cdkentertainment.bux.UIHelper
import com.cdkentertainment.bux.UIHelper.scaleEnterTransition
import com.cdkentertainment.bux.UISingleton
import com.cdkentertainment.bux.getLocalized
import com.cdkentertainment.bux.models.SharedDataClasses
import com.cdkentertainment.bux.view_models.AttendancePageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AttendancePageView() {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val attendancePageViewModel: AttendancePageViewModel = viewModel()
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition

    var showElements by rememberSaveable { mutableStateOf(false) }
    var showPinned by rememberSaveable { mutableStateOf(false) }
    var showUnpinned by rememberSaveable { mutableStateOf(false) }
    var shownCourses by rememberSaveable { mutableStateOf(ShownCourses.PINNED) }

    val context = LocalContext.current

    val onPopupDismissRequest: () -> Unit = {
        attendancePageViewModel.dismissPopup()
    }

    val onUnpinnedPopupDismissRequest: () -> Unit = {
        attendancePageViewModel.dismissUnpinnedPopup()
    }

    val onRemovePin: () -> Unit = {
        coroutineScope.launch {
            try {
                attendancePageViewModel.removePin(
                    attendancePageViewModel.popupData!!.classGroupData,
                    context
                )
                attendancePageViewModel.dismissPopup()
            } catch (e: Exception) {
                val message = SharedDataClasses.LangDict(
                    "Something went wrong",
                    "Wystąpił błąd"
                )
                Toast.makeText(context, message.getLocalized(context), Toast.LENGTH_SHORT).show()
            }
        }
    }

    val onAddPin: () -> Unit = {
        coroutineScope.launch {
            try {
                attendancePageViewModel.pinGroup(
                    attendancePageViewModel.popupData!!.classGroupData,
                    context
                )
                attendancePageViewModel.dismissUnpinnedPopup()
            } catch (e: Exception) {
                val message = SharedDataClasses.LangDict(
                    "Something went wrong",
                    "Wystąpił błąd"
                )
                Toast.makeText(context, message.getLocalized(context), Toast.LENGTH_SHORT).show()
            }
        }
    }

    val paddingModifier = Modifier.padding(
        horizontal = UISingleton.horizontalPadding,
        vertical = 8.dp
    )

    val textMeasurer = rememberTextMeasurer()
    val cardLabels: List<Pair<String, ImageVector>> = listOf(
        stringResource(R.string.pinned_courses) to
                ImageVector.vectorResource(R.drawable.rounded_bookmark_star_24),
        stringResource(R.string.unpinned_courses) to
                ImageVector.vectorResource(R.drawable.rounded_bookmark_remove_24)
    )

    val cardLabelStyle = MaterialTheme.typography.titleLarge
    val maxCardWidth = remember(cardLabels, cardLabelStyle) {
        cardLabels.maxOf {
            textMeasurer.measure(
                text = AnnotatedString(it.first),
                style = cardLabelStyle
            ).size.width
        }
    }

    val courses = (attendancePageViewModel.lessonGroups ?: emptyList()).toMutableList()
    val pinnedCoursesMap = attendancePageViewModel.pinnedGroupedBySubject

    for (i in courses.indices) {
        courses[i] = courses[i].filter { group ->
            attendancePageViewModel.pinnedGroups.isNullOrEmpty() ||
                    !attendancePageViewModel.pinnedGroups!!.contains(group)
        }
    }

    val coursesCount = courses.sumOf { it.size }

    val onStart: () -> Unit = {
        coroutineScope.launch {
            showElements = false
            attendancePageViewModel.fetchLessonGroups()
            delay(150)
            showElements = true
            showPinned = true
        }
    }

    LaunchedEffect(Unit) {
        onStart()
    }

    if (attendancePageViewModel.showPopup) {
        AttendancePopupView(
            onDismissRequest = onPopupDismissRequest,
            onRemovePin = onRemovePin
        )
    }

    if (attendancePageViewModel.showUnpinnedPopup) {
        UnpinnedCoursePopupView(
            onDismissRequest = onUnpinnedPopupDismissRequest,
            onAddPin = onAddPin
        )
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        item {
            PageHeaderView(
                text = stringResource(R.string.attendance_page),
                icon = ImageVector.vectorResource(R.drawable.rounded_alarm_24)
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            AnimatedVisibility(
                visible = attendancePageViewModel.loading,
                modifier = paddingModifier
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
                visible = attendancePageViewModel.error,
                enter = enterTransition(1)
            ) {
                TextAndIconCardView(
                    stringResource(R.string.failed_to_fetch),
                    paddingModifier
                ) {
                    onStart()
                }
            }
        }

        if (attendancePageViewModel.lessonGroups != null) {
            item {
                FlowRow(
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp)
                ) {
                    AnimatedVisibility(
                        showElements && attendancePageViewModel.loaded,
                        enter = scaleEnterTransition(1)
                    ) {
                        LatestSomethingView(
                            icon = cardLabels[0].second,
                            title = cardLabels[0].first,
                            maxWidth = maxCardWidth,
                            maxLines = 2,
                            disabledTextColor = UISingleton.textColor4,
                            disabledBackgroundColor = UISingleton.color3,
                            enabled = shownCourses != ShownCourses.PINNED
                        ) {
                            coroutineScope.launch {
                                shownCourses = ShownCourses.PINNED
                                showUnpinned = false
                                delay(50)
                                showPinned = true
                            }
                        }
                    }

                    AnimatedVisibility(
                        showElements && attendancePageViewModel.loaded,
                        enter = scaleEnterTransition(2)
                    ) {
                        LatestSomethingView(
                            icon = cardLabels[1].second,
                            title = cardLabels[1].first,
                            maxWidth = maxCardWidth,
                            badge = if (coursesCount == 0) null else "$coursesCount!",
                            maxLines = 2,
                            disabledTextColor = UISingleton.textColor4,
                            disabledBackgroundColor = UISingleton.color3,
                            enabled = shownCourses != ShownCourses.UNPINNED
                        ) {
                            coroutineScope.launch {
                                shownCourses = ShownCourses.UNPINNED
                                showPinned = false
                                delay(50)
                                showUnpinned = true
                            }
                        }
                    }
                }
            }

            if (shownCourses == ShownCourses.UNPINNED) {
                if (courses.isNotEmpty()) {
                    items(courses.size) { courseIndex ->
                        val courseUnits = courses[courseIndex]
                        AnimatedVisibility(
                            showElements && showUnpinned && attendancePageViewModel.loaded,
                            enter = enterTransition(4 + courseIndex)
                        ) {
                            if (courseUnits.isNotEmpty()) {
                                GroupedContentContainerView(
                                    title = courseUnits.first().course_name.getLocalized(context),
                                    modifier = paddingModifier
                                ) {
                                    for (course in courseUnits) {
                                        GradeCardView(
                                            courseName = UIHelper.classTypeIds[course.class_type_id]
                                                ?.name?.getLocalized(context)
                                                ?: course.class_type_id,
                                            grade = courseUnits.first().group_number.toString(),
                                            showArrow = true,
                                            showGrade = false,
                                            sideIcon = ImageVector.vectorResource(
                                                UIHelper.activityTypeIconMapping[course.class_type_id]
                                                    ?: UIHelper.otherIcon
                                            )
                                        ) {
                                            attendancePageViewModel.showUnpinnedPopup(course)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (coursesCount == 0) {
                    item {
                        AnimatedVisibility(
                            showElements && showUnpinned && attendancePageViewModel.loaded,
                            enter = enterTransition(4)
                        ) {
                            TextAndIconCardView(
                                title = stringResource(R.string.no_unpinned_courses),
                                icon = Icons.Rounded.Done,
                                modifier = paddingModifier,
                                backgroundColor = UISingleton.color2
                            )
                        }
                    }
                }
            } else {
                if (!pinnedCoursesMap.isNullOrEmpty()) {
                    val keys = pinnedCoursesMap.keys.toList()
                    items(keys.size) { keyIndex ->
                        val courseUnits = pinnedCoursesMap[keys[keyIndex]] ?: emptyList()
                        AnimatedVisibility(
                            showElements && showPinned && attendancePageViewModel.loaded,
                            enter = enterTransition(4 + keyIndex)
                        ) {
                            CourseContainerView(courseUnits, modifier = paddingModifier) { unit ->
                                AttendanceClassGroupView(unit) {
                                    attendancePageViewModel.showPopup(unit)
                                }
                            }
                        }
                    }
                } else {
                    item {
                        AnimatedVisibility(
                            showElements && showPinned && attendancePageViewModel.loaded,
                            enter = enterTransition(4)
                        ) {
                            TextAndIconCardView(
                                title = stringResource(R.string.no_pinned_courses),
                                icon = Icons.Rounded.Close,
                                modifier = paddingModifier,
                                backgroundColor = UISingleton.color2
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }

        item { Spacer(modifier = Modifier.height(64.dp)) }
    }
}

private enum class ShownCourses {
    PINNED,
    UNPINNED
}