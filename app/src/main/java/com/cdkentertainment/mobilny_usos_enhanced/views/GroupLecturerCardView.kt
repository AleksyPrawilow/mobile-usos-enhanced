package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerRate
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import com.cdkentertainment.mobilny_usos_enhanced.scaleIndependent
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LecturerRatesPageViewModel

@Composable
fun GroupLecturerCardView(
    lecturer: SharedDataClasses.Human,
) {
    val lecturerRatesPageViewModel: LecturerRatesPageViewModel = viewModel<LecturerRatesPageViewModel>()
    val lecturerRate: LecturerRate = lecturerRatesPageViewModel.lecturerRates.getOrDefault(lecturer.id, LecturerRate())
    val rateAverage: Float = (lecturerRate.rate_1 + lecturerRate.rate_2 + lecturerRate.rate_3 + lecturerRate.rate_4 + lecturerRate.rate_5) / 5f
    var showDetails: Boolean by rememberSaveable { mutableStateOf(false) }
    if (showDetails) {
        LecturerInfoPopupView(data = lecturer, onDismissRequest = { showDetails = false })
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            .background(UISingleton.color2)
            .clickable(onClick = {
                showDetails = true
            })
            .padding(12.dp)
    ) {
        Text(
            text = "${lecturer.first_name} ${lecturer.last_name}",
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