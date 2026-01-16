package com.cdkentertainment.mobilny_usos_enhanced

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
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
    var termIds: List<SharedDataClasses.IdAndName> = mutableListOf()

    private val lectureIcon: Int = R.drawable.rounded_school_24
    private val labsIcon: Int = R.drawable.rounded_science_24
    private val classroomIcon: Int = R.drawable.rounded_assignment_24
    private val seminarIcon: Int = R.drawable.rounded_group_24
    private val tutorialIcon: Int = R.drawable.rounded_developer_guide_24
    private val workshopIcon: Int = R.drawable.rounded_settings_24
    private val outsideTrainingIcon: Int = R.drawable.rounded_nature_people_24
    private val sportsIcon: Int = R.drawable.rounded_sports_24
    val otherIcon: Int = R.drawable.rounded_menu_book_24

    val activityTypeIconMapping: Map<String, Int?> = mapOf(
        "CW1" to classroomIcon, // cw
        "CW2" to classroomIcon,
        "CW3" to classroomIcon,
        "CW4" to classroomIcon,
        "CW5" to classroomIcon,
        "PB" to otherIcon, // Projekt badawczy
        "MET" to null, // Zajęcia metodyczne
        "WMED" to otherIcon, // Warsztaty metodyczne
        "WYK" to lectureIcon, // wykład
        "CW" to classroomIcon,
        "LAB" to labsIcon, // Zajęcia laboratoryjne,
        "SEM" to seminarIcon, // Seminarium,
        "WF" to sportsIcon, // WF
        "KON" to null, // Konserwatorium
        "LEK" to null, // Lektorat
        "EGZ" to classroomIcon, // Egzamin
        "WAR" to workshopIcon, // Warsztat
        "MED" to null, // Zajęcia metodyczne w szkole
        "ZTER" to outsideTrainingIcon, // Zajęcia terenowe
        "WYK2" to lectureIcon, // Wykład
        "PRA" to lectureIcon, // Praktyka
        "HOST" to null, // Hospitacje
        "PROS" to seminarIcon, // Proseminarium
        "MET1" to null, // Zajęcia metodyczne w szkołach oraz placówkach opiekuńczo-wychowawczych
        "MET2" to null, // Zajęcia metodyczne w szkołach, przedszkolach oraz placówkach opiekuńczo-wychowawczych
        "MET3" to null, // Zajęcia metodyczne w szkołach i przedszkolach
        "MET4" to null, // Zajęcia metodyczne w szkołach i przedszkolach oraz placówkach diagnostyczno-terapeutycznych
        "MET5" to null, // Zajęcia metod. w placówkach diagnostyczno-terapeutycznych oraz opiekuńczo-wychowawczych
        "LEKT" to null, // Lektorat
        "TEST" to classroomIcon, // Test
        "TUT" to tutorialIcon, // Tutorial
        "KCW" to labsIcon, // kcw
    )
}

val TextUnit.scaleIndependent @Composable get() = (this.value / LocalDensity.current.fontScale).sp

@Composable
fun spToDp(sp: TextUnit): Dp {
    val density = LocalDensity.current
    return with(density) { sp.toDp() }
}