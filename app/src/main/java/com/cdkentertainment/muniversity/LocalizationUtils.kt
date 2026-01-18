package com.cdkentertainment.muniversity

import android.content.Context
import com.cdkentertainment.muniversity.models.SharedDataClasses

fun SharedDataClasses.LangDict.getLocalized(context: Context): String {
    val locale = context.resources.configuration.locales[0]

    return when (locale.language) {
        "pl" -> pl.ifEmpty { en }
        else -> en.ifEmpty { pl }
    }
}