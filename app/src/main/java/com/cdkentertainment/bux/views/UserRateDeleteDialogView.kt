package com.cdkentertainment.bux.views

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.bux.R
import com.cdkentertainment.bux.UISingleton
import com.cdkentertainment.bux.getLocalized
import com.cdkentertainment.bux.models.SharedDataClasses
import com.cdkentertainment.bux.view_models.LecturerRatesPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun UserRateDeleteDialogView(
    lecturerId: String,
    onDismiss: () -> Unit
) {
    val context: Context = LocalContext.current
    val errorMessage: SharedDataClasses.LangDict = SharedDataClasses.LangDict(
        pl = "Nie udało się usunąć ocenę.",
        en = "Failed to delete the rating."
    )
    val deletingMessage: SharedDataClasses.LangDict = SharedDataClasses.LangDict(
        pl = "Usuwam ocenę...",
        en = "Deleting the rating..."
    )
    val toast: Toast = Toast.makeText(context, errorMessage.getLocalized(context), Toast.LENGTH_LONG)
    val viewModel: LecturerRatesPageViewModel = viewModel<LecturerRatesPageViewModel>()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
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
                        text = stringResource(R.string.delete_rating_confirm),
                        style = MaterialTheme.typography.headlineSmall,
                        color = UISingleton.textColor1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                AnimatedVisibility(visible = savingDeletion && !deletionFinished) {
                    Text(
                        text = deletingMessage.getLocalized(context),
                        style = MaterialTheme.typography.headlineSmall,
                        color = UISingleton.textColor1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                AnimatedVisibility(visible = deletionFinished && deletionFailed) {
                    Text(
                        text = errorMessage.getLocalized(context),
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
                            text = stringResource(R.string.yes),
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
                                            toast.show()
                                            onDismiss()
                                        }
                                    }
                                },
                                    enabled = !savingDeletion
                                )
                                .padding(12.dp)
                        )
                        Text(
                            text = stringResource(R.string.no),
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