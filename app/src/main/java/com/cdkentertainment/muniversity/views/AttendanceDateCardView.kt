package com.cdkentertainment.muniversity.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cdkentertainment.muniversity.R
import com.cdkentertainment.muniversity.UISingleton

@Composable
fun AttendanceDateCardView(
    index: Int,
    maxIndex: Int,
    date: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    var showTypeSelector: Boolean by rememberSaveable { mutableStateOf(false) }
    val imageVectors: List<ImageVector> = listOf(
        ImageVector.vectorResource(R.drawable.rounded_edit_24),
        Icons.Rounded.Done,
        ImageVector.vectorResource(R.drawable.rounded_warning_24),
        Icons.Rounded.Close,
        ImageVector.vectorResource(R.drawable.rounded_door_open_24),
    )
    val state = remember { mutableStateOf(1) }
    val shape: RoundedCornerShape = remember {
        RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = if (index == maxIndex - 1) UISingleton.uiElementsCornerRadius.dp else 0.dp,
            bottomEnd = if (index == maxIndex - 1) UISingleton.uiElementsCornerRadius.dp else 0.dp,
        )
    }

    if (showTypeSelector) {
        AttendanceTypePopupView(
            value = state,
            onDismissExtra = {
                showTypeSelector = false
            }
        )
    }

    Card(
        colors = CardColors(
            contentColor = UISingleton.textColor1,
            containerColor = UISingleton.color2,
            disabledContainerColor = UISingleton.color2,
            disabledContentColor = UISingleton.textColor1
        ),
        onClick = {
            showTypeSelector = true
        },
        enabled = enabled,
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .shadow(3.dp, shape)
            .background(UISingleton.color1, shape)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .then(modifier)
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
                text = date,
                style = MaterialTheme.typography.titleMedium,
                color = UISingleton.textColor1,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .weight(1f)
            )
            Icon(
                imageVector = if (enabled == false) icon!! else imageVectors[state.value],
                contentDescription = "Icon",
                tint = UISingleton.textColor4,
                modifier = Modifier
                    .size(48.dp)
                    .background(UISingleton.color3, CircleShape)
                    .padding(8.dp)
            )
        }
    }
}