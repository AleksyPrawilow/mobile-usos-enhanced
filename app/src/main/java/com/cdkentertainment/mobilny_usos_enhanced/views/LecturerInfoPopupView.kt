package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerRate
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LecturerRatesPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LecturerInfoPopupView(
    data: SharedDataClasses.Human,
    onDismissRequest: () -> Unit
) {
    val context: Context = LocalContext.current
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val viewModel: LecturerRatesPageViewModel = viewModel<LecturerRatesPageViewModel>()
    val userRating: LecturerRate? = viewModel.userRatings[data.id]
    val lecturerRatings: LecturerRate? = viewModel.lecturerRates[data.id]
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
                    Spacer(modifier = Modifier.height(48.dp))
                }
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
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
                item {
                    Text(
                        text = "${data.first_name} ${data.last_name}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = UISingleton.textColor1,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {
                    GroupedContentContainerView(
                        title = "Koordynowane przedmioty",
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
                item {
                    ContactInfoView()
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
                            lecturerId = data.id,
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
                                lecturerId = data.id,
                                title = "Twoja ocena",
                                numberOfReviews = 0,
                                showNumberOfReviews = false,
                                rate = userRating ?: LecturerRate(),
                                onAddRate = { rate ->
                                    coroutineScope.launch {
                                        viewModel.addUserRate(lecturerId = data.id, rate)
                                        editingRate = false
                                    }
                                }
                            )
                        }
                        AnimatedVisibility(
                            visible = userRating != null && !editingRate,
                        ) {
                            LecturerRateView(
                                lecturerId = data.id,
                                title = "Twoja ocena",
                                numberOfReviews = 1,
                                showNumberOfReviews = false,
                                rate = userRating ?: LecturerRate(),
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