package com.cdkentertainment.muniversity.views

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cdkentertainment.muniversity.R
import com.cdkentertainment.muniversity.UISingleton

@Composable
fun LecturerAddRateSummaryView() {
    val context: Context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.rate_thanks),
            style = MaterialTheme.typography.titleLarge,
            color = UISingleton.textColor1
        )
        Text(
            text = stringResource(R.string.sending_rate),
            style = MaterialTheme.typography.titleMedium,
            color = UISingleton.textColor1,
        )
        CircularProgressIndicator(
            color = UISingleton.textColor2
        )
    }
}