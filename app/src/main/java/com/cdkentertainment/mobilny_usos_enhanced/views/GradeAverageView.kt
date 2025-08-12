package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
            contentColor = UISingleton.color4.primaryColor,
            containerColor = UISingleton.color2.primaryColor,
            disabledContainerColor = UISingleton.color2.primaryColor,
            disabledContentColor = UISingleton.color4.primaryColor
        ),
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius),
        modifier = Modifier
            .fillMaxWidth()
            .border(5.dp, UISingleton.color3.primaryColor, RoundedCornerShape(UISingleton.uiElementsCornerRadius))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp)
                .background(UISingleton.color1.primaryColor, RoundedCornerShape(
                    UISingleton.uiElementsCornerRadius.dp
                ))
                .padding(12.dp)
        ) {
            Text(
                text = "Bieżąca średnia:",
                color = UISingleton.color4.primaryColor,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.weight(1f))
            Card(
                colors = CardColors(
                    contentColor = UISingleton.color4.primaryColor,
                    containerColor = UISingleton.color1.primaryColor,
                    disabledContainerColor = UISingleton.color1.primaryColor,
                    disabledContentColor = UISingleton.color4.primaryColor
                ),
                shape = CircleShape,
                modifier = Modifier
                    .size(48.dp)
                    .border(5.dp, UISingleton.color2.primaryColor, CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = "%.2f".format(gradeAverage),
                        color = UISingleton.color4.primaryColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}