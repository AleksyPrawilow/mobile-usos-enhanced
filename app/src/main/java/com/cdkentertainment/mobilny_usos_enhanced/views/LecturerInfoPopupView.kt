package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses

@Composable
fun LecturerInfoPopupView(
    data: SharedDataClasses.Human,
    onDismissRequest: () -> Unit
) {
    val context: Context = LocalContext.current

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .shadow(10.dp, shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .background(UISingleton.color2.primaryColor, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    Spacer(modifier = Modifier.height(48.dp))
                }
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(UISingleton.color2.primaryColor)
                                .border(5.dp, UISingleton.color1.primaryColor, shape = RoundedCornerShape(50.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = "Person",
                                tint = UISingleton.color4.primaryColor,
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
                item {
                    Text(
                        text = "${data.first_name} ${data.last_name}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = UISingleton.color4.primaryColor,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .background(
                                UISingleton.color1.primaryColor,
                                RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Koordynowane przedmioty",
                                style = MaterialTheme.typography.titleLarge,
                                color = UISingleton.color4.primaryColor
                            )
                            HorizontalDivider(
                                thickness = 5.dp,
                                color = UISingleton.color3.primaryColor,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                            )
                            repeat(2) { index ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            UISingleton.color2.primaryColor,
                                            RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp)
                                        )
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "${index + 1}. Matematyka",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = UISingleton.color4.primaryColor
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                            .background(UISingleton.color1.primaryColor, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                            .padding(12.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Kontakt",
                                style = MaterialTheme.typography.titleLarge,
                                color = UISingleton.color4.primaryColor
                            )
                            HorizontalDivider(
                                thickness = 5.dp,
                                color = UISingleton.color3.primaryColor,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                            )
                            val categories: Map<String, Pair<ImageVector, String>> = mapOf(
                                "Telefon" to (Icons.Rounded.Call to "+48 xxx-xxx-xxx"),
                                "Email" to (Icons.Rounded.Email to "example@amu.edu.pl"),
                                "Adres" to (Icons.Rounded.LocationOn to "Uniwersytetu Poznańskiego 4, 61-614 Poznań, pokój B1-420")
                            )
                            for ((category, iconAndText) in categories) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(UISingleton.color2.primaryColor, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                                        .padding(12.dp)
                                ) {
                                    Icon(
                                        imageVector = iconAndText.first,
                                        contentDescription = category,
                                        tint = UISingleton.color1.primaryColor,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(UISingleton.color3.primaryColor, CircleShape)
                                            .padding(12.dp)
                                    )
                                    Text(
                                        text = iconAndText.second,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = UISingleton.color4.primaryColor
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .background(UISingleton.color1.primaryColor, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                            .padding(12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Ogólna ocena",
                                style = MaterialTheme.typography.titleLarge,
                                color = UISingleton.color4.primaryColor
                            )
                            Text(
                                text = "4.5",
                                fontSize = 72.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = UISingleton.color4.primaryColor
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                            ) {
                                val fractionFilled: Float = 0.5f
                                val starBrush = Brush.horizontalGradient(
                                    0f to UISingleton.color3.primaryColor,
                                    fractionFilled to UISingleton.color3.primaryColor,
                                    fractionFilled to UISingleton.color2.primaryColor,
                                    1f to UISingleton.color2.primaryColor
                                )
                                repeat(5) { index ->
                                    val starModifier: Modifier = if (index < 4) {
                                        Modifier.size(32.dp)
                                    } else {
                                        Modifier
                                            .size(32.dp)
                                            .graphicsLayer {
                                                compositingStrategy = CompositingStrategy.Offscreen
                                            }
                                            .drawWithContent {
                                                drawContent()
                                                drawRect(starBrush, size = this.size, blendMode = BlendMode.SrcAtop)
                                            }
                                    }
                                    Icon(
                                        imageVector = Icons.Rounded.Star,
                                        contentDescription = "Star",
                                        tint = if (index < 4) UISingleton.color3.primaryColor else Color.Unspecified,
                                        modifier = starModifier
                                    )
                                }
                            }
                            Text(
                                text = "Na podstawie 23 ocen",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Light,
                                color = UISingleton.color4.primaryColor
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            val categories: Map<String, Float> = mapOf(
                                "Jasność" to 4f,
                                "Organizacja" to 4.5f,
                                "Zaangażowanie" to 5f,
                                "Komunikacja" to 4.9f,
                                "Obiektywność" to 4.2f
                            )

                            for ((category, rating) in categories) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                ) {
                                    Text(
                                        text = category,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = UISingleton.color4.primaryColor,
                                        modifier = Modifier.weight(0.5f)
                                    )
                                    LinearProgressIndicator(
                                        progress = { rating / 5f },
                                        color = UISingleton.color3.primaryColor,
                                        trackColor = UISingleton.color2.primaryColor,
                                        drawStopIndicator = {},
                                        modifier = Modifier
                                            .weight(0.5f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            DismissPopupButtonView(onDismissRequest = onDismissRequest, modifier = Modifier.align(Alignment.TopEnd))
        }
    }
}