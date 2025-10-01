package com.cdkentertainment.mobilny_usos_enhanced.views

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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.Lecturer
import com.cdkentertainment.mobilny_usos_enhanced.PeopleSingleton
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper.scaleEnterTransition
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LecturerRatesPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.max

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LecturerRatesPageView() {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val listState     : LazyListState  = rememberLazyListState()
    val textMeasurer  : TextMeasurer   = rememberTextMeasurer()
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    val lecturerRatesPageViewModel: LecturerRatesPageViewModel = viewModel<LecturerRatesPageViewModel>()
    var shownPage      : ShownPage by rememberSaveable { mutableStateOf(ShownPage.TEACHERS_INDEX) }
    var showElements   : Boolean   by rememberSaveable { mutableStateOf(false) }
    var showDetails    : Boolean   by rememberSaveable { mutableStateOf(false) }
    var showIndex      : Boolean   by rememberSaveable { mutableStateOf(true)  }
    var showSearch     : Boolean   by rememberSaveable { mutableStateOf(false) }
    var showLecturers  : Boolean   by rememberSaveable { mutableStateOf(false) }
    var selectedFaculty: String    by rememberSaveable { mutableStateOf("0600000000") }
    var selectedPage   : Int       by rememberSaveable { mutableStateOf(0) }
    val pageSize: Int = 20
    val lecturersIndex: Map<String, List<SharedDataClasses.Human>>? = lecturerRatesPageViewModel.lecturersIndex

    val density: Density = LocalDensity.current
    val insets = WindowInsets.systemBars
    val topInset = insets.getTop(density)
    val bottomInset = insets.getBottom(density)
    val topPadding = with(density) { topInset.toDp() }
    val bottomPadding = with(density) { bottomInset.toDp() }
    val paddingModifier: Modifier = Modifier.padding(horizontal = UISingleton.horizontalPadding, vertical = 8.dp)

    val cardLabels: List<Pair<String, ImageVector>> = listOf(
        Pair(stringResource(R.string.teachers_index), ImageVector.vectorResource(R.drawable.rounded_school_24)),
        Pair(stringResource(R.string.teachers_search), ImageVector.vectorResource(R.drawable.rounded_search_insights_24)),
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

    val onPageChange: (Int) -> Unit = { newPage ->
        val indexTotal: Int? = lecturerRatesPageViewModel.lecturersIndexTotal[selectedFaculty]
        if (indexTotal != null && newPage <= ceil((indexTotal / pageSize).toDouble())) {
            showLecturers = false
            selectedPage = newPage
            lecturerRatesPageViewModel.loadStaffIndex(selectedFaculty, newPage, pageSize) {
                coroutineScope.launch {
                    delay(1)
                    showLecturers = true
                }
            }
            coroutineScope.launch {
                listState.animateScrollToItem(0)
            }
        }
    }
    val onFacultyChange: (String) -> Unit = { newFaculty ->
        showElements = false
        showLecturers = false
        selectedFaculty = newFaculty
        selectedPage = 0
        coroutineScope.launch {
            lecturerRatesPageViewModel.suspendLoadStaffIndex(selectedFaculty, selectedPage, pageSize)
            showElements = true
            delay(500)
            showLecturers = true
        }
    }

    LaunchedEffect(Unit) {
        lecturerRatesPageViewModel.suspendLoadStaffIndex(selectedFaculty, 0, pageSize)
        showElements = true
        delay(500)
        showLecturers = true
    }

    LazyColumn(
        state = listState,
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
                text = stringResource(R.string.lecturers_page),
                icon = ImageVector.vectorResource(R.drawable.rounded_alarm_24)
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            AnimatedVisibility(lecturerRatesPageViewModel.lecturersIndex["$selectedFaculty/0"] == null, modifier = paddingModifier) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(color = UISingleton.textColor2, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
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
                        enabled = shownPage != ShownPage.TEACHERS_INDEX
                    ) {
                        coroutineScope.launch {
                            shownPage = ShownPage.TEACHERS_INDEX
                            showSearch = false
                            delay(50)
                            showIndex = true
                        }
                    }
                }
                AnimatedVisibility(showElements, enter = scaleEnterTransition(2)) {
                    LatestSomethingView(
                        icon = cardLabels[1].second,
                        title = cardLabels[1].first,
                        maxWidth = maxCardWidth,
                        maxLines = 2,
                        disabledTextColor = UISingleton.textColor4,
                        disabledBackgroundColor = UISingleton.color3,
                        enabled = shownPage != ShownPage.TEACHERS_SEARCH
                    ) {
                        coroutineScope.launch {
                            shownPage = ShownPage.TEACHERS_SEARCH
                            showIndex = false
                            delay(50)
                            showSearch = true
                        }
                    }
                }
            }
        }
        if (shownPage == ShownPage.TEACHERS_INDEX) {
            if (lecturersIndex != null) {
                stickyHeader {
                    AnimatedVisibility(
                        visible = showIndex && showElements,
                        enter = enterTransition(3)
                    ) {
                        PageSelectorView(
                            pageNum = "${selectedPage + 1}",
                            faculty = mapOf(
                                "0600000000" to "Wydział Matematyki i informatyki",
                                "0400000000" to "Wydział Fizyki i Astronomii"
                            ),
                            selectedFaculty = selectedFaculty,
                            showBack = selectedPage > 0,
                            showNext = selectedPage < ceil((lecturerRatesPageViewModel.lecturersIndexTotal.getOrDefault(selectedFaculty, 0) / pageSize).toDouble()),
                            onBack = {
                                onPageChange(max(0, selectedPage - 1))
                            },
                            onNext = {
                                onPageChange(selectedPage + 1)
                            },
                            onFacultySelect = { facultyId ->
                                onFacultyChange(facultyId)
                            },
                            modifier = paddingModifier
                        )
                    }
                }
                item {
                    AnimatedVisibility(
                        visible = showElements && lecturerRatesPageViewModel.lecturersIndexLoading["$selectedFaculty/$selectedPage"] == true
                    ) {
                        Box(modifier = paddingModifier.fillMaxWidth()) {
                            CircularProgressIndicator(color = UISingleton.textColor2, modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
                if (!lecturersIndex["$selectedFaculty/$selectedPage"].isNullOrEmpty()) {
                    items(lecturersIndex["$selectedFaculty/$selectedPage"]!!.size) { lecturerIndex ->
                        val lecturer: SharedDataClasses.Human? = lecturersIndex["$selectedFaculty/$selectedPage"]?.get(lecturerIndex)
                        val extendedLecturer: Lecturer? = if (lecturer != null) PeopleSingleton.lecturers[lecturer.id] else null
                        if (lecturer != null) {
                            AnimatedVisibility(
                                showElements && showIndex && showLecturers,
                                enter = enterTransition(4 + lecturerIndex)
                            ) {
                                if (extendedLecturer != null) {
                                    GroupLecturerCardView(
                                        lecturer = extendedLecturer, // I suppose it is safe to use !! here?
                                        modifier = paddingModifier,
                                        prefix = "${lecturerIndex + 1 + pageSize * selectedPage}. ",
                                        elevation = 3.dp
                                    )
                                }
                            }
                        }
                    }
                } else {
                    item {
                        AnimatedVisibility(
                            showElements && showIndex && lecturerRatesPageViewModel.lecturersIndexLoaded["$selectedFaculty$selectedPage"] == true,
                            enter = enterTransition(4)
                        ) {
                            TextAndIconCardView(
                                title = "Brak prowadzących na wydziale",
                                icon = Icons.Rounded.Done,
                                modifier = paddingModifier,
                                backgroundColor = UISingleton.color2,
                            )
                        }
                    }
                }
            }
        } else {
            if (true) {
                item {
                    Text(
                        "Wyszukiwarka bla bla bla"
                    )
                }
            } else {
                item {
                    AnimatedVisibility(
                        showElements && showSearch,
                        enter = enterTransition(4)
                    ) {
                        TextAndIconCardView(
                            title = "Nie znaleziono prowadzących.",
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
        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

private enum class ShownPage {
    TEACHERS_INDEX,
    TEACHERS_SEARCH
}