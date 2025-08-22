package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
fun NodeFolderView(
    nestIndex: Int = 0
) {
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
                .clip(RoundedCornerShape(if (nestIndex == 0) UISingleton.uiElementsCornerRadius.dp else 0.dp))
                .background(UISingleton.color1.primaryColor)
                .clickable(onClick = onClick)
                .zIndex(1f)
        ) {
            Text(
                "This is a node folder $nestIndex",
                style = MaterialTheme.typography.titleLarge,
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
                        if (nestIndex % 2 == 0) UISingleton.color3.primaryColor else UISingleton.color2.primaryColor,
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = UISingleton.uiElementsCornerRadius.dp,
                            bottomEnd = UISingleton.uiElementsCornerRadius.dp
                        )
                    )
                    .border(5.dp,
                        UISingleton.color1.primaryColor,
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = UISingleton.uiElementsCornerRadius.dp,
                            bottomEnd = UISingleton.uiElementsCornerRadius.dp
                        )
                    )
                    .zIndex(0.75f)
                    .padding(top = 12.dp + UISingleton.uiElementsCornerRadius.dp, bottom = UISingleton.uiElementsCornerRadius.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // if the node is not a folder wrap it in a box with horizontal padding
                    Box(
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        NodeGradeView()
                    }
                    if (nestIndex < 2) {
                        NodeFolderView(nestIndex = nestIndex + 1)
                    }
                    Box(
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        NodeGradeView()
                    }
                }
            }
        }
    }
}