package com.cdkentertainment.muniversity.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cdkentertainment.muniversity.UISingleton
import com.cdkentertainment.muniversity.scaleIndependent

@Composable
fun AttendanceStatCardView(
    statName: String,
    statValue: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                UISingleton.color2,
                RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = statName,
            color = UISingleton.textColor1,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        //Spacer(modifier = Modifier.weight(1f))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .defaultMinSize(minWidth = 48.dp)
                .height(48.dp)
                .background(UISingleton.color3, RoundedCornerShape(50.dp))
                .padding(horizontal = 6.dp)
        )  {
            Text(
                text = statValue,
                color = UISingleton.textColor4,
                fontSize = 14.sp.scaleIndependent,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}