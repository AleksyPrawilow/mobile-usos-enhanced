package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton

@Composable
fun RatingStageView(
    headline: String,
    rating: Int,
    headerMaxHeight: Int,
    onRatingChange: (Int) -> Unit
) {
    val context: Context = LocalContext.current
    val rateNames: List<String> = listOf(
        stringResource(R.string.not_chosen),
        stringResource(R.string.disaster_rate),
        stringResource(R.string.meh_rate),
        stringResource(R.string.ok_rate),
        stringResource(R.string.nice_rate),
        stringResource(R.string.awesome_rate)
    )
    val density: Density = LocalDensity.current

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = headline,
            style = MaterialTheme.typography.titleMedium,
            color = UISingleton.textColor1,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .height(with(density) { headerMaxHeight.toDp() })
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(5) { index ->
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (index + 1 <= rating) UISingleton.color3 else UISingleton.color2,
                        containerColor = Color.Transparent
                    ),
                    onClick = {
                        onRatingChange(index + 1)
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "star",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        Text(
            text = rateNames[rating],
            style = MaterialTheme.typography.titleLarge,
            color = UISingleton.textColor1,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
}

enum class RatingStage {
    First,
    Second,
    Third,
    Fourth,
    Fifth,
    Submit
}