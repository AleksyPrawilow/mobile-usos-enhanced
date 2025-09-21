package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper.scaleEnterTransition
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup
import com.cdkentertainment.mobilny_usos_enhanced.view_models.AttendancePageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AttendancePageView() {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val attendancePageViewModel: AttendancePageViewModel = viewModel<AttendancePageViewModel>()
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }
    var showPinned: Boolean by rememberSaveable { mutableStateOf(false) }
    var showUnpinned: Boolean by rememberSaveable { mutableStateOf(false) }
    var shownCourses: ShownCourses by rememberSaveable { mutableStateOf(ShownCourses.PINNED) }

    val context: Context = LocalContext.current
    val onPopupDismissRequest: () -> Unit = {
        attendancePageViewModel.dismissPopup()
    }

    val onUnpinnedPopupDismissRequest: () -> Unit = {
        attendancePageViewModel.dismissUnpinnedPopup()
    }

    val onRemovePin: () -> Unit = {
        CoroutineScope(Dispatchers.IO).launch {
            attendancePageViewModel.removePin(attendancePageViewModel.popupData!!.classGroupData, context)
            attendancePageViewModel.dismissPopup()
        }
    }

    val onAddPin: () -> Unit = {
        CoroutineScope(Dispatchers.IO).launch {
            attendancePageViewModel.pinGroup(attendancePageViewModel.popupData!!.classGroupData, context)
            attendancePageViewModel.dismissUnpinnedPopup()
        }
    }

    val density: Density = LocalDensity.current
    val insets = WindowInsets.systemBars
    val topInset = insets.getTop(density)
    val bottomInset = insets.getBottom(density)
    val topPadding = with(density) { topInset.toDp() }
    val bottomPadding = with(density) { bottomInset.toDp() }
    val paddingModifier: Modifier = Modifier.padding(horizontal = UISingleton.horizontalPadding, vertical = 8.dp)

    val textMeasurer = rememberTextMeasurer()
    val cardLabels: List<Pair<String, ImageVector>> = listOf(
        Pair(stringResource(R.string.pinned_courses), ImageVector.vectorResource(R.drawable.rounded_bookmark_star_24)),
        Pair(stringResource(R.string.unpinned_courses), ImageVector.vectorResource(R.drawable.rounded_bookmark_remove_24)),
    )
    val cardLabelStyle: TextStyle = MaterialTheme.typography.titleLarge
    val maxCardWidth: Int = remember(cardLabels, cardLabelStyle) {
        cardLabels.maxOf {
            textMeasurer.measure(
                text = AnnotatedString(it.first),
                style = cardLabelStyle
            ).size.width
        }
    }

    val courses: MutableList<List<LessonGroup>> = (attendancePageViewModel.lessonGroups ?: emptyList()).toMutableList()
    val pinnedCoursesMap: Map<String, List<LessonGroup>>? = attendancePageViewModel.pinnedGroupedBySubject
    for (listIndex in 0..<courses.size) {
        courses[listIndex] = courses[listIndex].filter { courseUnit ->
            if (attendancePageViewModel.pinnedGroups.isNullOrEmpty()) {
                true
            } else {
                !attendancePageViewModel.pinnedGroups!!.contains(courseUnit)
            }
        }
    }
    val coursesCount: Int = courses.sumOf { it.size }

    LaunchedEffect(Unit) {
        attendancePageViewModel.fetchLessonGroups()
        delay(150)
        showElements = true
        showPinned = true
    }

    if (attendancePageViewModel.showPopup) {
        AttendancePopupView(viewModel = attendancePageViewModel, onDismissRequest = onPopupDismissRequest, onRemovePin = onRemovePin)
    }

    if (attendancePageViewModel.showUnpinnedPopup) {
        UnpinnedCoursePopupView(viewModel = attendancePageViewModel, onDismissRequest = onUnpinnedPopupDismissRequest, onAddPin = onAddPin)
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
                text = stringResource(R.string.attendance_page),
                icon = ImageVector.vectorResource(R.drawable.rounded_alarm_24)
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            AnimatedVisibility(attendancePageViewModel.lessonGroups == null, modifier = paddingModifier) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(color = UISingleton.textColor2, modifier = Modifier.align(Alignment.Center))
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
                    AnimatedVisibility(showElements, enter = scaleEnterTransition(1)) {
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
                    AnimatedVisibility(showElements, enter = scaleEnterTransition(2)) {
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
                if (!courses.isEmpty()) {
                    items(courses.size) { courseIndex ->
                        val courseUnits: List<LessonGroup> = courses[courseIndex]
                        AnimatedVisibility(
                            showElements && showUnpinned,
                            enter = enterTransition(4 + courseIndex)
                        ) {
                            if (courseUnits.isNotEmpty()) {
                                GroupedContentContainerView(
                                    title = courseUnits.first().course_name.getLocalized(context),
                                    modifier = paddingModifier
                                ) {
                                    for (course in courseUnits) {
                                        GradeCardView(
                                            courseName = UIHelper.classTypeIds[course.class_type_id]?.name?.getLocalized(context) ?: course.class_type_id,
                                            grade = courseUnits.first().group_number.toString(),
                                            showArrow = true,
                                            showGrade = false,
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
                            showElements && showUnpinned,
                            enter = enterTransition(4)
                        ) {
                            TextAndIconCardView(
                                title = "Brak niepodpiętych przedmiotów",
                                icon = Icons.Rounded.Done,
                                modifier = paddingModifier,
                                backgroundColor = UISingleton.color2,
                            )
                        }
                    }
                }
            } else {
                if (!pinnedCoursesMap.isNullOrEmpty()) {
                    val keys: List<String> = pinnedCoursesMap.keys.toList()
                    items(keys.size) { keyIndex ->
                        val courseUnits: List<LessonGroup> = pinnedCoursesMap[keys[keyIndex]] ?: emptyList()
                        AnimatedVisibility(
                            showElements && showPinned,
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
                            showElements && showPinned,
                            enter = enterTransition(4)
                        ) {
                            TextAndIconCardView(
                                title = "Brak podpiętych przedmiotów",
                                icon = Icons.Rounded.Close,
                                modifier = paddingModifier,
                                backgroundColor = UISingleton.color2,
                            )
                        }
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

private enum class ShownCourses {
    PINNED,
    UNPINNED
}