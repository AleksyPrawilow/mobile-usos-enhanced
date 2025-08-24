package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton

@Composable
fun TestNameDescriptionView(name: String, description: String) {
    var expanded: Boolean by rememberSaveable { mutableStateOf(false) }
    val arrowRotation: Float by animateFloatAsState(
        if (expanded) 180f else 0f
    )
    val onClick: () -> Unit = { expanded = !expanded }
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .background(UISingleton.color1.primaryColor)
                .clickable(onClick = onClick)
                .zIndex(1f)
        ) {
            Text(
                name,
                style = MaterialTheme.typography.titleMedium,
                color = UISingleton.color4.primaryColor,
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f)
            )
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = "More",
                tint = UISingleton.color4.primaryColor,
                modifier = Modifier
                    .graphicsLayer(
                        rotationZ = arrowRotation
                    )
                    .padding(12.dp)
            )
        }
        AnimatedVisibility(expanded, enter = expandVertically(), exit = shrinkVertically(), modifier = Modifier.offset(y = -UISingleton.uiElementsCornerRadius.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        UISingleton.color3.primaryColor,
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = UISingleton.uiElementsCornerRadius.dp,
                            bottomEnd = UISingleton.uiElementsCornerRadius.dp
                        )
                    )
                    .zIndex(0.75f)
                    .padding(top = 12.dp + UISingleton.uiElementsCornerRadius.dp, bottom = 12.dp)
                    .padding(horizontal = 6.dp)
            ) {
                Text(
                    description,
                    style = MaterialTheme.typography.titleMedium,
                    color = UISingleton.color1.primaryColor
                )
            }
        }
    }
}