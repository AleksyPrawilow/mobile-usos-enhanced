package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton

@Composable
fun SemesterCardView(text: String) {
    Card(
        colors = CardColors(
            contentColor = UISingleton.textColor1,
            containerColor = UISingleton.color1,
            disabledContainerColor = UISingleton.color1,
            disabledContentColor = UISingleton.textColor1
        ),
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius),
        modifier = Modifier
            .border(5.dp, UISingleton.color2, RoundedCornerShape(
                UISingleton.uiElementsCornerRadius))
            .shadow(5.dp, RoundedCornerShape(UISingleton.uiElementsCornerRadius))
    ) {
        Text(
            text = text,
            color = UISingleton.textColor1,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(12.dp)
        )
    }
}