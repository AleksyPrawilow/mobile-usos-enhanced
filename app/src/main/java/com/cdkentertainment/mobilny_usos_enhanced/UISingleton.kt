package com.cdkentertainment.mobilny_usos_enhanced

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object UISingleton {
    // Pallete taken from https://colorhunt.co/palette/1b3c53456882d2c1b6f9f3ef

    // Background colors
    var color1: Color by mutableStateOf(Color(0xFFF9F3EF))
        private set
    // UI elements background colors
    var color2: Color by mutableStateOf(Color(0xFFD2C1B6))
        private set
    // Light text colors
    var color3: Color by mutableStateOf(Color(0xFF456882))
        private set
    // Dark text colors
    var color4: Color by mutableStateOf(Color(0xFF1B3C53))
        private set
    var textColor1: Color by mutableStateOf(Color(0xFF1B3C53))
        private set
    var textColor2: Color by mutableStateOf(Color(0xFF456882))
        private set
    var textColor3: Color by mutableStateOf(Color(0xFFBDC3C7))
        private set
    var textColor4: Color by mutableStateOf(Color(0xFFECF0F1))
        private set

    var blurRadius: Dp by mutableStateOf(0.dp)
        private set

    private val standardLightTextTheme: TextTheme = TextTheme(
        color1 = Color(0xFF1B3C53),
        color2 = Color(0xFF456882),
        color3 = Color(0xFFBDC3C7),
        color4 = Color(0xFFECF0F1)
    )

    private val standardDarkTextTheme: TextTheme = TextTheme(
        color1 = Color(0xFFECF0F1),
        color2 = Color(0xFFBDC3C7),
        color3 = Color(0xFF456882),
        color4 = Color(0xFF1B3C53)
    )

    private val testTheme: Theme = Theme(
        color1 = Color(0xFFFFFFFF),
        color2 = Color(0xFFEAEAEA),
        color3 = Color(0xFF456882),
        color4 = Color(0xFF1B3C53)
    )

    private val lightTheme: Theme = Theme(
        color1 = Color(0xFFF9F3EF),
        color2 = Color(0xFFD2C1B6),
        color3 = Color(0xFF456882),
        color4 = Color(0xFF1B3C53)
    )

    private val lowDarkTheme: Theme = Theme(
        color1 = Color(0xFF1B3C53),
        color2 = Color(0xFF456882),
        color3 = Color(0xFFD2C1B6),
        color4 = Color(0xFFF9F3EF),
        textTheme = standardDarkTextTheme
    )

    private val ultraDarkTextTheme: TextTheme = TextTheme(
        color1 = Color(0xFFFFEB3B),
        color2 = Color(0xFFE7D436),
        color3 = Color(0xFF2A2A2A),
        color4 = Color(0xFF3A3838)
    )

    private val darkTheme: Theme = Theme(
        color1 = Color(0xFF000000),
        color2 = Color(0xFF2A2A2A),
        color3 = Color(0xFFFFEB3B),
        color4 = Color(0xFFE7D436),
        textTheme = ultraDarkTextTheme
    )

    val uiElementsCornerRadius: Int = 24

    fun changeTheme(dark: Boolean) {
        if (dark) {
            color1 = darkTheme.color1
            color2 = darkTheme.color2
            color3 = darkTheme.color3
            color4 = darkTheme.color4
            textColor1 = darkTheme.textTheme.color1
            textColor2 = darkTheme.textTheme.color2
            textColor3 = darkTheme.textTheme.color3
            textColor4 = darkTheme.textTheme.color4
        } else {
            color1 = lightTheme.color1
            color2 = lightTheme.color2
            color3 = lightTheme.color3
            color4 = lightTheme.color4
            textColor1 = lightTheme.textTheme.color1
            textColor2 = lightTheme.textTheme.color2
            textColor3 = lightTheme.textTheme.color3
            textColor4 = lightTheme.textTheme.color4
        }
    }

    fun changeTheme(theme: Theme) {
        color1 = theme.color1
        color2 = theme.color2
        color3 = theme.color3
        color4 = theme.color4
        textColor1 = theme.textTheme.color1
        textColor2 = theme.textTheme.color2
        textColor3 = theme.textTheme.color3
        textColor4 = theme.textTheme.color4
    }

    fun blurContent() {
        blurRadius = 1.dp
    }

    fun dropBlurContent() {
        blurRadius = 0.dp
    }
}

data class TextTheme(
    val color1: Color = Color(0xFF1B3C53),
    val color2: Color = Color(0xFF456882),
    val color3: Color = Color(0xFFD2C1B6),
    val color4: Color = Color(0xFFF9F3EF)
)

data class Theme(
    val color1: Color,
    val color2: Color,
    val color3: Color,
    val color4: Color,
    val textTheme: TextTheme = TextTheme()
)