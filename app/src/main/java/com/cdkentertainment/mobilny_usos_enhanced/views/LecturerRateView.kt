package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerRate
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LecturerRatesPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.min

@Composable
fun LecturerRateView(
    lecturerId: String,
    title: String = stringResource(R.string.overall_rating),
    numberOfReviews: Int = 0,
    showNumberOfReviews: Boolean = true,
    rate: LecturerRate = LecturerRate(),
    onAddRate: (LecturerRate) -> Unit = { rate ->
        println(rate)
    },
    onEditRate: () -> Unit = {},
) {
    val viewModel: LecturerRatesPageViewModel = viewModel<LecturerRatesPageViewModel>()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    val deletionFailureToast: Toast = Toast.makeText(context, stringResource(R.string.something_went_wrong), Toast.LENGTH_SHORT)
    val rateAverage: Float = (rate.rate_1 + rate.rate_2 + rate.rate_3 + rate.rate_4 + rate.rate_5) / 5f
    var showDeleteDialog: Boolean by rememberSaveable { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!showNumberOfReviews && numberOfReviews == 0) {
            LecturerAddRateView(rate, onAddRate = onAddRate)
        } else {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = UISingleton.textColor1
            )
            Text(
                text = "%.1f".format(rateAverage),
                fontSize = 72.sp,
                fontWeight = FontWeight.ExtraBold,
                color = UISingleton.textColor1
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
            ) {
                repeat(5) { index ->
                    val fractionFilled: Float = min(rateAverage - index, 1f)
                    val starBrush = Brush.horizontalGradient(
                        0f to UISingleton.color3,
                        fractionFilled to UISingleton.color3,
                        fractionFilled to UISingleton.color2,
                        1f to UISingleton.color2
                    )
                    val starModifier: Modifier = Modifier
                        .size(32.dp)
                        .graphicsLayer {
                            compositingStrategy = CompositingStrategy.Offscreen
                        }
                        .drawWithContent {
                            drawContent()
                            drawRect(starBrush, size = this.size, blendMode = BlendMode.SrcAtop)
                        }
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Star",
                        tint = Color.Unspecified,
                        modifier = starModifier
                    )
                }
            }
            if (showNumberOfReviews) {
                val reviewTextSuffix: String = when {
                    numberOfReviews == 1 -> stringResource(R.string.review_singular)
                    else -> stringResource(R.string.reviews_plural)
                }
                Text(
                    text = "${stringResource(R.string.based_on_reviews)} $numberOfReviews $reviewTextSuffix",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Light,
                    color = UISingleton.textColor1
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            val categories: Map<String, Float> = mapOf(
                stringResource(R.string.clarity_rate_name) to rate.rate_1,
                stringResource(R.string.organization_rate_name) to rate.rate_2,
                stringResource(R.string.engagement_rate_name) to rate.rate_3,
                stringResource(R.string.communication_rate_name) to rate.rate_4,
                stringResource(R.string.fairness_rate_name) to rate.rate_5
            )
            for ((category, rating) in categories) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleSmall,
                        color = UISingleton.textColor1,
                        modifier = Modifier.weight(0.5f)
                    )
                    LinearProgressIndicator(
                        progress = { rating / 5f },
                        color = UISingleton.color3,
                        trackColor = UISingleton.color2,
                        drawStopIndicator = {},
                        modifier = Modifier
                            .weight(0.5f)
                    )
                }
            }
            if (!showNumberOfReviews) {
                if (showDeleteDialog) {
                    ConfirmDialogPopupView(
                        title = stringResource(R.string.delete_rating_confirm),
                        confirmTitle = stringResource(R.string.yes),
                        dismissTitle = stringResource(R.string.no),
                        onConfirm = {
                            coroutineScope.launch {
                                val deleted = viewModel.deleteUserRate(lecturerId = lecturerId)
                                if (deleted) {
                                    showDeleteDialog = false
                                } else {
                                    deletionFailureToast.show()
                                    showDeleteDialog = false
                                }
                            }
                        },
                        onDismiss = { showDeleteDialog = false }
                    )
//                    UserRateDeleteDialogView(lecturerId) {
//                        showDeleteDialog = false
//                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                        .background(UISingleton.color2)
                        .clickable(onClick = onEditRate)
                        .padding(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.update_rating),
                        style = MaterialTheme.typography.titleMedium,
                        color = UISingleton.textColor1,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.rounded_edit_24),
                        contentDescription = "Edit",
                        tint = UISingleton.textColor4,
                        modifier = Modifier
                            .size(48.dp)
                            .background(UISingleton.color3, CircleShape)
                            .padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                        .background(UISingleton.color2)
                        .clickable(onClick = { showDeleteDialog = true })
                        .padding(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.delete_rating),
                        style = MaterialTheme.typography.titleMedium,
                        color = UISingleton.textColor1,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.rounded_delete_24),
                        contentDescription = "Delete",
                        tint = UISingleton.textColor4,
                        modifier = Modifier
                            .size(48.dp)
                            .background(UISingleton.color3, CircleShape)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}