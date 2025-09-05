package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton

@Composable
fun GradeAverageView(gradeAverage: Float?) {
    Card(
        colors = CardColors(
            contentColor = UISingleton.textColor1,
            containerColor = UISingleton.color3,
            disabledContainerColor = UISingleton.color3,
            disabledContentColor = UISingleton.textColor1
        ),
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius),
        modifier = Modifier
            .fillMaxWidth()
            .border(5.dp, UISingleton.color4, RoundedCornerShape(UISingleton.uiElementsCornerRadius))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = "Bieżąca średnia:",
                color = UISingleton.textColor4,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .defaultMinSize(minWidth = 48.dp)
                    .height(48.dp)
                    .background(UISingleton.color2, RoundedCornerShape(50.dp))
                    .padding(horizontal = 6.dp)
            ) {
                Text(
                    text = if (gradeAverage?.isNaN() ?: true) "-" else "%.2f".format(gradeAverage),
                    color = UISingleton.textColor1,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp.scaleIndependent(),
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}