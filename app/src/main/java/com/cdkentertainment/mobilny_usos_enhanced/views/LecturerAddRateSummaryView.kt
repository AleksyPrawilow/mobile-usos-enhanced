package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton

@Composable
fun LecturerAddRateSummaryView() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Dziękujemy za wypełnienie ankiety!",
            style = MaterialTheme.typography.titleLarge,
            color = UISingleton.color4.primaryColor
        )
        Text(
            text = "Wysyłam ocenę...",
            style = MaterialTheme.typography.titleMedium,
            color = UISingleton.color4.primaryColor,
        )
        CircularProgressIndicator(
            color = UISingleton.color3.primaryColor
        )
    }
}