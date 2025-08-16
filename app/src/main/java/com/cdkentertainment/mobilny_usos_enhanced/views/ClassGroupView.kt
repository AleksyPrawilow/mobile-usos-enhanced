package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LessonGroupPageViewModel

@Composable
fun ClassGroupView(
    data: LessonGroup,
    viewModel: LessonGroupPageViewModel
) {
    val groupKey: String = "${data.course_unit_id}-${data.group_number}"
    val onClick: () -> Unit = {
        viewModel.groupDetails[groupKey]?.showDetails = true
    }
    val onDismissRequest: () -> Unit = {
        viewModel.groupDetails[groupKey]?.showDetails = false
    }
    if (viewModel.groupDetails[groupKey]?.showDetails == true) {
        ClassGroupPopupView(
            data = data,
            viewModel = viewModel,
            groupKey = groupKey,
            onDismissRequest = onDismissRequest
        )
    }

    Card(
        colors = CardColors(
            contentColor = UISingleton.color4.primaryColor,
            containerColor = UISingleton.color2.primaryColor,
            disabledContainerColor = UISingleton.color2.primaryColor,
            disabledContentColor = UISingleton.color4.primaryColor
        ),
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = data.course_name.pl,
                color = UISingleton.color4.primaryColor,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            HorizontalDivider(
                thickness = 5.dp,
                color = UISingleton.color3.primaryColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            )
            Text(
                text = "${data.class_type_id}, grupa ${data.group_number}",
                style = MaterialTheme.typography.titleMedium,
                color = UISingleton.color3.primaryColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}