package com.cdkentertainment.muniversity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cdkentertainment.muniversity.models.SharedDataClasses

object UISingleton {
    var isDarkTheme: Boolean by mutableStateOf(false)
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

    private val oledTextTheme: TextTheme = TextTheme(
        color1 = Color(0xFFFFEB3B),
        color2 = Color(0xFFE7D436),
        color3 = Color(0xFF2A2A2A),
        color4 = Color(0xFF3A3838)
    )

    private val lightTheme: Theme = Theme(
        id = 0,
        color1 = Color(0xFFFFFFFF),
        color2 = Color(0xFFEAEAEA),
        color3 = Color(0xFF456882),
        color4 = Color(0xFF1B3C53),
        isDark = false,
        textTheme = standardLightTextTheme
    )

    private val lowDarkTheme: Theme = Theme(
        id = 1,
        color1 = Color(0xFF1B3C53),
        color2 = Color(0xFF456882),
        color3 = Color(0xFFEAEAEA),
        color4 = Color(0xFFFFFFFF),
        isDark = true,
        textTheme = standardDarkTextTheme
    )

    private val oledTheme: Theme = Theme(
        id = 3,
        color1 = Color(0xFF000000),
        color2 = Color(0xFF2A2A2A),
        color3 = Color(0xFFFFEB3B),
        color4 = Color(0xFFE7D436),
        isDark = true,
        textTheme = oledTextTheme
    )

    val themes: Map<SharedDataClasses.LangDict, Theme> = mapOf(
        SharedDataClasses.LangDict("Jasny", "Light") to lightTheme,
        SharedDataClasses.LangDict("Ciemny", "Dark") to lowDarkTheme,
        SharedDataClasses.LangDict("Wysoki kontrast", "High contrast") to oledTheme
    )

    val uiElementsCornerRadius: Int = 16
    val horizontalPadding: Dp = 12.dp
    val verticalPadding: Dp = 24.dp

    fun changeTheme(theme: Theme) {
        color1 = theme.color1
        color2 = theme.color2
        color3 = theme.color3
        color4 = theme.color4
        textColor1 = theme.textTheme.color1
        textColor2 = theme.textTheme.color2
        textColor3 = theme.textTheme.color3
        textColor4 = theme.textTheme.color4
        isDarkTheme = theme.isDark
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
    val color3: Color = Color(0xFFBDC3C7),
    val color4: Color = Color(0xFFECF0F1),
)

data class Theme(
    val id: Int,
    val color1: Color,
    val color2: Color,
    val color3: Color,
    val color4: Color,
    val isDark: Boolean = false,
    val textTheme: TextTheme = TextTheme()
)