package com.cdkentertainment.mobilny_usos_enhanced.views

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
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Warning
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.view_models.AttendancePageViewModel

@Composable
fun AttendanceDateCardView(
    index: Int,
    date: String,
    viewModel: AttendancePageViewModel,
    modifier: Modifier
) {
    var showTypeSelector: Boolean by rememberSaveable { mutableStateOf(false) }
    val imageVectors: List<ImageVector> = listOf(
        Icons.Rounded.Edit,
        Icons.Rounded.Done,
        Icons.Rounded.Warning,
        Icons.Rounded.Close
    )
    val state = remember { mutableStateOf(0) }
    val shape: RoundedCornerShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = if (index == 5 - 1) UISingleton.uiElementsCornerRadius.dp else 0.dp,
        bottomEnd = if (index == 5 - 1) UISingleton.uiElementsCornerRadius.dp else 0.dp,
    )

    if (showTypeSelector) {
        AttendanceTypePopupView(
            value = state,
            onDismissExtra = {
                showTypeSelector = false
                println("Ok")
            },
            sex = 0
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
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
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
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f)
            )
            Icon(
                imageVector = imageVectors[state.value],
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