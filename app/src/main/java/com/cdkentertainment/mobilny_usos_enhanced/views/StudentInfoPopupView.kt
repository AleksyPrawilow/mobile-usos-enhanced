package com.cdkentertainment.mobilny_usos_enhanced.views

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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import kotlinx.coroutines.CoroutineScope

@Composable
fun StudentInfoPopupView(
    data: SharedDataClasses.Human,
    onDismissRequest: () -> Unit
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

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
                    ContactInfoView()
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            DismissPopupButtonView(onDismissRequest = onDismissRequest, modifier = Modifier.align(Alignment.TopEnd))
        }
    }
}