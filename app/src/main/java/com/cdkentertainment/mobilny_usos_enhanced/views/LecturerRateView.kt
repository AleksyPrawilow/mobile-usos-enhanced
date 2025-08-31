package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerRate
import kotlin.math.min

@Composable
fun LecturerRateView(
    title: String = "Ogólna ocena",
    numberOfReviews: Int = 0,
    showNumberOfReviews: Boolean = true,
    rate: LecturerRate = LecturerRate()
) {
    val rateAverage: Float = (rate.rate_1 + rate.rate_2 + rate.rate_3 + rate.rate_4 + rate.rate_5) / 5f
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = UISingleton.color4.primaryColor
        )
        Text(
            text = "%.1f".format(rateAverage),
            fontSize = 72.sp,
            fontWeight = FontWeight.ExtraBold,
            color = UISingleton.color4.primaryColor
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            repeat(5) { index ->
                val fractionFilled: Float = min(rateAverage - index, 1f)
                val starBrush = Brush.horizontalGradient(
                    0f to UISingleton.color3.primaryColor,
                    fractionFilled to UISingleton.color3.primaryColor,
                    fractionFilled to UISingleton.color2.primaryColor,
                    1f to UISingleton.color2.primaryColor
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
                color = UISingleton.color4.primaryColor
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

        if (!showNumberOfReviews && numberOfReviews == 0) {
            var ratingStage: RatingStage by rememberSaveable { mutableStateOf(RatingStage.First) }
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            Text(
                text = "Podziel się swoją opinią!",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = UISingleton.color4.primaryColor,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            HorizontalDivider(
                thickness = 5.dp,
                color = UISingleton.color3.primaryColor
            )
            AnimatedContent(
                ratingStage,
                transitionSpec = { fadeIn() + expandHorizontally(expandFrom = Alignment.End) togetherWith fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.Start) }
            ) { stage ->
                when(stage) {
                    RatingStage.First -> {
                        Text(
                            text = "Na ile jasno prowadzący tłumaczy materiał?",
                            style = MaterialTheme.typography.titleMedium,
                            color = UISingleton.color4.primaryColor,
                            modifier = Modifier.clickable(onClick = {
                                ratingStage = RatingStage.Second
                            })
                        )
                    }
                    RatingStage.Second -> {
                        Text(
                            text = "Na ile zajęcia są dobrze zorganizowane (plan, materiały, czas)?",
                            style = MaterialTheme.typography.titleMedium,
                            color = UISingleton.color4.primaryColor,
                            modifier = Modifier.clickable(onClick = {
                                ratingStage = RatingStage.Third
                            })
                        )
                    }
                    RatingStage.Third -> {
                        Text(
                            text = "Na ile prowadzący angażuje studentów w zajęcia?",
                            style = MaterialTheme.typography.titleMedium,
                            color = UISingleton.color4.primaryColor,
                            modifier = Modifier.clickable(onClick = {
                                ratingStage = RatingStage.Fourth
                            })
                        )
                    }
                    RatingStage.Fourth -> {
                        Text(
                            text = "Na ile komunikacja prowadzącego jest klarowna i życzliwa?",
                            style = MaterialTheme.typography.titleMedium,
                            color = UISingleton.color4.primaryColor,
                            modifier = Modifier.clickable(onClick = {
                                ratingStage = RatingStage.Fifth
                            })
                        )
                    }
                    RatingStage.Fifth -> {
                        Text(
                            text = "Na ile oceny są obiektywne, a kryteria jasne?",
                            style = MaterialTheme.typography.titleMedium,
                            color = UISingleton.color4.primaryColor,
                            modifier = Modifier.clickable(onClick = {
                                ratingStage = RatingStage.Submit
                            })
                        )
                    }
                    RatingStage.Submit -> {
                        Text(
                            text = "Dziękujemy!",
                            style = MaterialTheme.typography.titleMedium,
                            color = UISingleton.color4.primaryColor
                        )
                    }
                }
            }
        } else {
            for ((category, rating) in categories) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleSmall,
                        color = UISingleton.color4.primaryColor,
                        modifier = Modifier.weight(0.5f)
                    )
                    LinearProgressIndicator(
                        progress = { rating / 5f },
                        color = UISingleton.color3.primaryColor,
                        trackColor = UISingleton.color2.primaryColor,
                        drawStopIndicator = {},
                        modifier = Modifier
                            .weight(0.5f)
                    )
                }
            }
        }
    }
}

enum class RatingStage {
    First,
    Second,
    Third,
    Fourth,
    Fifth,
    Submit
}