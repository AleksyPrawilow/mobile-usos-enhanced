package com.cdkentertainment.mobilny_usos_enhanced.models

import kotlinx.serialization.Serializable

class SharedDataClasses {
    @Serializable
    data class IdAndDescription(
        val id: String,
        val description: LangDict
    )

    @Serializable
    data class IdAndName(
        val id: String,
        val name: LangDict
    )

    @Serializable
    data class Human (
        val id: String,
        val first_name: String,
        val last_name: String
    )

    @Serializable
    data class LangDict(
        val pl: String,
        val en: String
    )
}

