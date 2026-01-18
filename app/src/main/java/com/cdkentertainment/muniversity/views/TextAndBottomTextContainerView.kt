package com.cdkentertainment.muniversity.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cdkentertainment.muniversity.UISingleton
import com.cdkentertainment.muniversity.scaleIndependent

@Composable
fun TextAndBottomTextContainerView(
    title: String,
    highlightedText: String,
    bottomFirstText: String,
    bottomSecondText: String,
    showHighlightedText: Boolean = true,
    showArrow: Boolean = true,
    backgroundColor: Color = UISingleton.color2,
    elevation: Dp = 0.dp,
    onClick: (() -> Unit)? = null
) {
    val shape: RoundedCornerShape = remember { RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp) }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor,
            contentColor = UISingleton.textColor1,
            disabledContentColor = UISingleton.textColor1
        ),
        shape = shape,
        elevation = CardDefaults.cardElevation(elevation, disabledElevation = elevation),
        enabled = onClick != null,
        onClick = onClick ?: {},
    ) {
        Box(
            modifier = Modifier
                .height(intrinsicSize = IntrinsicSize.Min)
        ) {
            VerticalDivider(
                color = UISingleton.color3,
                thickness = UISingleton.uiElementsCornerRadius.dp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
            )
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = UISingleton.uiElementsCornerRadius.dp, top = 12.dp, bottom = 12.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = UISingleton.textColor1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 6.dp)
                    )
                    if (showArrow) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = "More",
                            tint = UISingleton.textColor1,
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                        )
                    }
                    if (showHighlightedText) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .defaultMinSize(minWidth = 40.dp)
                                .height(40.dp)
                                .offset(x = UISingleton.uiElementsCornerRadius.dp)
                                .background(
                                    color = UISingleton.color4,
                                    shape = RoundedCornerShape(
                                        topEnd = UISingleton.uiElementsCornerRadius.dp,
                                    )
                                )
                                .offset(x = -UISingleton.uiElementsCornerRadius.dp)
                                .background(
                                    color = UISingleton.color4,
                                    shape = RoundedCornerShape(
                                        topStart = UISingleton.uiElementsCornerRadius.dp,
                                        bottomStart = UISingleton.uiElementsCornerRadius.dp
                                    )
                                )
                                .padding(horizontal = 6.dp)
                        ) {
                            Text(
                                text = highlightedText,
                                color = UISingleton.textColor4,
                                fontSize = 17.sp.scaleIndependent,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1,
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(UISingleton.uiElementsCornerRadius.dp)
                                    .offset(x = UISingleton.uiElementsCornerRadius.dp + 6.dp, y = UISingleton.uiElementsCornerRadius.dp)
                                    .background(
                                        color = UISingleton.color4,
                                    )
                                    .background(
                                        color = UISingleton.color3,
                                        shape = RoundedCornerShape(
                                            topEnd = UISingleton.uiElementsCornerRadius.dp
                                        )
                                    )
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = bottomFirstText,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        color = UISingleton.textColor4,
                        modifier = Modifier
                            .background(
                                UISingleton.color3,
                                RoundedCornerShape(
                                    bottomStart = UISingleton.uiElementsCornerRadius.dp,
                                )
                            )
                            .background(
                                UISingleton.color4,
                                RoundedCornerShape(
                                    bottomStart = UISingleton.uiElementsCornerRadius.dp,
                                    bottomEnd = UISingleton.uiElementsCornerRadius.dp,
                                )
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                    Text(
                        text = bottomSecondText,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        color = UISingleton.textColor4,
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                UISingleton.color3,
                                RoundedCornerShape(
                                    bottomEnd = UISingleton.uiElementsCornerRadius.dp,
                                )
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}