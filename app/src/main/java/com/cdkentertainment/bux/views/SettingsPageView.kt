package com.cdkentertainment.bux.views

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.bux.BuildConfig
import com.cdkentertainment.bux.MainActivity
import com.cdkentertainment.bux.R
import com.cdkentertainment.bux.UIHelper
import com.cdkentertainment.bux.UISingleton
import com.cdkentertainment.bux.UserDataSingleton
import com.cdkentertainment.bux.view_models.SettingsPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SettingsPageView() {
    val settingsPageViewModel: SettingsPageViewModel = viewModel<SettingsPageViewModel>()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val enterTransition: (Int) -> EnterTransition = UIHelper.slideEnterTransition
    var showElements: Boolean by rememberSaveable { mutableStateOf(false) }
    var showLogOutConfirm: Boolean by rememberSaveable { mutableStateOf(false) }
    var showPrivacyPolicy: Boolean by rememberSaveable { mutableStateOf(false) }
    val context: Context = LocalContext.current
    val paddingModifier: Modifier = Modifier.padding(horizontal = UISingleton.horizontalPadding, vertical = 8.dp)
    val textStyle: TextStyle = MaterialTheme.typography.titleMedium
    val fontWeight: FontWeight = FontWeight.Bold
    val somethingWentWrongText: String = stringResource(R.string.something_went_wrong)

    LaunchedEffect(Unit) {
        delay(150)
        showElements = true
    }

    if (showLogOutConfirm) {
        ConfirmDialogPopupView(
            onDismiss = {
                showLogOutConfirm = false
            },
            onConfirm = {
                coroutineScope.launch {
                    try {
                        UserDataSingleton.deleteUserCredentials(context)
                        val activity = context as Activity
                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                        activity.finish()
                    } catch (e: Exception) {
                        val toast: Toast = Toast.makeText(context, somethingWentWrongText, Toast.LENGTH_SHORT)
                        toast.show()
                        showLogOutConfirm = false
                    }
                }
            },
            title = stringResource(R.string.log_out_confirm),
            confirmTitle = stringResource(R.string.yes),
            dismissTitle = stringResource(R.string.no)
        )
    }

    if (showPrivacyPolicy) {
        PrivacyPolicyPopupView(
            onDismiss = {
                showPrivacyPolicy = false
            }
        )
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        item {
            PageHeaderView(
                text = stringResource(R.string.settings_page),
                icon = ImageVector.vectorResource(R.drawable.rounded_settings_24)
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(1), modifier = paddingModifier) {
                Text(
                    text = stringResource(R.string.app_preferences),
                    color = UISingleton.textColor1,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = fontWeight
                )
            }
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(2)) {
                ThemeSelectionButtonView(paddingModifier)
            }
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(3)) {
                LanguageSelectionButtonView(paddingModifier)
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(4), modifier = paddingModifier) {
                Text(
                    text = stringResource(R.string.profile),
                    color = UISingleton.textColor1,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = fontWeight
                )
            }
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(5)) {
                TextAndIconCardView(
                    modifier = paddingModifier,
                    title = stringResource(R.string.log_out),
                    icon = ImageVector.vectorResource(R.drawable.rounded_door_open_24),
                    backgroundColor = UISingleton.color2,
                    showArrow = true,
                    elevation = 3.dp,
                    iconSize = 40.dp,
                    iconPadding = 6.dp,
                    fontWeight = fontWeight,
                    textStyle = textStyle
                ) {
                    showLogOutConfirm = true
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(6), modifier = paddingModifier) {
                Text(
                    text = stringResource(R.string.support_n_feedback),
                    color = UISingleton.textColor1,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = fontWeight
                )
            }
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(7)) {
                TextAndIconCardView(
                    modifier = paddingModifier,
                    title = stringResource(R.string.bug_report),
                    icon = ImageVector.vectorResource(R.drawable.rounded_bug_report_24),
                    backgroundColor = UISingleton.color2,
                    showArrow = true,
                    elevation = 3.dp,
                    iconSize = 40.dp,
                    iconPadding = 6.dp,
                    fontWeight = fontWeight,
                    textStyle = textStyle
                ) {
                    val subject = Uri.encode("Bug report")
                    val uri = "mailto:mobile.usos.enhanced.app@gmail.com?subject=$subject".toUri()
                    val intent = Intent(Intent.ACTION_SENDTO, uri)
                    try {
                        context.startActivity(Intent.createChooser(intent, "Choose an email client"))
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(context, "No email client installed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(8)) {
                TextAndIconCardView(
                    modifier = paddingModifier,
                    title = stringResource(R.string.leave_a_review),
                    icon = ImageVector.vectorResource(R.drawable.rounded_social_leaderboard_24),
                    backgroundColor = UISingleton.color2,
                    showArrow = true,
                    elevation = 3.dp,
                    iconSize = 40.dp,
                    iconPadding = 6.dp,
                    fontWeight = fontWeight,
                    textStyle = textStyle
                ) {
                    val packageName = "com.cdkentertainment.mobilny_usos_enhanced"
                    try {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                "market://details?id=$packageName".toUri()
                            ).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                        )
                    } catch (e: android.content.ActivityNotFoundException) {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                "https://play.google.com/store/apps/details?id=$packageName".toUri()
                            ).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                        )
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(9), modifier = paddingModifier) {
                Text(
                    text = stringResource(R.string.about_app),
                    color = UISingleton.textColor1,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = fontWeight
                )
            }
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(10)) {
                GradeCardView(
                    modifier = paddingModifier,
                    courseName = stringResource(R.string.app_version),
                    grade = BuildConfig.VERSION_NAME,
                    backgroundColor = UISingleton.color2,
                    showArrow = false,
                    elevation = 3.dp,
                    fontWeight = fontWeight,
                    textStyle = textStyle
                )
            }
        }
        item {
            AnimatedVisibility(showElements, enter = enterTransition(11)) {
                TextAndIconCardView(
                    modifier = paddingModifier,
                    title = stringResource(R.string.privacy_policy),
                    icon = ImageVector.vectorResource(R.drawable.rounded_privacy_tip_24),
                    backgroundColor = UISingleton.color2,
                    showArrow = true,
                    elevation = 3.dp,
                    iconSize = 40.dp,
                    iconPadding = 6.dp,
                    fontWeight = fontWeight,
                    textStyle = textStyle
                ) {
                    showPrivacyPolicy = true
                }
            }
        }
        item {
            Spacer(
                modifier = Modifier.height(88.dp)
            )
        }
    }
}