package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton

@Composable
fun GradeCardView(
    courseName: String = "Nazwa przedmiotu",
    grade: String = "5",
    showGrade: Boolean = true,
    showArrow: Boolean = false,
    backgroundColor: Color = UISingleton.color1,
    onClick: (() -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            .background(
                backgroundColor,
            )
            .clickable(onClick = onClick ?: {}, enabled = onClick != null)
            .padding(12.dp)
    ) {
        Text(
            text = courseName,
            color = UISingleton.textColor1,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f).padding(end = 6.dp)
        )
        if (showArrow) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = "More",
                tint = UISingleton.textColor1,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            )
        }
        if (showGrade) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .defaultMinSize(minWidth = 40.dp)
                    .height(40.dp)
                    .background(UISingleton.color3, RoundedCornerShape(50.dp))
                    .padding(horizontal = 6.dp)
            ) {
                Text(
                    text = grade,
                    color = UISingleton.textColor4,
                    fontSize = 17.sp.scaleIndependent(),
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}