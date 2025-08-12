package com.cdkentertainment.mobilny_usos_enhanced

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

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

    val uiElementsCornerRadius: Int = 24

    fun changeTheme() {
        color1 = color1.swapCopy()
        color2 = color2.swapCopy()
        color3 = color3.swapCopy()
        color4 = color4.swapCopy()
    }
}

data class ColorObject(val primaryColor: Color, val oppositeColor: Color) {
    fun swapCopy() = ColorObject(oppositeColor, primaryColor)
}