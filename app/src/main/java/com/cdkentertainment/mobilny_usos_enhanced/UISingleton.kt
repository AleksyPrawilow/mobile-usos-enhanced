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
    var color1: ColorObject by mutableStateOf(ColorObject(Color(0xFFF9F3EF), Color(0xFF1B3C53)))
        private set
    // UI elements background colors
    var color2: ColorObject by mutableStateOf(ColorObject(Color(0xFFD2C1B6), Color(0xFF456882)))
        private set
    // Light text colors
    var color3: ColorObject by mutableStateOf(ColorObject(Color(0xFF456882), Color(0xFFD2C1B6)))
        private set
    // Dark text colors
    var color4: ColorObject by mutableStateOf(ColorObject(Color(0xFF1B3C53), Color(0xFFF9F3EF)))
        private set
    var blurRadius: Dp by mutableStateOf(0.dp)
        private set

    private val lightTheme: Theme = Theme(
        color1 = ColorObject(Color(0xFFF9F3EF), Color(0xFF1B3C53)),
        color2 = ColorObject(Color(0xFFD2C1B6), Color(0xFF456882)),
        color3 = ColorObject(Color(0xFF456882), Color(0xFFD2C1B6)),
        color4 = ColorObject(Color(0xFF1B3C53), Color(0xFFF9F3EF))
    )
    private val darkTheme: Theme = Theme(
        color1 = ColorObject(Color(0xFF1B3C53), Color(0xFFF9F3EF)),
        color2 = ColorObject(Color(0xFF456882), Color(0xFFD2C1B6)),
        color3 = ColorObject(Color(0xFFD2C1B6), Color(0xFF456882)),
        color4 = ColorObject(Color(0xFFF9F3EF), Color(0xFF1B3C53))
    )

    val uiElementsCornerRadius: Int = 24

    fun changeTheme(dark: Boolean) {
        if (dark) {
            color1 = darkTheme.color1
            color2 = darkTheme.color2
            color3 = darkTheme.color3
            color4 = darkTheme.color4
        } else {
            color1 = lightTheme.color1
            color2 = lightTheme.color2
            color3 = lightTheme.color3
            color4 = lightTheme.color4
        }
    }

    fun blurContent() {
        blurRadius = 1.dp
    }

    fun dropBlurContent() {
        blurRadius = 0.dp
    }
}

data class Theme(
    val color1: ColorObject,
    val color2: ColorObject,
    val color3: ColorObject,
    val color4: ColorObject
)

data class ColorObject(
    val primaryColor: Color,
    val oppositeColor: Color
)