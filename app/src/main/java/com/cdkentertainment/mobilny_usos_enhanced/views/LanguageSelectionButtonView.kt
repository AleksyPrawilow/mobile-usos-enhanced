package com.cdkentertainment.mobilny_usos_enhanced.views

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton.color2
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton.color3
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton.textColor1
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton.textColor4
import com.cdkentertainment.mobilny_usos_enhanced.view_models.SettingsPageViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun LanguageSelectionButtonView(modifier: Modifier) {
    val settingsPageViewModel: SettingsPageViewModel = viewModel<SettingsPageViewModel>()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val shape: RoundedCornerShape = remember { RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp) }
    var expanded by remember { mutableStateOf(false) }
    val context: Context = LocalContext.current
    val languages: List<Pair<String, String>> = listOf(
        Pair("en", "English"),
        Pair("pl", "Polski")
    )
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color2,
            disabledContainerColor = color2,
            contentColor = textColor1,
            disabledContentColor = textColor1
        ),
        shape = shape,
        elevation = CardDefaults.cardElevation(3.dp),
        onClick = { expanded = !expanded },
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(R.string.app_language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor1,
                modifier = Modifier
                    .weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = "More",
                tint = UISingleton.textColor1,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color3, CircleShape)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.rounded_language_24),
                    contentDescription = "Language",
                    tint = textColor4,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                        .align(Alignment.Center)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor = color2,
                    shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
                ) {
                    for (language in languages) {
                        DropdownMenuItem(
                            contentPadding = PaddingValues(12.dp),
                            text = {
                                Text(
                                    text = language.second,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = textColor1
                                )
                            },
                            onClick = {
                                expanded = false
                                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language.first)
                                (context as? Activity)?.apply {
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                                }
                                AppCompatDelegate.setApplicationLocales(appLocale)

                            }
                        )
                    }
                }
            }
        }
    }
}