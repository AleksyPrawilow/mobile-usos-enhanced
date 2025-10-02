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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size.Companion.ORIGINAL
import com.cdkentertainment.mobilny_usos_enhanced.PeopleSingleton
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.StudentData
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LessonGroupPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun StudentInfoPopupView(
    data: SharedDataClasses.Human,
    onDismissRequest: () -> Unit
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    var fetchError: Boolean = false
    val classGroupsViewModel: LessonGroupPageViewModel = viewModel<LessonGroupPageViewModel>()
    val extendedData: StudentData? = PeopleSingleton.students[data.id]?.studentData
    LaunchedEffect(Unit) {
        fetchError = false
        if (extendedData == null) {
            fetchError = !classGroupsViewModel.fetchUserInfo(data)
        } else {
            fetchError = false
        }
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
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
                        title = "${data.first_name} ${data.last_name}"
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
                                    extendedData != null
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
                                    if (profilePicture != null && extendedData != null && extendedData.has_photo) {
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
                                    } else {
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
                                AnimatedVisibility(
                                    visible = extendedData == null,
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize()
                                    ){
                                        CircularProgressIndicator(
                                            color = UISingleton.textColor1,
                                            modifier = Modifier.size(24.dp)
                                                .align(Alignment.Center)
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
                if (extendedData != null) {
                    item {
                        ContactInfoView(
                            phoneNumber = if (extendedData.mobile_numbers.isNotEmpty()) extendedData.mobile_numbers.joinToString("\n") else "",
                            email = extendedData.email ?: "",
                            address = ""
                        )
                    }
                }
                if (fetchError) {
                    item {
                        TextAndIconCardView(
                            title = stringResource(R.string.failed_to_fetch),
                            showArrow = true,
                        ) {
                            coroutineScope.launch {
                                fetchError = false
                                if (extendedData == null) {
                                    fetchError = !classGroupsViewModel.fetchUserInfo(data)
                                } else {
                                    fetchError = false
                                }
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            DismissPopupButtonView(onDismissRequest = onDismissRequest, modifier = Modifier.align(Alignment.TopEnd))
        }
    }
}