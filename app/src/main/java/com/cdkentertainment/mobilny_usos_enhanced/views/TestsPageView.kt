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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import com.cdkentertainment.mobilny_usos_enhanced.models.Test
import com.cdkentertainment.mobilny_usos_enhanced.view_models.TestsPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestsPageView() {
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    val density: Density = LocalDensity.current
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val testsPageViewModel: TestsPageViewModel = viewModel<TestsPageViewModel>()
    val listState: LazyListState = rememberLazyListState()
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }

    val onStart: () -> Unit = {
        coroutineScope.launch {
            showElements = false
            testsPageViewModel.fetchTests()
            delay(150)
            showElements = true
        }
    }

    LaunchedEffect(Unit) {
        onStart()
    }

    val paddingModifier: Modifier = Modifier.padding(
        horizontal = UISingleton.horizontalPadding,
        vertical = 8.dp
    )

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        item {
            PageHeaderView(
                text = stringResource(R.string.tests_page),
                icon = ImageVector.vectorResource(R.drawable.rounded_assignment_24)
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            AnimatedVisibility(
                visible = testsPageViewModel.loading,
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
                visible = testsPageViewModel.error && showElements,
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

        var currentIndex: Int = 2
        if (testsPageViewModel.tests != null) {
            for (semester in testsPageViewModel.tests!!.tests.keys.reversed()) {
                currentIndex++
                val semesterTests: Map<String, Test>? = testsPageViewModel.tests?.tests[semester]

                stickyHeader {
                    AnimatedVisibility(
                        visible = showElements && testsPageViewModel.loaded,
                        enter = enterTransition(1)
                    ) {
                        SemesterCardView(
                            semester,
                            modifier = paddingModifier
                        )
                    }
                }

                if (semesterTests != null) {
                    val keys: List<String> = semesterTests.keys.toList()

                    for (index in 0 until semesterTests.keys.size) {
                        currentIndex++
                        val captureIndex: Int = currentIndex
                        val test: Test? = semesterTests[keys[index]]

                        if (test != null) {
                            item {
                                AnimatedVisibility(
                                    visible = showElements && testsPageViewModel.loaded,
                                    enter = enterTransition(2 + index)
                                ) {
                                    val data: Test? = semesterTests[keys[index]]
                                    if (data != null) {
                                        TestCardView(
                                            data,
                                            modifier = paddingModifier
                                        ) {
                                            coroutineScope.launch {
                                                listState.animateScrollToItem(
                                                    index = captureIndex + 1,
                                                    scrollOffset = with(density) { -96.dp.toPx() }.toInt()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
                currentIndex++
            }
        }

        item { Spacer(modifier = Modifier.height(64.dp)) }
    }
}