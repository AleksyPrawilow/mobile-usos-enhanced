package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Payment
import com.cdkentertainment.mobilny_usos_enhanced.view_models.PaymentsPageViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import kotlinx.coroutines.delay

@Composable
fun PaymentsPageView() {
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    val paymentsPageViewModel: PaymentsPageViewModel = viewModel<PaymentsPageViewModel>()
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }
    var showTexts: Boolean by rememberSaveable { mutableStateOf(false) }
    val unpaidSum: Float by animateFloatAsState(
        targetValue = if (showTexts) paymentsPageViewModel.unpaidSum else 0f,
        animationSpec = tween(2000, 0, EaseOutQuad)
    )
    val density: Density = LocalDensity.current
    val insets = WindowInsets.systemBars
    val topInset = insets.getTop(density)
    val bottomInset = insets.getBottom(density)
    val topPadding = with(density) { topInset.toDp() }
    val bottomPadding = with(density) { bottomInset.toDp() }
    val paddingModifier: Modifier = Modifier.padding(horizontal = UISingleton.horizontalPadding)

    LaunchedEffect(Unit) {
        paymentsPageViewModel.fetchPayments()
        delay(150)
        showElements = true
        delay(150)
        showTexts = true
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding, bottom = bottomPadding)
    ) {
        item {
            PageHeaderView(stringResource(R.string.payments_page))
        }
        item {
            Spacer(Modifier.height(16.dp))
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(2), modifier = paddingModifier) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = UISingleton.color3,
                        disabledContainerColor = UISingleton.color3,
                        contentColor = UISingleton.textColor1,
                        disabledContentColor = UISingleton.textColor1
                    ),
                    shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .border(5.dp, UISingleton.color4, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        AnimatedVisibility(showTexts, enter = enterTransition(2)) {
                            Text(
                                text = "${"%.2f".format(unpaidSum)} zł",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = UISingleton.textColor4
                            )
                        }
                        AnimatedVisibility(showTexts, enter = enterTransition(3)) {
                            Text(
                                text = "Do zapłaty",
                                style = MaterialTheme.typography.titleMedium,
                                color = UISingleton.textColor4
                            )
                        }
                    }
                }
            }
        }
        item {
            Spacer(Modifier.height(16.dp))
        }
        val unpaidPayments: List<Payment>? = paymentsPageViewModel.unpaidPayments
        val paidPayments: List<Payment>? = paymentsPageViewModel.paidPayments
        val unpaidSize: Int = unpaidPayments?.size ?: 0
        val paidSize: Int = paidPayments?.size ?: 0
        item {
            AnimatedVisibility(showTexts, enter = enterTransition(4), modifier = paddingModifier) {
                Text(
                    text = "Nierozliczone płatności",
                    style = MaterialTheme.typography.headlineSmall,
                    color = UISingleton.textColor1
                )
            }
        }
        item {
            AnimatedVisibility(showTexts, enter = enterTransition(5), modifier = paddingModifier) {
                HorizontalDivider(
                    thickness = 5.dp,
                    color = UISingleton.textColor2,
                    modifier = Modifier
                        .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                )
            }
        }
        if (!unpaidPayments.isNullOrEmpty()) {
            items(unpaidPayments.size, key = { paymentIndex -> unpaidPayments[paymentIndex].id }) { paymentIndex ->
                AnimatedVisibility(showTexts, enter = enterTransition(6 + paymentIndex), modifier = paddingModifier) {
                    PaymentView(unpaidPayments[paymentIndex])
                }
            }
        } else {
            item {
                AnimatedVisibility(showTexts, enter = enterTransition(7 + unpaidSize), modifier = paddingModifier) {
                    Text(
                        text = "Brak nierozliczonych płatności",
                        style = MaterialTheme.typography.titleLarge,
                        color = UISingleton.textColor1
                    )
                }
            }
        }
        item {
            Spacer(Modifier.height(16.dp))
        }
        item {
            AnimatedVisibility(showTexts, enter = enterTransition(8 + unpaidSize), modifier = paddingModifier) {
                Text(
                    text = "Rozliczone płatności",
                    style = MaterialTheme.typography.headlineSmall,
                    color = UISingleton.textColor1
                )
            }
        }
        item {
            AnimatedVisibility(showTexts, enter = enterTransition(9 + unpaidSize), modifier = paddingModifier) {
                HorizontalDivider(
                    thickness = 5.dp,
                    color = UISingleton.textColor2,
                    modifier = Modifier
                        .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                )
            }
        }
        if (!paidPayments.isNullOrEmpty()) {
            items(paidPayments.size, key = { paymentIndex -> paidPayments[paymentIndex].id }) { paymentIndex ->
                AnimatedVisibility(showTexts, enter = enterTransition(10 + unpaidSize + paymentIndex), modifier = paddingModifier) {
                    PaymentView(paidPayments[paymentIndex])
                }
            }
        } else {
            item {
                AnimatedVisibility(showTexts, enter = enterTransition(11 + unpaidSize + paidSize), modifier = paddingModifier) {
                    Text(
                        text = "Brak rozliczonych płatności",
                        style = MaterialTheme.typography.titleLarge,
                        color = UISingleton.textColor1
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentsPagePreview() {
    OAuthSingleton.setTestAccessToken()
    val currentScreen: Screens = Screens.HOME
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1)
            .padding(12.dp)
    ) {
        AnimatedContent(targetState = currentScreen) { target ->
            if (currentScreen == target) {
                PaymentsPageView()
            }
        }
    }
}