package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
    ) {
        Text(
            text = text,
            color = UISingleton.textColor1,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(12.dp)
        )
    }
}