package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
        if (!showNumberOfReviews && numberOfReviews == 0) {
            var ratingStage: RatingStage by rememberSaveable { mutableStateOf(RatingStage.First) }
            var showBackButton: Boolean by rememberSaveable { mutableStateOf(false) }
            val backButtonWeight: Float by animateFloatAsState(
                if (showBackButton) 1f else 0.01f
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Podziel się swoją opinią!",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    color = UISingleton.color4.primaryColor,
                    modifier = Modifier.fillMaxWidth()
                )
                HorizontalDivider(
                    thickness = 5.dp,
                    color = UISingleton.color3.primaryColor,
                    modifier = Modifier.clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                )
                AnimatedContent(
                    ratingStage,
                    transitionSpec = { fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start) togetherWith fadeOut() + slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) }
                ) { stage ->
                    when(stage) {
                        RatingStage.First -> {
                            RatingStageView(
                                "Jak oceniasz jasność i zrozumiałość tłumaczenia materiału?",
                                0
                            )
                        }
                        RatingStage.Second -> {
                            RatingStageView(
                                "Jak oceniasz przygotowanie i organizację zajęć?",
                                1
                            )
                        }
                        RatingStage.Third -> {
                            RatingStageView(
                                "Jak oceniasz zaangażowanie prowadzącego w prowadzeniu zajęć?",
                                2
                            )
                        }
                        RatingStage.Fourth -> {
                            RatingStageView(
                                "Jak oceniasz sposób komunikacji i kontakt ze studentami?",
                                3
                            )
                        }
                        RatingStage.Fifth -> {
                            RatingStageView(
                                "Jak oceniasz sprawiedliwość i obiektywność oceniania?",
                                4
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AnimatedVisibility(
                        visible = showBackButton,
                        enter = fadeIn(snap()),
                        modifier = Modifier.weight(backButtonWeight).padding(horizontal = 4.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                                .background(UISingleton.color2.primaryColor)
                                .clickable(onClick = {
                                    when(ratingStage) {
                                        RatingStage.First -> ratingStage = RatingStage.First
                                        RatingStage.Second -> { ratingStage = RatingStage.First; showBackButton = false }
                                        RatingStage.Third -> ratingStage = RatingStage.Second
                                        RatingStage.Fourth -> ratingStage = RatingStage.Third
                                        RatingStage.Fifth -> ratingStage = RatingStage.Fourth
                                        RatingStage.Submit -> ratingStage = RatingStage.Fifth
                                    }
                                })
                                .padding(vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "back",
                                tint = UISingleton.color4.primaryColor
                            )
                        }
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                            .background(UISingleton.color2.primaryColor)
                            .clickable(onClick = {
                                when(ratingStage) {
                                    RatingStage.First -> { ratingStage = RatingStage.Second; showBackButton = true }
                                    RatingStage.Second -> ratingStage = RatingStage.Third
                                    RatingStage.Third -> ratingStage = RatingStage.Fourth
                                    RatingStage.Fourth -> ratingStage = RatingStage.Fifth
                                    RatingStage.Fifth -> ratingStage = RatingStage.Submit
                                    RatingStage.Submit -> ratingStage = RatingStage.Submit
                                }
                            })
                            .padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = "back",
                            tint = UISingleton.color4.primaryColor
                        )
                    }
                }
            }
        } else {
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

@Composable
private fun RatingStageView(
    headline: String,
    sectionId: Int = 0
) {
    val rateNames: List<String> = listOf(
        "Nie wybrano",
        "Tragedia",
        "Może być",
        "Spoko",
        "Super",
        "Ekstra"
    )
    var selectedRate: Int by rememberSaveable { mutableStateOf(0) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = headline,
            style = MaterialTheme.typography.titleMedium,
            color = UISingleton.color4.primaryColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(5) { index ->
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (index + 1 <= selectedRate) UISingleton.color3.primaryColor else UISingleton.color2.primaryColor,
                        containerColor = Color.Transparent
                    ),
                    onClick = {
                        selectedRate = index + 1
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "star",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        Text(
            text = rateNames[selectedRate],
            style = MaterialTheme.typography.titleLarge,
            color = UISingleton.color4.primaryColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(5) { index ->
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .graphicsLayer(
                            scaleX = if (sectionId == index) 1.15f else 0.85f,
                            scaleY = if (sectionId == index) 1.15f else 0.85f
                        )
                        .background(UISingleton.color2.primaryColor, CircleShape)
                        .padding(4.dp)
                        .background(if (index < sectionId + 1) UISingleton.color1.primaryColor else UISingleton.color2.primaryColor, CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
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