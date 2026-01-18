package com.cdkentertainment.muniversity.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cdkentertainment.muniversity.UISingleton
import com.cdkentertainment.muniversity.scaleIndependent

@Composable
fun GradeCardView(
    modifier: Modifier = Modifier,
    courseName: String = "Nazwa przedmiotu",
    grade: String = "5",
    showGrade: Boolean = true,
    showArrow: Boolean = false,
    backgroundColor: Color = UISingleton.color1,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    fontWeight: FontWeight = FontWeight.Medium,
    sideIcon: ImageVector? = null,
    elevation: Dp = 0.dp,
    onClick: (() -> Unit)? = null
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation, disabledElevation = elevation),
        onClick = onClick ?: {},
        enabled = onClick != null,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Min)
        ) {
            if (sideIcon != null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 6.dp)
                        .width(36.dp)
                        .background(UISingleton.color3)
                ) {
                    Icon(
                        imageVector = sideIcon,
                        contentDescription = "Icon",
                        tint = UISingleton.textColor4,
                        modifier = Modifier
                            .size(36.dp)
                            .padding(6.dp)
                    )
                }
            } else {
                Spacer(
                    modifier = Modifier.width(12.dp)
                )
            }
            Text(
                text = courseName,
                color = UISingleton.textColor1,
                style = textStyle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = fontWeight,
                modifier = Modifier.weight(1f).padding(top = 12.dp, bottom = 12.dp, end = 6.dp)
            )
            if (showArrow) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = "More",
                    tint = UISingleton.textColor1,
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 12.dp)
                )
            }
            if (showGrade) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp, end = 12.dp)
                        .defaultMinSize(minWidth = 40.dp)
                        .height(40.dp)
                        .background(UISingleton.color3, RoundedCornerShape(50.dp))
                        .padding(horizontal = 6.dp)
                ) {
                    Text(
                        text = grade,
                        color = UISingleton.textColor4,
                        fontSize = 17.sp.scaleIndependent,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}