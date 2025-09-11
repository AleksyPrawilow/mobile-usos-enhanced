package com.cdkentertainment.mobilny_usos_enhanced

import android.content.Context
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses

fun SharedDataClasses.LangDict.getLocalized(context: Context): String {
    val locale = context.resources.configuration.locales[0]

    return when (locale.language) {
        "pl" -> pl.ifEmpty { en }
        else -> en.ifEmpty { pl }
    }
}