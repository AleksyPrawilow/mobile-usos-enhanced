package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size.Companion.ORIGINAL
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.UserDataSingleton

@Composable
fun UserDataView(modifier: Modifier = Modifier) {
    val paddingSize: Int = 12
    val context: Context = LocalContext.current
    val profilePicture: Painter? = if (UserDataSingleton.userData != null) rememberAsyncImagePainter(
        ImageRequest.Builder(context)
            .data(UserDataSingleton.userData?.photo_urls["100x100"])
            .size(ORIGINAL)
            .crossfade(true)
            .placeholder(android.R.drawable.ic_menu_help)
            .error(android.R.drawable.ic_menu_help)
            .build()
    ) else null
    val shape: RoundedCornerShape =
        remember { RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp) }
    Card(
        colors = CardColors(
            contentColor = UISingleton.textColor1,
            containerColor = UISingleton.color2,
            disabledContainerColor = UISingleton.color2,
            disabledContentColor = UISingleton.textColor1
        ),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = shape,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(paddingSize.dp)
        ) {
            Card(
                colors = CardColors(
                    contentColor = UISingleton.textColor2,
                    containerColor = UISingleton.color1,
                    disabledContainerColor = UISingleton.color1,
                    disabledContentColor = UISingleton.textColor2
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .size(72.dp)
                    .border(5.dp, UISingleton.color1, shape = RoundedCornerShape(50.dp))
            ) {
                if (profilePicture != null) {
                    Icon(
                        painter = profilePicture,
                        contentDescription = "Person",
                        tint = androidx.compose.ui.graphics.Color.Unspecified,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = 0.85f,
                                scaleY = 0.85f
                            )
                    )
                } else {
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
            } // User photo card
            Column {
                Text(
                    text = "${UserDataSingleton.userData?.first_name} ${UserDataSingleton.userData?.last_name}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = UISingleton.textColor1
                )
                Text(
                    text = stringResource(R.string.active_student),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Light,
                    color = UISingleton.textColor1
                )
                Text(
                    text = "${stringResource(R.string.student_number)} ${UserDataSingleton.userData?.student_number}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Light,
                    color = UISingleton.textColor1
                )
            }
        }
    }
}