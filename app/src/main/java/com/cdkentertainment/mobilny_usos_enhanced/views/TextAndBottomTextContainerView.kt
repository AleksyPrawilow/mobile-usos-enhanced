package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton

@Composable
fun TextAndBottomTextContainerView(
    title: String,
    highlightedText: String,
    bottomFirstText: String,
    bottomSecondText: String,
    showHighlightedText: Boolean = true,
    showArrow: Boolean = true,
    backgroundColor: Color = UISingleton.color2,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .drawBehind {
                drawRoundRect(
                    UISingleton.color3,
                    cornerRadius = CornerRadius(UISingleton.uiElementsCornerRadius.dp.toPx(), UISingleton.uiElementsCornerRadius.dp.toPx())
                )
            }
            .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            .clickable(onClick = onClick ?: {}, enabled = onClick != null)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                disabledContainerColor = backgroundColor,
                contentColor = UISingleton.textColor1,
                disabledContentColor = UISingleton.textColor1
            ),
            shape = RoundedCornerShape(
                bottomStart = 0.dp,
                topEnd = UISingleton.uiElementsCornerRadius.dp,
                topStart = UISingleton.uiElementsCornerRadius.dp,
                bottomEnd = 0.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
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
                            .padding(horizontal = 12.dp)
                    )
                }
                if (showHighlightedText) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .defaultMinSize(minWidth = 40.dp)
                            .height(40.dp)
                            .background(UISingleton.color3, RoundedCornerShape(50.dp))
                            .padding(horizontal = 6.dp)
                    ) {
                        Text(
                            text = highlightedText,
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
                        UISingleton.color4,
                        RoundedCornerShape(
                            bottomStart = UISingleton.uiElementsCornerRadius.dp,
                            bottomEnd = UISingleton.uiElementsCornerRadius.dp,
                            topStart = 0.dp,
                            topEnd = 0.dp
                        )
                    )
                    .zIndex(0.75f)
                    .padding(top = 4.dp, bottom = 4.dp, start = 12.dp, end = 12.dp)
            )
            Text(
                text = bottomSecondText,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                color = UISingleton.textColor4,
                modifier = Modifier
                    .offset(x = -UISingleton.uiElementsCornerRadius.dp)
                    .weight(1f)
                    .background(
                        UISingleton.color3,
                        RoundedCornerShape(
                            bottomStart = 0.dp,
                            bottomEnd = UISingleton.uiElementsCornerRadius.dp,
                            topStart = 0.dp,
                            topEnd = 0.dp
                        )
                    )
                    .zIndex(0.5f)
                    .padding(top = 4.dp, bottom = 4.dp, start = (12 + UISingleton.uiElementsCornerRadius).dp, end = 12.dp)
            )
        }
    }
}