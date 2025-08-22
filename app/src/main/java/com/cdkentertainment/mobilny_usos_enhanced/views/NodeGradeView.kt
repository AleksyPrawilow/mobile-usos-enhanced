package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.runtime.Composable

@Composable
fun NodeGradeView() {
    GradeCardView(
        courseName = "Nazwa oceny",
        grade = "5",
        onClick = {
            // Popup view with grade stats
        }
    )
}