package com.cdkentertainment.mobilny_usos_enhanced.view_models

import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.LessonGroupPageModel
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    OAuthSingleton.setTestAccessToken()
    val model = LessonGroupPageModel()
    val lessonGroups = model.getLessonGroups()
    println(lessonGroups)
}