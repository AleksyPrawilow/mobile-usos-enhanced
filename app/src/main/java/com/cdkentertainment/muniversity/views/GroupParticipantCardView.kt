package com.cdkentertainment.muniversity.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cdkentertainment.muniversity.UISingleton
import com.cdkentertainment.muniversity.models.SharedDataClasses

@Composable
fun GroupParticipantCardView(
    index: Int,
    participant: SharedDataClasses.Human,
    participantsSize: Int,
    modifier: Modifier = Modifier,
) {
    var showDetails: Boolean by rememberSaveable { mutableStateOf(false) }
    if (showDetails) {
        StudentInfoPopupView(data = participant, onDismissRequest = { showDetails = false })
    }
    val shape: RoundedCornerShape = remember {
        RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = if (index == participantsSize - 1) UISingleton.uiElementsCornerRadius.dp else 0.dp,
            bottomEnd = if (index == participantsSize - 1) UISingleton.uiElementsCornerRadius.dp else 0.dp,
        )
    }
    Card(
        colors = CardColors(
            contentColor = UISingleton.textColor1,
            containerColor = UISingleton.color2,
            disabledContainerColor = UISingleton.color2,
            disabledContentColor = UISingleton.textColor1
        ),
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
        onClick = {
            showDetails = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .shadow(3.dp, shape)
            .background(UISingleton.color1, shape)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .then(modifier)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "${index + 1}. ${participant.first_name} ${participant.last_name}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = UISingleton.textColor1,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = "More",
                tint = UISingleton.textColor1,
                modifier = Modifier
            )
        }
    }
}