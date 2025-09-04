package com.cdkentertainment.mobilny_usos_enhanced.views

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.TermGrade
import com.cdkentertainment.mobilny_usos_enhanced.view_models.GradesPageViewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.core.cartesian.CartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.Insets
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()
private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    context.model.extraStore[BottomAxisLabelKey][x.toInt()]
}

@Composable
fun GradePopupView(
    grade: TermGrade, onDismiss: () -> Unit
) {
    val viewModel: GradesPageViewModel = viewModel<GradesPageViewModel>()
    var fetchingSuccess: Boolean by rememberSaveable { mutableStateOf(true) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val fetchDetails: () -> Unit = {
        if (viewModel.gradesDistribution.getOrDefault(grade.exam_id, null) == null) {
            coroutineScope.launch {
                fetchingSuccess = viewModel.fetchGradesDistribution(grade.exam_id)
            }
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
                .background(UISingleton.color2.primaryColor, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .border(5.dp, UISingleton.color1.primaryColor, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(12.dp)
            ) {
                Spacer(Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(UISingleton.color1.primaryColor, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                        .padding(12.dp)
                ){
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Informacja o ocenie",
                            style = MaterialTheme.typography.titleLarge,
                            color = UISingleton.color4.primaryColor
                        )
                        HorizontalDivider(
                            thickness = 5.dp,
                            color = UISingleton.color3.primaryColor,
                            modifier = Modifier.clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                        )
                        GradeCardView(
                            courseName = "Twoja ocena",
                            grade = grade.value_symbol,
                            backgroundColor = UISingleton.color2.primaryColor
                        )
                        if (grade.date_modified != null) {
                            Text(
                                text = "Data wprowadzenia: ${grade.date_modified}",
                                style = MaterialTheme.typography.titleMedium,
                                color = UISingleton.color4.primaryColor,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 48.dp)
                                    .background(UISingleton.color2.primaryColor, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                                    .padding(12.dp)
                            )
                        }
                        if (grade.modification_author != null) {
                            Text(
                                text = "Wystawiający: ${grade.modification_author.first_name} ${grade.modification_author.last_name}",
                                style = MaterialTheme.typography.titleMedium,
                                color = UISingleton.color4.primaryColor,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 48.dp)
                                    .background(UISingleton.color2.primaryColor, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                                    .padding(12.dp)
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(UISingleton.color1.primaryColor, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                        .padding(12.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Dystrybucja ocen",
                            style = MaterialTheme.typography.titleLarge,
                            color = UISingleton.color4.primaryColor
                        )
                        HorizontalDivider(
                            thickness = 5.dp,
                            color = UISingleton.color3.primaryColor,
                            modifier = Modifier.clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                        )
                        if (viewModel.gradesDistribution.getOrDefault(grade.exam_id, null) != null)  {
                            GradeChart(
                                gradeData = viewModel.gradesDistribution[grade.exam_id]!!,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        } else {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                androidx.compose.animation.AnimatedVisibility(!fetchingSuccess) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                                            .background(UISingleton.color2.primaryColor)
                                            .clickable(onClick = {
                                                fetchDetails()
                                            })
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = "Nie udało się pobrać danych",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = UISingleton.color4.primaryColor,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Icon(
                                            imageVector = Icons.Rounded.Refresh,
                                            contentDescription = null,
                                            tint = UISingleton.color1.primaryColor,
                                            modifier = Modifier
                                                .size(48.dp)
                                                .background(UISingleton.color3.primaryColor, CircleShape)
                                                .padding(8.dp)
                                        )
                                    }
                                }
                                androidx.compose.animation.AnimatedVisibility(fetchingSuccess){
                                    CircularProgressIndicator(
                                        color = UISingleton.color3.primaryColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
            DismissPopupButtonView(onDismiss, Modifier.align(Alignment.TopEnd))
        }
    }
}

@Composable
fun GradeChart(gradeData: Map<String, Int>, modifier: Modifier = Modifier) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries { series(gradeData.values) }
            extras { it[BottomAxisLabelKey] = gradeData.keys.toList() }
        }
    }

    val textComponent: TextComponent = rememberTextComponent(
        color = UISingleton.color4.primaryColor,
        textSize = 12.sp,
        padding = Insets(3f)
    )
    val chart: CartesianChart = rememberCartesianChart(
        rememberColumnCartesianLayer(
            columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                rememberLineComponent(
                    fill = Fill(UISingleton.color3.primaryColor.toArgb()),
                    thickness = 12.dp,
                    shape = CorneredShape.rounded(topLeftPercent = UISingleton.uiElementsCornerRadius, topRightPercent = UISingleton.uiElementsCornerRadius),
                )
            ),
            dataLabel = textComponent,
            dataLabelValueFormatter = { _, value, _ ->
                "${value.toInt()}%"
            },
            rangeProvider = remember { CartesianLayerRangeProvider.fixed(minY = 0.0, maxY = gradeData.values.max() * 1.25) }
        ),
        startAxis = VerticalAxis.rememberStart(
            itemPlacer = remember { VerticalAxis.ItemPlacer.count({ 6 }) },
            line = rememberLineComponent(
                fill = Fill(UISingleton.color2.primaryColor.toArgb()),
                thickness = 5.dp,
                shape = CorneredShape.rounded(topRightPercent = 50)
            ),
            label = textComponent,
            tick = rememberLineComponent(
                fill = Fill(UISingleton.color2.primaryColor.toArgb()),
                thickness = 5.dp,
                shape = CorneredShape.rounded(bottomLeftPercent = 50, topLeftPercent = 50)
            ),
            valueFormatter = { _, value, _ ->
                "${value.toInt()}%"
            },
            guideline = rememberLineComponent(
                fill = Fill(UISingleton.color2.primaryColor.toArgb()),
                thickness = 1.dp
            )
        ),
        bottomAxis = HorizontalAxis.rememberBottom(
            line = rememberLineComponent(
                fill = Fill(UISingleton.color2.primaryColor.toArgb()),
                thickness = 5.dp,
                shape = CorneredShape.rounded(bottomRightPercent = 50, topRightPercent = 50)
            ),
            label = textComponent,
            guideline = null,
            tick = rememberLineComponent(
                fill = Fill(UISingleton.color2.primaryColor.toArgb()),
                thickness = 5.dp,
                shape = CorneredShape.rounded(bottomLeftPercent = 50, bottomRightPercent = 50)
            ),
            itemPlacer = remember { HorizontalAxis.ItemPlacer.aligned() },
            valueFormatter = BottomAxisValueFormatter,
        ),
        layerPadding = { cartesianLayerPadding(scalableStart = 8.dp, scalableEnd = 8.dp) }
    )

    CartesianChartHost(
        chart = chart,
        modelProducer = modelProducer,
        modifier = modifier
    )
}