package com.cdkentertainment.bux.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cdkentertainment.bux.R
import com.cdkentertainment.bux.UISingleton
import com.cdkentertainment.bux.scaleIndependent

@Composable
fun GradeAverageView(gradeAverage: Float?, modifier: Modifier = Modifier) {
    Card(
        colors = CardColors(
            contentColor = UISingleton.textColor1,
            containerColor = UISingleton.color2,
            disabledContainerColor = UISingleton.color2,
            disabledContentColor = UISingleton.textColor1
        ),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius),
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.round_insights_24),
                contentDescription = "Grade average",
                tint = UISingleton.textColor1,
                modifier = Modifier
                    .size(36.dp)
                    .padding(end = 6.dp)
            )
            Text(
                text = stringResource(R.string.grade_average),
                color = UISingleton.textColor1,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .defaultMinSize(minWidth = 48.dp)
                    .height(48.dp)
                    .background(UISingleton.color3, CircleShape)
                    .padding(horizontal = 6.dp)
            ) {
                Text(
                    text = if (gradeAverage?.isNaN() ?: true || gradeAverage == 0f) "—" else "%.2f".format(gradeAverage),
                    color = UISingleton.textColor4,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp.scaleIndependent,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}