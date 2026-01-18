package com.cdkentertainment.muniversity.views

import android.graphics.Color.TRANSPARENT
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cdkentertainment.muniversity.UISingleton

@Composable
fun SwitchSettingView(
    color1: Color,
    color2: Color,
    color3: Color,
    color4: Color,
    text: String = "Switch",
    checked: Boolean,
    onSwitchChange: ((Boolean) -> Unit)?
) {
    Card(
        colors = CardColors(
            contentColor = color4,
            containerColor = color3,
            disabledContainerColor = color1,
            disabledContentColor = color3
        ),
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(5.dp, color4, RoundedCornerShape(UISingleton.uiElementsCornerRadius))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = text,
                color = UISingleton.textColor1,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .weight(1f)
            )
            Switch(
                checked = checked,
                onCheckedChange = onSwitchChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = color3,
                    checkedTrackColor = color2,
                    checkedBorderColor = Color(TRANSPARENT),
                    uncheckedThumbColor = color3,
                    uncheckedTrackColor = color2,
                    uncheckedBorderColor = Color(TRANSPARENT)
                ),
                thumbContent = if (checked) {
                    {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null,
                            tint = color2,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                }
            )
        }
    }
}