package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton

@Composable
fun ContactInfoView(
    phoneNumber: String = "",
    email: String = "",
    address: String = ""
) {
    val context = LocalContext.current
    GroupedContentContainerView(
        title = stringResource(R.string.contact_information),
        backgroundColor = UISingleton.color1,
        modifier = Modifier.padding(horizontal = 12.dp)
    ) {
        val categories: Map<String, Pair<ImageVector, String>> = mapOf(
            "Telefon" to (ImageVector.vectorResource(R.drawable.rounded_call_24) to phoneNumber),
            "Email" to (ImageVector.vectorResource(R.drawable.rounded_mail_24) to email),
            "Adres" to (ImageVector.vectorResource(R.drawable.rounded_location_on_24) to address)
        )
        for ((category, iconAndText) in categories) {
            if (iconAndText.second.isEmpty()) continue
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(UISingleton.color2, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                    .clickable(
                        enabled = category != "Adres",
                        onClick = {
                            if (category == "Telefon") {
                                val phoneNumber = Uri.encode(iconAndText.second)
                                val uri = "tel:$phoneNumber".toUri()
                                val intent = Intent(Intent.ACTION_DIAL, uri)
                                try {
                                    context.startActivity(intent)
                                } catch (e: ActivityNotFoundException) {
                                    Toast.makeText(context, "No phone app installed", Toast.LENGTH_SHORT).show()
                                }
                            } else if (category == "Email") {
                                val uri = "mailto:${iconAndText.second}".toUri()
                                val intent = Intent(Intent.ACTION_SENDTO, uri)
                                try {
                                    context.startActivity(Intent.createChooser(intent, "Choose an email client"))
                                } catch (e: ActivityNotFoundException) {
                                    Toast.makeText(
                                        context,
                                        "No email client installed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    )
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = iconAndText.first,
                    contentDescription = category,
                    tint = UISingleton.textColor4,
                    modifier = Modifier
                        .size(48.dp)
                        .background(UISingleton.color3, CircleShape)
                        .padding(12.dp)
                )
                Text(
                    text = iconAndText.second,
                    style = MaterialTheme.typography.titleMedium,
                    color = UISingleton.textColor1
                )
            }
        }
    }
}