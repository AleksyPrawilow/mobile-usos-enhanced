package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerRate
import kotlin.math.min

@Composable
fun LecturerRateView(
    lecturerId: String,
    title: String = "Ogólna ocena",
    numberOfReviews: Int = 0,
    showNumberOfReviews: Boolean = true,
    rate: LecturerRate = LecturerRate(),
    onAddRate: (LecturerRate) -> Unit = { rate ->
        println(rate)
    },
    onEditRate: () -> Unit = {},
) {
    val rateAverage: Float = (rate.rate_1 + rate.rate_2 + rate.rate_3 + rate.rate_4 + rate.rate_5) / 5f
    var showDeleteDialog: Boolean by rememberSaveable { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!showNumberOfReviews && numberOfReviews == 0) {
            LecturerAddRateView(rate, onAddRate = onAddRate)
        } else {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = UISingleton.textColor1
            )
            Text(
                text = "%.1f".format(rateAverage),
                fontSize = 72.sp,
                fontWeight = FontWeight.ExtraBold,
                color = UISingleton.textColor1
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
            ) {
                repeat(5) { index ->
                    val fractionFilled: Float = min(rateAverage - index, 1f)
                    val starBrush = Brush.horizontalGradient(
                        0f to UISingleton.color3,
                        fractionFilled to UISingleton.color3,
                        fractionFilled to UISingleton.color2,
                        1f to UISingleton.color2
                    )
                    val starModifier: Modifier = Modifier
                        .size(32.dp)
                        .graphicsLayer {
                            compositingStrategy = CompositingStrategy.Offscreen
                        }
                        .drawWithContent {
                            drawContent()
                            drawRect(starBrush, size = this.size, blendMode = BlendMode.SrcAtop)
                        }
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Star",
                        tint = Color.Unspecified,
                        modifier = starModifier
                    )
                }
            }
            if (showNumberOfReviews) {
                val reviewTextSuffix: String = when {
                    numberOfReviews == 1 -> "oceny"
                    else -> "ocen"
                }
                Text(
                    text = "Na podstawie $numberOfReviews $reviewTextSuffix",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Light,
                    color = UISingleton.textColor1
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            val categories: Map<String, Float> = mapOf(
                "Jasność" to rate.rate_1,
                "Organizacja" to rate.rate_2,
                "Zaangażowanie" to rate.rate_3,
                "Komunikacja" to rate.rate_4,
                "Obiektywność" to rate.rate_5
            )
            for ((category, rating) in categories) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleSmall,
                        color = UISingleton.textColor1,
                        modifier = Modifier.weight(0.5f)
                    )
                    LinearProgressIndicator(
                        progress = { rating / 5f },
                        color = UISingleton.color3,
                        trackColor = UISingleton.color2,
                        drawStopIndicator = {},
                        modifier = Modifier
                            .weight(0.5f)
                    )
                }
            }
            if (!showNumberOfReviews) {
                if (showDeleteDialog) {
                    UserRateDeleteDialogView(lecturerId) {
                        showDeleteDialog = false
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                        .background(UISingleton.color2)
                        .clickable(onClick = onEditRate)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Zmień swoją ocenę",
                        style = MaterialTheme.typography.titleMedium,
                        color = UISingleton.textColor1,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = "Edit",
                        tint = UISingleton.textColor4,
                        modifier = Modifier
                            .size(48.dp)
                            .background(UISingleton.color3, CircleShape)
                            .padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                        .background(UISingleton.color2)
                        .clickable(onClick = { showDeleteDialog = true })
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Usuń swoją ocenę",
                        style = MaterialTheme.typography.titleMedium,
                        color = UISingleton.textColor1,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete",
                        tint = UISingleton.textColor4,
                        modifier = Modifier
                            .size(48.dp)
                            .background(UISingleton.color3, CircleShape)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}