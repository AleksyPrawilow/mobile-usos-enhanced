package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Lesson
import com.cdkentertainment.mobilny_usos_enhanced.view_models.SchedulePageViewModel

@Composable
fun ActivityView(
    schedulePageViewModel: SchedulePageViewModel,
    activity: Lesson,
    modifier: Modifier = Modifier
) {
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
            .then(modifier)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "${schedulePageViewModel.getTimeFromDate(activity.start_time)} - ${schedulePageViewModel.getTimeFromDate(activity.end_time)}",
                    color = UISingleton.textColor1,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .weight(1f)
                )
                Column {
                    Text(
                        text = activity.classtype_id,
                        color = UISingleton.textColor2,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .align(Alignment.End)
                    )
                    Text(
                        text = "Sala: ${activity.room_number}",
                        color = UISingleton.textColor2,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .align(Alignment.End)
                    )
                }
            }
            HorizontalDivider(
                thickness = 5.dp,
                color = UISingleton.textColor2,
                modifier = Modifier
                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            )
            Text(
                text = activity.course_name.pl,
                color = UISingleton.textColor1,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}