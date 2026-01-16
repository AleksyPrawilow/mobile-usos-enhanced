package com.cdkentertainment.bux.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.bux.R
import com.cdkentertainment.bux.UIHelper
import com.cdkentertainment.bux.UIHelper.scaleEnterTransition
import com.cdkentertainment.bux.UISingleton
import com.cdkentertainment.bux.models.Payment
import com.cdkentertainment.bux.view_models.PaymentsPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PaymentsPageView() {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    val paymentsPageViewModel: PaymentsPageViewModel = viewModel<PaymentsPageViewModel>()

    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }
    var showTexts: Boolean by rememberSaveable { mutableStateOf(false) }

    val unpaidSum: Float by animateFloatAsState(
        targetValue = if (showTexts) paymentsPageViewModel.unpaidSum else 0f,
        animationSpec = tween(2000, 0, EaseOutQuad)
    )

    var showPaid: Boolean by rememberSaveable { mutableStateOf(false) }
    var showUnpaid: Boolean by rememberSaveable { mutableStateOf(false) }
    var shownDebts: ShownDebts by rememberSaveable { mutableStateOf(ShownDebts.UNPAID) }

    val paddingModifier: Modifier = Modifier.padding(
        horizontal = UISingleton.horizontalPadding,
        vertical = 8.dp
    )

    val textMeasurer = rememberTextMeasurer()
    val cardLabels: List<Pair<String, ImageVector>> = listOf(
        Pair(
            stringResource(R.string.unpaid_debts),
            ImageVector.vectorResource(R.drawable.rounded_payments_24)
        ),
        Pair(
            stringResource(R.string.paid_debts),
            Icons.Rounded.Done
        )
    )

    val cardLabelStyle: TextStyle = MaterialTheme.typography.titleLarge
    val maxCardWidth: Int = remember(cardLabels, cardLabelStyle) {
        cardLabels.maxOf {
            textMeasurer.measure(
                text = AnnotatedString(it.first),
                style = cardLabelStyle
            ).size.width
        }
    }

    val onStart: () -> Unit = {
        coroutineScope.launch {
            paymentsPageViewModel.fetchPayments()
            delay(150)
            showElements = true
            delay(150)
            showUnpaid = true
            showTexts = true
        }
    }

    LaunchedEffect(Unit) {
        onStart()
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        item {
            PageHeaderView(
                text = stringResource(R.string.payments_page),
                icon = ImageVector.vectorResource(R.drawable.rounded_payments_24)
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            AnimatedVisibility(
                visible = paymentsPageViewModel.loading,
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
                visible = paymentsPageViewModel.error && showElements,
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

        val unpaidPayments: List<Payment>? = paymentsPageViewModel.unpaidPayments
        val paidPayments: List<Payment>? = paymentsPageViewModel.paidPayments

        item {
            FlowRow(
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
            ) {
                AnimatedVisibility(
                    showTexts && paymentsPageViewModel.loaded,
                    enter = scaleEnterTransition(1)
                ) {
                    LatestSomethingView(
                        icon = cardLabels[0].second,
                        title = cardLabels[0].first,
                        maxWidth = maxCardWidth,
                        maxLines = 2,
                        disabledTextColor = UISingleton.textColor4,
                        disabledBackgroundColor = UISingleton.color3,
                        enabled = shownDebts != ShownDebts.UNPAID
                    ) {
                        coroutineScope.launch {
                            shownDebts = ShownDebts.UNPAID
                            showPaid = false
                            delay(50)
                            showUnpaid = true
                        }
                    }
                }

                AnimatedVisibility(
                    showTexts && paymentsPageViewModel.loaded,
                    enter = scaleEnterTransition(2)
                ) {
                    LatestSomethingView(
                        icon = cardLabels[1].second,
                        title = cardLabels[1].first,
                        maxWidth = maxCardWidth,
                        maxLines = 2,
                        disabledTextColor = UISingleton.textColor4,
                        disabledBackgroundColor = UISingleton.color3,
                        enabled = shownDebts != ShownDebts.PAID
                    ) {
                        coroutineScope.launch {
                            shownDebts = ShownDebts.PAID
                            showUnpaid = false
                            delay(50)
                            showPaid = true
                        }
                    }
                }
            }
        }

        if (shownDebts == ShownDebts.UNPAID) {
            item {
                AnimatedVisibility(
                    showElements && showUnpaid && paymentsPageViewModel.loaded,
                    enter = enterTransition(2)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = UISingleton.color2,
                            disabledContainerColor = UISingleton.color2,
                            contentColor = UISingleton.textColor1,
                            disabledContentColor = UISingleton.textColor1
                        ),
                        elevation = CardDefaults.cardElevation(3.dp),
                        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
                        modifier = paddingModifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.rounded_payments_24),
                                contentDescription = "To pay",
                                tint = UISingleton.textColor1,
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(end = 6.dp)
                            )

                            Column {
                                AnimatedVisibility(
                                    showTexts && paymentsPageViewModel.loaded,
                                    enter = enterTransition(2)
                                ) {
                                    Text(
                                        text = "${"%.2f".format(unpaidSum)} zł",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = UISingleton.textColor1
                                    )
                                }

                                AnimatedVisibility(
                                    showTexts && paymentsPageViewModel.loaded,
                                    enter = enterTransition(3)
                                ) {
                                    Text(
                                        text = stringResource(R.string.to_pay),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Light,
                                        color = UISingleton.textColor1
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (!unpaidPayments.isNullOrEmpty()) {
                items(unpaidPayments.size) { index ->
                    AnimatedVisibility(
                        visible = showTexts && showUnpaid && paymentsPageViewModel.loaded,
                        enter = enterTransition(3 + index)
                    ) {
                        PaymentView(
                            payment = unpaidPayments[index],
                            modifier = paddingModifier
                        )
                    }
                }
            } else {
                item {
                    AnimatedVisibility(
                        showTexts && showUnpaid && paymentsPageViewModel.loaded,
                        enter = enterTransition(3)
                    ) {
                        TextAndIconCardView(
                            title = stringResource(R.string.no_unpaid_debts),
                            icon = Icons.Rounded.Done,
                            modifier = paddingModifier,
                            backgroundColor = UISingleton.color2
                        )
                    }
                }
            }
        } else {
            if (!paidPayments.isNullOrEmpty()) {
                items(paidPayments.size) { index ->
                    AnimatedVisibility(
                        visible = showTexts && showPaid && paymentsPageViewModel.loaded,
                        enter = enterTransition(2 + index)
                    ) {
                        PaymentView(
                            payment = paidPayments[index],
                            modifier = paddingModifier
                        )
                    }
                }
            } else {
                item {
                    AnimatedVisibility(
                        showTexts && showPaid && paymentsPageViewModel.loaded,
                        enter = enterTransition(2)
                    ) {
                        TextAndIconCardView(
                            title = stringResource(R.string.no_paid_debts),
                            icon = Icons.Rounded.Close,
                            modifier = paddingModifier,
                            backgroundColor = UISingleton.color2
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }

        item { Spacer(modifier = Modifier.height(64.dp)) }
    }
}

private enum class ShownDebts {
    PAID,
    UNPAID
}