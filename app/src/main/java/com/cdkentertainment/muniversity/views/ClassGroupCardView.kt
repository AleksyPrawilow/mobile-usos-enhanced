package com.cdkentertainment.muniversity.views

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cdkentertainment.muniversity.UIHelper
import com.cdkentertainment.muniversity.UISingleton
import com.cdkentertainment.muniversity.getLocalized
import com.cdkentertainment.muniversity.models.LessonGroup
import com.cdkentertainment.muniversity.scaleIndependent

@Composable
fun ClassGroupCardView(
    data: LessonGroup,
    onClick: () -> Unit = {}
) {
    val context: Context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                UISingleton.color1,
                RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
            )
            .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Text(
            text = UIHelper.classTypeIds[data.class_type_id]?.name?.getLocalized(context) ?: data.class_type_id,
            color = UISingleton.textColor1,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(end = 6.dp)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = "More",
            tint = UISingleton.textColor1,
            modifier = Modifier
                .padding(12.dp)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .defaultMinSize(minWidth = 48.dp)
                .height(48.dp)
                .background(UISingleton.color3, RoundedCornerShape(50.dp))
                .padding(horizontal = 6.dp)
        ) {
            Text(
                text = "${data.group_number}",
                color = UISingleton.textColor4,
                fontSize = 18.sp.scaleIndependent,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}