package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Payment
import com.cdkentertainment.mobilny_usos_enhanced.view_models.PaymentsPageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PaymentsPageView(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val appearDuration   : Int = 500
    val delayBetweenShows: Int = 150
    val safeEasing = Easing { fraction ->
        EaseOutBack.transform(fraction.coerceIn(0f, 0.9999f))
    }
    val tweenSlideSpec : (Int) -> FiniteAnimationSpec<IntOffset> = { delayIndex: Int -> tween(appearDuration, delayBetweenShows * delayIndex, safeEasing) }
    val tweenFadeSpec  : (Int) -> FiniteAnimationSpec<Float>     = { delayIndex: Int -> tween(appearDuration, delayBetweenShows * delayIndex, safeEasing) }
    val enterTransition: (Int) -> EnterTransition = { delayIndex: Int -> slideInHorizontally(tweenSlideSpec(delayIndex)) + fadeIn(tweenFadeSpec(delayIndex)) }

    val paymentsPageViewModel: PaymentsPageViewModel = viewModel<PaymentsPageViewModel>()
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }
    var showTexts: Boolean by rememberSaveable { mutableStateOf(false) }
    val unpaidSum: Float by animateFloatAsState(
        targetValue = if (showTexts) paymentsPageViewModel.unpaidSum else 0f,
        animationSpec = tween(2000, 0, EaseOutQuad)
    )

    LaunchedEffect(Unit) {
        paymentsPageViewModel.fetchPayments()
        delay(50)
        showElements = true
        delay(150)
        showTexts = true
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            AnimatedVisibility(
                visible = showElements,
                enter = enterTransition(0)
            ) {
                Text(
                    text = "Płatności",
                    style = MaterialTheme.typography.headlineLarge,
                    color = UISingleton.color4.primaryColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
        item {
            Spacer(Modifier.height(16.dp))
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(2), modifier = Modifier.offset(y = (UISingleton.uiElementsCornerRadius).dp)) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = UISingleton.color3.primaryColor,
                        disabledContainerColor = UISingleton.color3.primaryColor,
                        contentColor = UISingleton.color4.primaryColor,
                        disabledContentColor = UISingleton.color4.primaryColor
                    ),
                    shape = RoundedCornerShape(topStart = UISingleton.uiElementsCornerRadius.dp, topEnd = UISingleton.uiElementsCornerRadius.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    Column(
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp).padding(bottom = (16 + UISingleton.uiElementsCornerRadius).dp)
                    ) {
                        AnimatedVisibility(showTexts, enter = enterTransition(2)) {
                            Text(
                                text = "${"%.2f".format(unpaidSum)} zł",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = UISingleton.color1.primaryColor

                            )
                        }
                        AnimatedVisibility(showTexts, enter = enterTransition(3)) {
                            Text(
                                text = "Do zapłaty",
                                style = MaterialTheme.typography.titleMedium,
                                color = UISingleton.color1.primaryColor
                            )
                        }
                    }
                }
            }
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(1)) {
                Box(
                    modifier = Modifier
                        .height(UISingleton.uiElementsCornerRadius.dp)
                        .fillMaxWidth()
                        .background(
                            UISingleton.color2.primaryColor,
                            RoundedCornerShape(
                                topStart = UISingleton.uiElementsCornerRadius.dp,
                                topEnd = UISingleton.uiElementsCornerRadius.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            )
                        )
                )
            }
        }
        val unpaidPayments: List<Payment>? = paymentsPageViewModel.unpaidPayments
        val paidPayments: List<Payment>? = paymentsPageViewModel.paidPayments
        val unpaidSize: Int = unpaidPayments?.size ?: 0
        val paidSize: Int = paidPayments?.size ?: 0
        item {
            AnimatedVisibility(showElements, enter = enterTransition(1)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(UISingleton.color2.primaryColor)
                        .padding(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 12.dp)
                ) {
                    AnimatedVisibility(showTexts, enter = enterTransition(4)) {
                        Text(
                            text = "Nierozliczone płatności",
                            style = MaterialTheme.typography.headlineSmall,
                        )

                    }
                }
            }
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(1)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(UISingleton.color2.primaryColor)
                        .padding(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 12.dp)
                ) {
                    AnimatedVisibility(showTexts, enter = enterTransition(5)) {
                        HorizontalDivider(
                            thickness = 5.dp,
                            color = UISingleton.color3.primaryColor,
                            modifier = Modifier
                                .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                        )
                    }
                }
            }
        }
        if (!unpaidPayments.isNullOrEmpty()) {
            items(unpaidPayments.size, key = { paymentIndex -> unpaidPayments[paymentIndex].id }) { paymentIndex ->
                AnimatedVisibility(showElements, enter = enterTransition(1)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(UISingleton.color2.primaryColor)
                            .padding(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 12.dp)
                    ) {
                        AnimatedVisibility(showTexts, enter = enterTransition(6 + paymentIndex)) {
                            PaymentView(unpaidPayments[paymentIndex])
                        }
                    }
                }
            }
        } else {
            item {
                AnimatedVisibility(showElements, enter = enterTransition(1)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(UISingleton.color2.primaryColor)
                            .padding(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 12.dp)
                    ) {
                        AnimatedVisibility(showTexts, enter = enterTransition(7 + unpaidSize)) {
                            Text(
                                text = "Brak nierozliczonych płatności",
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                    }
                }
            }
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(1)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(UISingleton.color2.primaryColor)
                        .padding(start = 12.dp, end = 12.dp, top = 24.dp, bottom = 12.dp)
                ) {
                    AnimatedVisibility(showTexts, enter = enterTransition(8 + unpaidSize)) {
                        Text(
                            text = "Rozliczone płatności",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                }
            }
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(1)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(UISingleton.color2.primaryColor)
                        .padding(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 12.dp)
                ) {
                    AnimatedVisibility(showTexts, enter = enterTransition(9 + unpaidSize)) {
                        HorizontalDivider(
                            thickness = 5.dp,
                            color = UISingleton.color3.primaryColor,
                            modifier = Modifier
                                .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                        )
                    }
                }
            }
        }
        if (!paidPayments.isNullOrEmpty()) {
            items(paidPayments.size, key = { paymentIndex -> paidPayments[paymentIndex].id }) { paymentIndex ->
                AnimatedVisibility(showElements, enter = enterTransition(1)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(UISingleton.color2.primaryColor)
                            .padding(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 12.dp)
                    ) {
                        AnimatedVisibility(showTexts, enter = enterTransition(10 + unpaidSize + paymentIndex)) {
                            PaymentView(paidPayments[paymentIndex])
                        }
                    }
                }
            }

        } else {
            item {
                AnimatedVisibility(showElements, enter = enterTransition(1)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(UISingleton.color2.primaryColor)
                            .padding(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 12.dp)
                    ) {
                        AnimatedVisibility(showTexts, enter = enterTransition(11 + unpaidSize + paidSize)) {
                            Text(
                                text = "Brak rozliczonych płatności",
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                    }
                }
            }
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(1)) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(UISingleton.uiElementsCornerRadius.dp)
                        .background(
                            UISingleton.color2.primaryColor,
                            RoundedCornerShape(
                                bottomStart = UISingleton.uiElementsCornerRadius.dp,
                                bottomEnd = UISingleton.uiElementsCornerRadius.dp
                            )
                        )
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun PaymentsPagePreview() {
    OAuthSingleton.setTestAccessToken()
    val currentScreen: Screens = Screens.HOME
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1.primaryColor)
            .padding(12.dp)
    ) {
        SharedTransitionLayout {
            AnimatedContent(targetState = currentScreen) { target ->
                if (currentScreen == target) {
                    PaymentsPageView(this@SharedTransitionLayout, this@AnimatedContent)
                }
            }
        }
    }
}