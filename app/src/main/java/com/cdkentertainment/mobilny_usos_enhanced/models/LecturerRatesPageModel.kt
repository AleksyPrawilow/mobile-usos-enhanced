package com.cdkentertainment.mobilny_usos_enhanced.models

import kotlinx.serialization.Serializable

class LecturerRatesPageModel {
}

@Serializable
data class LecturerRate(
    val rate_1: Float = 0f,
    val rate_2: Float = 0f,
    val rate_3: Float = 0f,
    val rate_4: Float = 0f,
    val rate_5: Float = 0f
)