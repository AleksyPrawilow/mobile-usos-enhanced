package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.common.data.ExtraStore

private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()
private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    context.model.extraStore[BottomAxisLabelKey][x.toInt()]
}

@Composable
fun GradePopupView() {
    val sample: Map<String, Int> = mapOf(
        "2" to 10,
        "3" to 15,
        "3.5" to 10,
        "4" to 25,
        "4.5" to 30,
        "5" to 10
    )
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            // Learn more: https://patrykandpatrick.com/eji9zq.
            columnSeries { series(sample.values) }
            extras { it[BottomAxisLabelKey] = sample.keys.toList() }
        }
    }
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberColumnCartesianLayer(),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(
                    itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                    valueFormatter = BottomAxisValueFormatter,
                ),
                layerPadding = { cartesianLayerPadding(scalableStart = 8.dp, scalableEnd = 8.dp) }
            ),
        modelProducer = modelProducer,
    )
}

@Preview(showBackground = true)
@Composable
fun GradePopupPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1.primaryColor)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        GradePopupView()
    }
}