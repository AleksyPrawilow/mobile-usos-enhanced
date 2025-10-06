package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.Lecturer
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerRate
import com.cdkentertainment.mobilny_usos_enhanced.models.UserRate
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LecturerRatesPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LecturerInfoPopupView(
    data: Lecturer,
    onDismissRequest: () -> Unit
) {
    val context: Context = LocalContext.current
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val viewModel: LecturerRatesPageViewModel = viewModel<LecturerRatesPageViewModel>()
    val userRating: UserRate? = viewModel.userRatings[data.human.id]
    val lecturerRatings: LecturerRate? = LecturerRate(
        data.rating.avgRate1,
        data.rating.avgRate2,
        data.rating.avgRate3,
        data.rating.avgRate4,
        data.rating.avgRate5
    )
    var editingRate: Boolean by rememberSaveable { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .shadow(10.dp, shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .background(UISingleton.color2, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .border(5.dp, UISingleton.color1, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    PopupHeaderView(
                        title = "${data.lecturerData?.titles?.get("before") ?: ""} ${data.human.first_name} ${data.human.last_name} ${data.lecturerData?.titles?.get("after") ?: ""}"
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .shadow(3.dp, CircleShape)
                                    .background(UISingleton.color2)
                                    .border(5.dp, UISingleton.color1, shape = RoundedCornerShape(50.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = "Person",
                                    tint = UISingleton.textColor1,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer(
                                            scaleX = 0.75f,
                                            scaleY = 0.75f
                                        )
                                )
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    GroupedContentContainerView(
                        title = stringResource(R.string.coordinated_courses),
                        backgroundColor = UISingleton.color1,
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                    ) {
                        repeat(2) { index ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        UISingleton.color2,
                                        RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
                                    )
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "${index + 1}. Matematyka",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = UISingleton.textColor1
                                )
                            }
                        }
                    }
                }
                if (!data.lecturerData?.office_hours?.getLocalized(context).isNullOrEmpty())
                item {
                    GroupedContentContainerView(
                        title = "Dyżur",
                        backgroundColor = UISingleton.color1,
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(UISingleton.color2, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                                .padding(12.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.rounded_alarm_24),
                                contentDescription = "office_hours",
                                tint = UISingleton.textColor4,
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(UISingleton.color3, CircleShape)
                                    .padding(12.dp)
                            )
                            Text(
                                text = data.lecturerData?.office_hours?.getLocalized(context) ?: "",
                                style = MaterialTheme.typography.titleMedium,
                                color = UISingleton.textColor1
                            )
                        }
                    }
                }
                item {
                    ContactInfoView(
                        phoneNumber = data.lecturerData?.phone_numbers?.joinToString(separator = "\n") ?: "",
                        email = data.lecturerData?.email ?: "",
                        address = if (data.lecturerData?.room != null && data.lecturerData?.room?.building_name != null) (data.lecturerData?.room?.building_name?.getLocalized(context) + ", ${stringResource(R.string.lecturer_room)} " + data.lecturerData?.room?.number) else ""
                    )
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .shadow(3.dp, shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                            .background(UISingleton.color1, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                            .padding(12.dp)
                    ) {
                        LecturerRateView(
                            lecturerId = data.human.id,
                            numberOfReviews = if (lecturerRatings != null) 1 else 0,
                            rate = if (lecturerRatings != null) lecturerRatings else LecturerRate()
                        )
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                            .shadow(3.dp, shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                            .background(UISingleton.color1, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                            .padding(12.dp)
                    ) {
                        AnimatedVisibility(
                            visible = userRating == null || editingRate
                        ) {
                            LecturerRateView(
                                lecturerId = data.human.id,
                                title = stringResource(R.string.your_rating),
                                numberOfReviews = 0,
                                showNumberOfReviews = false,
                                rate = if (userRating != null) LecturerRate(userRating.rate1.toFloat(), userRating.rate2.toFloat(), userRating.rate3.toFloat(), userRating.rate4.toFloat(), userRating.rate5.toFloat()) else LecturerRate(),
                                onAddRate = { rate ->
                                    coroutineScope.launch {
                                        viewModel.addUserRate(lecturerId = data.human.id, rate)
                                        editingRate = false
                                    }
                                }
                            )
                        }
                        AnimatedVisibility(
                            visible = userRating != null && !editingRate,
                        ) {
                            LecturerRateView(
                                lecturerId = data.human.id,
                                title = stringResource(R.string.your_rating),
                                numberOfReviews = 1,
                                showNumberOfReviews = false,
                                rate = if (userRating != null) LecturerRate(userRating.rate1.toFloat(), userRating.rate2.toFloat(), userRating.rate3.toFloat(), userRating.rate4.toFloat(), userRating.rate5.toFloat()) else LecturerRate(),
                                onEditRate = {
                                    editingRate = true
                                }
                            )
                        }
                    }
                }
            }
            DismissPopupButtonView(onDismissRequest = onDismissRequest, modifier = Modifier.align(Alignment.TopEnd))
        }
    }
}