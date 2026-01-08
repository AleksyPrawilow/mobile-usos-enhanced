package com.cdkentertainment.mobilny_usos_enhanced.views

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerRate
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun LecturerAddRateView(
    rate: LecturerRate = LecturerRate(),
    onAddRate: (LecturerRate) -> Unit
) {
    val context: Context = LocalContext.current
    var rate_1: Int by rememberSaveable { mutableStateOf(rate.rate_1.toInt()) }
    var rate_2: Int by rememberSaveable { mutableStateOf(rate.rate_2.toInt()) }
    var rate_3: Int by rememberSaveable { mutableStateOf(rate.rate_3.toInt()) }
    var rate_4: Int by rememberSaveable { mutableStateOf(rate.rate_4.toInt()) }
    var rate_5: Int by rememberSaveable { mutableStateOf(rate.rate_5.toInt()) }

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        var ratingStage: RatingStage by rememberSaveable { mutableStateOf(RatingStage.First) }
        var showBackButton: Boolean by rememberSaveable { mutableStateOf(false) }
        var lastClickedBack: Boolean by remember { mutableStateOf(false) }
        val backButtonWeight: Float by animateFloatAsState(
            if (showBackButton) 1f else 0.01f
        )
        val density: Density = LocalDensity.current
        val context: Context = LocalContext.current
        val availableWidthPx = with(density) { maxWidth.toPx() }
        val categoriesPrompts: List<String> = listOf(
            stringResource(R.string.clarity_rate),
            stringResource(R.string.organization_rate),
            stringResource(R.string.engagement_rate),
            stringResource(R.string.communication_rate),
            stringResource(R.string.fairness_rate)
        )
        val headerStyle: TextStyle = MaterialTheme.typography.titleMedium
        val textMeasurer = rememberTextMeasurer()
        val maxHeaderHeight = remember(categoriesPrompts, headerStyle) {
            categoriesPrompts.maxOf {
                textMeasurer.measure(
                    text = AnnotatedString(it),
                    style = headerStyle,
                    constraints = Constraints(
                        maxWidth = availableWidthPx.toInt()
                    )
                ).size.height
            }
        }
        val toast: Toast = Toast.makeText(context, SharedDataClasses.LangDict("Nie wybrano oceny!", "No rate chosen!").getLocalized(context), Toast.LENGTH_SHORT)
        val noRateToast: () -> Unit = {
            toast.show()
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.share_opinion),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = UISingleton.textColor1,
                modifier = Modifier.fillMaxWidth()
            )
            AnimatedContent(
                ratingStage,
                transitionSpec = {
                    fadeIn() + slideIntoContainer(if(!lastClickedBack) AnimatedContentTransitionScope.SlideDirection.Start else AnimatedContentTransitionScope.SlideDirection.End) togetherWith
                            fadeOut() + slideOutOfContainer(towards = if (!lastClickedBack) AnimatedContentTransitionScope.SlideDirection.Start else AnimatedContentTransitionScope.SlideDirection.End)
                }
            ) { stage ->
                when(stage) {
                    RatingStage.First -> {
                        RatingStageView(
                            headline = categoriesPrompts[0],
                            rating = rate_1,
                            headerMaxHeight = maxHeaderHeight
                        ) {
                            rate_1 = it
                        }
                    }
                    RatingStage.Second -> {
                        RatingStageView(
                            headline = categoriesPrompts[1],
                            rating = rate_2,
                            headerMaxHeight = maxHeaderHeight
                        ) {
                            rate_2 = it
                        }
                    }
                    RatingStage.Third -> {
                        RatingStageView(
                            headline = categoriesPrompts[2],
                            rating = rate_3,
                            headerMaxHeight = maxHeaderHeight
                        ) {
                            rate_3 = it
                        }
                    }
                    RatingStage.Fourth -> {
                        RatingStageView(
                            headline = categoriesPrompts[3],
                            rating = rate_4,
                            headerMaxHeight = maxHeaderHeight
                        ) {
                            rate_4 = it
                        }
                    }
                    RatingStage.Fifth -> {
                        RatingStageView(
                            headline = categoriesPrompts[4],
                            rating = rate_5,
                            headerMaxHeight = maxHeaderHeight
                        ) {
                            rate_5 = it
                        }
                    }
                    RatingStage.Submit -> {
                        LecturerAddRateSummaryView()
                    }
                }
            }
            AnimatedVisibility(
                visible = ratingStage != RatingStage.Submit
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(5) { index ->
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .graphicsLayer(
                                    scaleX = if (ratingStage.ordinal == index) 1.15f else 0.85f,
                                    scaleY = if (ratingStage.ordinal == index) 1.15f else 0.85f
                                )
                                .background(UISingleton.color2, CircleShape)
                                .padding(4.dp)
                                .background(if (index < ratingStage.ordinal + 1) UISingleton.color1 else UISingleton.color2, CircleShape)
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = ratingStage != RatingStage.Submit
            ) {
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
                                .background(UISingleton.color2)
                                .clickable(onClick = {
                                    lastClickedBack = true
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
                                tint = UISingleton.textColor1
                            )
                        }
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                            .background(UISingleton.color2)
                            .clickable(onClick = {
                                lastClickedBack = false
                                when(ratingStage) {
                                    RatingStage.First -> {
                                        if (rate_1 == 0) {
                                            noRateToast()
                                            return@clickable
                                        }
                                        ratingStage = RatingStage.Second
                                        showBackButton = true
                                    }
                                    RatingStage.Second -> {
                                        if (rate_2 == 0) {
                                            noRateToast()
                                            return@clickable
                                        }
                                        ratingStage = RatingStage.Third
                                    }
                                    RatingStage.Third -> {
                                        if (rate_3 == 0) {
                                            noRateToast()
                                            return@clickable
                                        }
                                        ratingStage = RatingStage.Fourth
                                    }
                                    RatingStage.Fourth -> {
                                        if (rate_4 == 0) {
                                            noRateToast()
                                            return@clickable
                                        }
                                        ratingStage = RatingStage.Fifth
                                    }
                                    RatingStage.Fifth -> {
                                        if (rate_5 == 0) {
                                            noRateToast()
                                            return@clickable
                                        }
                                        onAddRate(
                                            LecturerRate(
                                                rate_1.toFloat(),
                                                rate_2.toFloat(),
                                                rate_3.toFloat(),
                                                rate_4.toFloat(),
                                                rate_5.toFloat()
                                            )
                                        )
                                        ratingStage = RatingStage.Submit
                                    }
                                    RatingStage.Submit -> return@clickable
                                }
                            })
                            .padding(vertical = 8.dp)
                    ) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = ratingStage == RatingStage.Fifth
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = "Submit",
                                tint = UISingleton.textColor1
                            )
                        }
                        androidx.compose.animation.AnimatedVisibility(
                            visible = ratingStage != RatingStage.Fifth
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = "back",
                                tint = UISingleton.textColor1
                            )
                        }
                    }
                }
            }
        }
    }
}