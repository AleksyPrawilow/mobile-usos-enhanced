package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.view_models.OAuthViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.ScreenManagerViewModel
import com.cdkentertainment.mobilny_usos_enhanced.view_models.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.LoginPageView(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    screenManagerViewModel: ScreenManagerViewModel = viewModel<ScreenManagerViewModel>()
) {
    val coroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    val oAuthViewModel: OAuthViewModel = viewModel<OAuthViewModel>()
    oAuthViewModel.context = context

    LaunchedEffect(Unit) {
        if (oAuthViewModel.tryAutoLogin()) {
            delay(1000)
            screenManagerViewModel.authorize()
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mobilny USOS Enhanced",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = UISingleton.color4.primaryColor
        )
        AnimatedVisibility(oAuthViewModel.isCheckingConnection) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sprawdzam połączenie...",
                    style = MaterialTheme.typography.titleLarge,
                    color = UISingleton.color3.primaryColor
                )
                CircularProgressIndicator(color = UISingleton.color3.primaryColor)
            }
        }
        AnimatedVisibility(oAuthViewModel.authorized) {
            Text(
                text = "Zalogowany",
                style = MaterialTheme.typography.titleLarge,
                color = UISingleton.color3.primaryColor
            )
        }
        AnimatedVisibility(!oAuthViewModel.authorized && !oAuthViewModel.isCheckingConnection) {
            Text(
                text = if (!oAuthViewModel.gotRequestToken) "Nie jesteś zalogowany" + oAuthViewModel.errorMessage else "Trwa logowanie",
                style = MaterialTheme.typography.titleLarge,
                color = UISingleton.color3.primaryColor
            )
        }
        AnimatedVisibility(!oAuthViewModel.gotRequestToken && !oAuthViewModel.isCheckingConnection && !oAuthViewModel.authorized) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    contentColor = UISingleton.color3.primaryColor,
                    containerColor = UISingleton.color2.primaryColor,
                ),
                onClick = {
                    coroutineScope.launch {
                        oAuthViewModel.authorize()
                    }
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Zaloguj się",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = "Icon",
                    )
                }
            }
        }
        AnimatedVisibility(oAuthViewModel.gotRequestToken && !oAuthViewModel.authorized && !oAuthViewModel.isCheckingConnection) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
            ) {
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = UISingleton.color3.primaryColor,
                        unfocusedTextColor = UISingleton.color3.primaryColor,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = UISingleton.color3.primaryColor,
                        focusedIndicatorColor = UISingleton.color2.primaryColor,
                        unfocusedIndicatorColor = UISingleton.color2.primaryColor
                    ),
                    value = oAuthViewModel.oauthVerifierPin,
                    leadingIcon = { Icon(imageVector = Icons.Rounded.Lock, contentDescription = "lockIcon", tint = UISingleton.color3.primaryColor) },
                    placeholder = { Text(text = "Wprowadź pin.") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        if (it.length <= 8 && it.all { char -> char.isDigit() }) {
                            oAuthViewModel.oauthVerifierPin = it
                        }
                    }
                )
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = UISingleton.color3.primaryColor,
                        containerColor = UISingleton.color2.primaryColor,
                    ),
                    onClick = {
                        if (oAuthViewModel.oauthVerifierPin.length == 8) {
                            coroutineScope.launch {
                                oAuthViewModel.getAccessToken(oAuthViewModel.oauthVerifierPin)
                                delay(1000)
                                screenManagerViewModel.authorize()
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = "Authorize"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    val currentScreen: Screens = Screens.LOGIN
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UISingleton.color1.primaryColor)
            .padding(12.dp)
    ) {
        SharedTransitionLayout {
            AnimatedContent(targetState = currentScreen) { target ->
                if (currentScreen == target) {
                    LoginPageView(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent
                    )
                }
            }
        }
    }
}