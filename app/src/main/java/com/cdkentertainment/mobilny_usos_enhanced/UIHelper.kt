package com.cdkentertainment.mobilny_usos_enhanced

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntOffset
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses

object UIHelper {
    private const val SLIDE_APPEAR_DURATION: Int = 350
    private const val SLIDE_DELAY_BETWEEN_SHOWS: Int = 100
    private val safeEasing = Easing { fraction ->
        EaseOutBack.transform(fraction.coerceIn(0f, 0.9999f))
    }
    private val tweenSlideSpec : (Int) -> FiniteAnimationSpec<IntOffset> = { delayIndex: Int -> tween(SLIDE_APPEAR_DURATION, SLIDE_DELAY_BETWEEN_SHOWS * delayIndex) }
    private val tweenFadeSpec  : (Int) -> FiniteAnimationSpec<Float>     = { delayIndex: Int -> tween(SLIDE_APPEAR_DURATION, SLIDE_DELAY_BETWEEN_SHOWS * delayIndex) }
    val slideEnterTransition: (Int) -> EnterTransition = { delayIndex: Int -> slideInHorizontally(tweenSlideSpec(delayIndex)) + fadeIn(tweenFadeSpec(delayIndex)) }
    val scaleEnterTransition: (Int) -> EnterTransition = { delayIndex: Int -> scaleIn(tweenFadeSpec(delayIndex)) + fadeIn(tweenFadeSpec(delayIndex)) }

    var classTypeIds: Map<String, SharedDataClasses.IdAndName> by mutableStateOf(emptyMap())
}