package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.Lecturer
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerAvgRates
import com.cdkentertainment.mobilny_usos_enhanced.scaleIndependent
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LecturerRatesPageViewModel

@Composable
fun GroupLecturerCardView(
    lecturer: Lecturer,
    modifier: Modifier = Modifier,
    prefix: String = "",
    elevation: Dp = 0.dp,
    backgroundColor: Color = UISingleton.color2
) {
    val lecturerRatesPageViewModel: LecturerRatesPageViewModel = viewModel<LecturerRatesPageViewModel>()
    val lecturerRate: LecturerAvgRates = lecturer.rating
    val rateAverage: Float = (lecturerRate.avgRate1 + lecturerRate.avgRate2 + lecturerRate.avgRate3 + lecturerRate.avgRate4 + lecturerRate.avgRate5) / 5f
    var showDetails: Boolean by rememberSaveable { mutableStateOf(false) }
    if (showDetails) {
        LecturerInfoPopupView(data = lecturer, onDismissRequest = { showDetails = false })
    }
    val shape: RoundedCornerShape = remember { RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp) }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor,
            contentColor = UISingleton.textColor1,
            disabledContentColor = UISingleton.textColor1
        ),
        elevation = CardDefaults.cardElevation(elevation, disabledElevation = elevation),
        onClick = { showDetails = true },
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "$prefix${lecturer.human.first_name} ${lecturer.human.last_name}",
                color = UISingleton.textColor1,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = "More",
                tint = UISingleton.textColor1,
                modifier = Modifier
                    .padding(4.dp)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .defaultMinSize(minWidth = 48.dp)
                    .height(48.dp)
                    .background(UISingleton.color3, RoundedCornerShape(50.dp))
                    .padding(horizontal = 6.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(0.dp, alignment = Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "%.1f".format(rateAverage),
                        color = UISingleton.textColor4,
                        fontSize = 14.sp.scaleIndependent,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                    )
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Rate",
                        tint = UISingleton.textColor4,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}