package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup

@Composable
fun ClassGroupCardView(
    data: LessonGroup,
    onClick: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            .clickable(onClick = onClick)
    ) {
        Text(
            text = data.course_name.pl,
            color = UISingleton.color4.primaryColor,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    UISingleton.color2.primaryColor,
                    RoundedCornerShape(
                        topStart = UISingleton.uiElementsCornerRadius.dp,
                        topEnd = UISingleton.uiElementsCornerRadius.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
                .padding(12.dp)
        )
        Text(
            text = "${data.class_type_id}, grupa ${data.group_number}",
            style = MaterialTheme.typography.titleMedium,
            color = UISingleton.color1.primaryColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    UISingleton.color3.primaryColor,
                    RoundedCornerShape(
                        bottomStart = UISingleton.uiElementsCornerRadius.dp,
                        bottomEnd = UISingleton.uiElementsCornerRadius.dp,
                        topStart = 0.dp,
                        topEnd = 0.dp
                    )
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }

//    Card(
//        colors = CardColors(
//            contentColor = UISingleton.color4.primaryColor,
//            containerColor = UISingleton.color2.primaryColor,
//            disabledContainerColor = UISingleton.color2.primaryColor,
//            disabledContentColor = UISingleton.color4.primaryColor
//        ),
//        onClick = onClick,
//        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
//        modifier = Modifier
//            .fillMaxWidth()
//    ) {
//        Column(
//            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
//            modifier = Modifier
//                .padding(12.dp)
//                .fillMaxWidth()
//        ) {
//            Text(
//                text = data.course_name.pl,
//                color = UISingleton.color4.primaryColor,
//                style = MaterialTheme.typography.titleLarge,
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis
//            )
//            HorizontalDivider(
//                thickness = 5.dp,
//                color = UISingleton.color3.primaryColor,
//                modifier = Modifier
//                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
//            )
//            Text(
//                text = "${data.class_type_id}, grupa ${data.group_number}",
//                style = MaterialTheme.typography.titleMedium,
//                color = UISingleton.color3.primaryColor,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//        }
//    }
}