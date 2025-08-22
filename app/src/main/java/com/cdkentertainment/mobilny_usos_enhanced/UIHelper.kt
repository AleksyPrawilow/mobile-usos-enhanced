package com.cdkentertainment.mobilny_usos_enhanced

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.ui.unit.IntOffset

object UIHelper {
    private const val SLIDE_APPEAR_DURATION: Int = 500
    private const val SLIDE_DELAY_BETWEEN_SHOWS: Int = 150
    private val safeEasing = Easing { fraction ->
        EaseOutBack.transform(fraction.coerceIn(0f, 0.9999f))
    }
    private val tweenSlideSpec : (Int) -> FiniteAnimationSpec<IntOffset> = { delayIndex: Int -> tween(SLIDE_APPEAR_DURATION, SLIDE_DELAY_BETWEEN_SHOWS * delayIndex, safeEasing) }
    private val tweenFadeSpec  : (Int) -> FiniteAnimationSpec<Float>     = { delayIndex: Int -> tween(SLIDE_APPEAR_DURATION, SLIDE_DELAY_BETWEEN_SHOWS * delayIndex, safeEasing) }
    val slideEnterTransition: (Int) -> EnterTransition = { delayIndex: Int -> slideInHorizontally(tweenSlideSpec(delayIndex)) + fadeIn(tweenFadeSpec(delayIndex)) }
}