package com.cdkentertainment.bux.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.bux.R
import com.cdkentertainment.bux.UISingleton
import com.cdkentertainment.bux.models.SharedDataClasses
import com.cdkentertainment.bux.models.StudentsPoints
import com.cdkentertainment.bux.view_models.TestsPageViewModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()
private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    context.model.extraStore[BottomAxisLabelKey][x.toInt()]
}

@Composable
fun TestTaskPopupView(
    grade: StudentsPoints,
    nodeId: Int,
    type: String,
    title: String? = null,
    onDismiss: () -> Unit
) {
    val viewModel: TestsPageViewModel = viewModel<TestsPageViewModel>()
    val grader: SharedDataClasses.Human? = viewModel.taskNodeDetails[nodeId]?.task_node_details?.students_points?.grader
    val lastChanged: String? = viewModel.taskNodeDetails[nodeId]?.task_node_details?.students_points?.last_changed
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val fetchDetails: () -> Unit = {
        coroutineScope.launch {
            viewModel.fetchSpecificNodeDetails(nodeId, type)
        }
    }
    LaunchedEffect(Unit) {
        fetchDetails()
    }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .background(UISingleton.color2, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .border(5.dp, UISingleton.color1, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                if (title != null) {
                    PopupHeaderView(
                        title = title
                    )
                } else {
                    Spacer(Modifier.height(48.dp))
                }
                GroupedContentContainerView(
                    title = stringResource(R.string.grade_information),
                    backgroundColor = UISingleton.color1,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 0.dp)
                ) {
                    GradeCardView(
                        courseName = stringResource(R.string.your_grade),
                        grade = if (grade.points != null) grade.points.toString() else "—",
                        backgroundColor = UISingleton.color2
                    )
                    if (lastChanged != null) {
                        Text(
                            text = "${stringResource(R.string.modification_date)}: $lastChanged",
                            style = MaterialTheme.typography.titleMedium,
                            color = UISingleton.textColor1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 48.dp)
                                .background(UISingleton.color2, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                                .padding(12.dp)
                        )
                    }
                    if (grader != null) {
                        Text(
                            text = "${stringResource(R.string.grader)}: ${grader.first_name} ${grader.last_name}",
                            style = MaterialTheme.typography.titleMedium,
                            color = UISingleton.textColor1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 48.dp)
                                .background(UISingleton.color2, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                                .padding(12.dp)
                        )
                    }
                }
                GroupedContentContainerView(
                    title = stringResource(R.string.grade_distribution),
                    backgroundColor = UISingleton.color1,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                ) {
                    if (viewModel.loadedTaskNodeDetails[nodeId] == true)  {
                        GradeChart(
                            gradeData = viewModel.taskNodeDetails[nodeId]?.students_points?.associate { it.value.toString() to it.number_of_values.toInt() } ?: emptyMap(),
                            modifier = Modifier
                                .fillMaxWidth(),
                            addPercent = false
                        )
                    } else {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            androidx.compose.animation.AnimatedVisibility(viewModel.errorTaskNodeDetails[nodeId] == true) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                                        .background(UISingleton.color2)
                                        .clickable(onClick = {
                                            fetchDetails()
                                        })
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.failed_to_fetch),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = UISingleton.textColor1,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        imageVector = Icons.Rounded.Refresh,
                                        contentDescription = null,
                                        tint = UISingleton.textColor4,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(UISingleton.color3, CircleShape)
                                            .padding(8.dp)
                                    )
                                }
                            }
                            androidx.compose.animation.AnimatedVisibility(viewModel.loadingTaskNodeDetails[nodeId] == true) {
                                CircularProgressIndicator(
                                    color = UISingleton.textColor2
                                )
                            }
                        }
                    }
                }
            }
            DismissPopupButtonView(onDismiss, Modifier.align(Alignment.TopEnd))
        }
    }
}