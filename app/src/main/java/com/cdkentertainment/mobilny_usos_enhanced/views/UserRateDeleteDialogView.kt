package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LecturerRatesPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UserRateDeleteDialogView(
    lecturerId: String,
    onDismiss: () -> Unit
) {
    val viewModel: LecturerRatesPageViewModel = viewModel<LecturerRatesPageViewModel>()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val userSex: String = OAuthSingleton.userData?.basicInfo?.sex ?: "M"
    var savingDeletion: Boolean by rememberSaveable { mutableStateOf(false) }
    var deletionFailed: Boolean by rememberSaveable { mutableStateOf(false) }
    var deletionFinished: Boolean by rememberSaveable { mutableStateOf(false) }
    Dialog(
        onDismissRequest = { }
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .shadow(
                    10.dp,
                    shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
                )
                .background(
                    UISingleton.color2,
                    RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
                )
                .border(
                    5.dp,
                    UISingleton.color1,
                    RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(12.dp)
            ) {
                AnimatedVisibility(visible = !savingDeletion) {
                    Text(
                        text = "Czy jesteś ${if (userSex == "M") "pewien" else "pewna"} że chcesz usunąc swoją ocenę?",
                        style = MaterialTheme.typography.headlineSmall,
                        color = UISingleton.textColor1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                AnimatedVisibility(visible = savingDeletion && !deletionFinished) {
                    Text(
                        text = "Usuwam ocenę...",
                        style = MaterialTheme.typography.headlineSmall,
                        color = UISingleton.textColor1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                AnimatedVisibility(visible = deletionFinished && deletionFailed) {
                    Text(
                        text = "Nie udało się usunąć ocenę.",
                        style = MaterialTheme.typography.headlineSmall,
                        color = UISingleton.textColor1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                AnimatedVisibility(visible = savingDeletion && !deletionFinished) {
                    CircularProgressIndicator(color = UISingleton.textColor2)
                }
                AnimatedVisibility(visible = !savingDeletion) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Tak",
                            style = MaterialTheme.typography.titleLarge,
                            color = UISingleton.textColor4,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                                .background(UISingleton.color3)
                                .clickable(onClick = {
                                    savingDeletion = true
                                    coroutineScope.launch {
                                        val deleted = viewModel.deleteUserRate(lecturerId = lecturerId)
                                        deletionFinished = true
                                        if (deleted) {
                                            onDismiss()
                                        } else {
                                            deletionFailed = true
                                            delay(1000)
                                            onDismiss()
                                        }
                                    }
                                },
                                    enabled = !savingDeletion
                                )
                                .padding(12.dp)
                        )
                        Text(
                            text = "Nie",
                            style = MaterialTheme.typography.titleLarge,
                            color = UISingleton.textColor1,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                                .background(UISingleton.color1)
                                .clickable(onClick = onDismiss, enabled = !savingDeletion)
                                .padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}