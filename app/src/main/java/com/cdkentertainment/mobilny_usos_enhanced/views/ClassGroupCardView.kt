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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup

@Composable
fun ClassGroupCardView(
    data: LessonGroup,
    onClick: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                UISingleton.color1.primaryColor,
                RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
            )
            .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Text(
            text = data.class_type_id,
            color = UISingleton.color4.primaryColor,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .defaultMinSize(minWidth = 48.dp)
                .height(48.dp)
                .background(UISingleton.color3.primaryColor, RoundedCornerShape(50.dp))
                .padding(horizontal = 6.dp)
        ) {
            Text(
                text = "${data.group_number}",
                color = UISingleton.color1.primaryColor,
                fontSize = 18.sp.scaleIndependent(),
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}