package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton.color1
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton.color2
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton.color3
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton.color4
import com.cdkentertainment.mobilny_usos_enhanced.view_models.SettingsPageViewModel

@Composable
fun ThemeSelectionButtonView() {
    val settingsPageViewModel: SettingsPageViewModel = viewModel<SettingsPageViewModel>()
    var expanded by remember { mutableStateOf(false) }
    val themeBrush = Brush.horizontalGradient(
        0f to color1,
        0.25f to color1,
        0.25f to color2,
        0.5f to color2,
        0.5f to color3,
        0.75f to color3,
        0.75f to color4,
        1f to color4
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
            .background(color2)
            .clickable(onClick = { expanded = !expanded })
            .padding(12.dp)
    ) {
        Text(
            text = "Motyw",
            style = MaterialTheme.typography.titleMedium,
            color = UISingleton.textColor1,
            modifier = Modifier
                .weight(1f)
        )
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(themeBrush, CircleShape)
                .border(5.dp, color3, CircleShape)
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = color2,
                shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
            ) {
                for ((themeName, theme) in UISingleton.themes) {
                    val previewBrush = Brush.horizontalGradient(
                        0f to theme.color1,
                        0.25f to theme.color1,
                        0.25f to theme.color2,
                        0.5f to theme.color2,
                        0.5f to theme.color3,
                        0.75f to theme.color3,
                        0.75f to theme.color4,
                        1f to theme.color4
                    )
                    DropdownMenuItem(
                        contentPadding = PaddingValues(12.dp),
                        text = {
                            Text(
                                text = themeName,
                                style = MaterialTheme.typography.titleMedium,
                                color = UISingleton.textColor1
                            )
                        },
                        trailingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(previewBrush, CircleShape)
                                    .border(3.dp, color3, CircleShape)
                            )
                        },
                        onClick = {
                            expanded = false
                            settingsPageViewModel.chooseTheme(theme)
                        }
                    )
                }
            }
        }
    }
}