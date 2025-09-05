package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LessonGroupPageViewModel

@Composable
fun GroupParticipantCardView(
    index: Int,
    participant: SharedDataClasses.Human,
    viewModel: LessonGroupPageViewModel,
    groupKey: String,
    modifier: Modifier = Modifier
) {
    val shape: RoundedCornerShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = if (index == viewModel.groupDetails[groupKey]?.participants!!.participants.size - 1) UISingleton.uiElementsCornerRadius.dp else 0.dp,
        bottomEnd = if (index == viewModel.groupDetails[groupKey]?.participants!!.participants.size - 1) UISingleton.uiElementsCornerRadius.dp else 0.dp,
    )
    Card(
        colors = CardColors(
            contentColor = UISingleton.textColor1,
            containerColor = UISingleton.color2,
            disabledContainerColor = UISingleton.color2,
            disabledContentColor = UISingleton.textColor1
        ),
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .background(UISingleton.color1, shape)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .then(modifier)
    ) {
        Text(
            text = "${index + 1}. ${participant.first_name} ${participant.last_name}",
            style = MaterialTheme.typography.titleMedium,
            color = UISingleton.textColor1,
            modifier = Modifier
                .padding(12.dp)
        )
    }
}