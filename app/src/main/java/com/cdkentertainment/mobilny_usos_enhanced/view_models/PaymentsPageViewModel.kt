package com.cdkentertainment.mobilny_usos_enhanced.view_models

import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Payment
import com.cdkentertainment.mobilny_usos_enhanced.models.PaymentsPageModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking{
    OAuthSingleton.setTestAccessToken()
    launch {
        val model = PaymentsPageModel()
        val payments: List<Payment> = model.getAllPayments()
        println(payments)
    }
}