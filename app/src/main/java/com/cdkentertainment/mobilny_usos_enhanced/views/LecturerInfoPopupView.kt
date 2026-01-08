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
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size.Companion.ORIGINAL
import com.cdkentertainment.mobilny_usos_enhanced.Lecturer
import com.cdkentertainment.mobilny_usos_enhanced.LecturerData
import com.cdkentertainment.mobilny_usos_enhanced.PeopleSingleton
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
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
    val extendedData: LecturerData? = PeopleSingleton.lecturers[data.human.id]?.lecturerData
    var lecturerDataFetched: Boolean by rememberSaveable { mutableStateOf(extendedData != null) }
    var lecturerDataFetchError: Boolean by rememberSaveable { mutableStateOf(false) }

    val loadLecturersInfo: () -> Unit = {
        lecturerDataFetched = false
        lecturerDataFetchError = false
        viewModel.getLecturersExtendedData(
            lecturerIds = listOf(data.human.id),
            onSuccess = {
                lecturerDataFetched = true
                lecturerDataFetchError = false
            },
            onError = {
                lecturerDataFetchError = true
                lecturerDataFetched = false
            }
        )
    }

    LaunchedEffect(Unit) {
        loadLecturersInfo()
    }

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
                                AnimatedVisibility(
                                    visible = extendedData != null,
                                    enter = UIHelper.scaleEnterTransition(1)
                                ) {
                                    val profilePicture: Painter? = if (extendedData != null) rememberAsyncImagePainter(
                                        ImageRequest.Builder(context)
                                            .data(extendedData.photo_urls["100x100"])
                                            .size(ORIGINAL)
                                            .crossfade(true)
                                            .placeholder(android.R.drawable.ic_menu_help)
                                            .error(android.R.drawable.ic_menu_help)
                                            .build()
                                    ) else null
                                    if (profilePicture != null) {
                                        Icon(
                                            painter = profilePicture,
                                            contentDescription = "Person",
                                            tint = androidx.compose.ui.graphics.Color.Unspecified,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .graphicsLayer(
                                                    scaleX = 0.85f,
                                                    scaleY = 0.85f
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    AnimatedVisibility(
                        visible = !lecturerDataFetched && !lecturerDataFetchError,
                        enter = UIHelper.slideEnterTransition(1)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth().padding(12.dp)
                        ) {
                            CircularProgressIndicator(color = UISingleton.textColor2)
                        }
                    }
                }
                item {
                    AnimatedVisibility(
                        visible = lecturerDataFetchError,
                        enter = UIHelper.slideEnterTransition(1)
                    ) {
                        TextAndIconCardView(
                            title = stringResource(R.string.failed_to_fetch),
                            icon = Icons.Rounded.Refresh,
                            iconSize = 40.dp,
                            iconPadding = 6.dp,
                            backgroundColor = UISingleton.color1,
                            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp)
                        ) {
                            loadLecturersInfo()
                        }
                    }
                }
                if (!data.lecturerData?.office_hours?.getLocalized(context).isNullOrEmpty()) {
                    item {
                        AnimatedVisibility(
                            visible = lecturerDataFetched,
                            enter = UIHelper.slideEnterTransition(1)
                        ) {
                            GroupedContentContainerView(
                                title = stringResource(R.string.office_hours),
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
                    }
                }
                if (data.lecturerData?.phone_numbers != null || data.lecturerData?.email != null || data.lecturerData?.room != null) {
                    item {
                        AnimatedVisibility(
                            visible = lecturerDataFetched,
                            enter = UIHelper.slideEnterTransition(2)
                        ) {
                            ContactInfoView(
                                phoneNumber = data.lecturerData?.phone_numbers?.joinToString(separator = "\n") ?: "",
                                email = data.lecturerData?.email ?: "",
                                address = if (data.lecturerData?.room != null && data.lecturerData?.room?.building_name != null) (data.lecturerData?.room?.building_name?.getLocalized(context) + ", ${stringResource(R.string.lecturer_room)} " + data.lecturerData?.room?.number) else ""
                            )
                        }
                    }
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
                            numberOfReviews = data.rating.ratesCount,
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
                                        if (userRating != null) {
                                            viewModel.updateUserRate(data.human.id, rate)
                                            editingRate = false
                                        } else {
                                            viewModel.addUserRate(lecturerId = data.human.id, rate)
                                            editingRate = false
                                        }
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