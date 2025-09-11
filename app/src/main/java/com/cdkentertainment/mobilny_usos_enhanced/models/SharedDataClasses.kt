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

// from https://gist.github.com/mayankmkh/92084bdf2b59288d3e74c3735cccbf9f
// Only for tests!!
fun Any.prettyPrint(): String {

    var indentLevel = 0
    val indentWidth = 4

    fun padding() = "".padStart(indentLevel * indentWidth)

    val toString = toString()

    val stringBuilder = StringBuilder(toString.length)

    var i = 0
    while (i < toString.length) {
        when (val char = toString[i]) {
            '(', '[', '{' -> {
                indentLevel++
                stringBuilder.appendLine(char).append(padding())
            }
            ')', ']', '}' -> {
                indentLevel--
                stringBuilder.appendLine().append(padding()).append(char)
            }
            ',' -> {
                stringBuilder.appendLine(char).append(padding())
                // ignore space after comma as we have added a newline
                val nextChar = toString.getOrElse(i + 1) { char }
                if (nextChar == ' ') i++
            }
            else -> {
                stringBuilder.append(char)
            }
        }
        i++
    }

    return stringBuilder.toString()
}
