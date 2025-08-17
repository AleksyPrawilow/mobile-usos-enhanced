package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroup
import com.cdkentertainment.mobilny_usos_enhanced.view_models.AttendancePageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AttendanceClassGroupView(
    data: LessonGroup,
    viewModel: AttendancePageViewModel
) {
    val context: Context = LocalContext.current
    val onClick: () -> Unit = {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.showPopup(data, context)
        }
    }
    ClassGroupCardView(
        data = data,
        onClick = onClick
    )
}