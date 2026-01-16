package com.cdkentertainment.bux

import android.content.Context
import com.cdkentertainment.bux.models.SharedDataClasses

fun SharedDataClasses.LangDict.getLocalized(context: Context): String {
    val locale = context.resources.configuration.locales[0]

    return when (locale.language) {
        "pl" -> pl.ifEmpty { en }
        else -> en.ifEmpty { pl }
    }
}