package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.Payment
import com.cdkentertainment.mobilny_usos_enhanced.models.PaymentsPageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.prettyPrint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main(): Unit = runBlocking{
    OAuthSingleton.setTestAccessToken()
    launch {
        val model = PaymentsPageModel()
        val payments: List<Payment> = model.getAllPayments()

        val filtered: List<Payment> = payments.filter { it.state == "unpaid" }
        println(filtered.prettyPrint())
    }
}

class PaymentsPageViewModel: ViewModel() {
    var unpaidPayments: List<Payment>? by mutableStateOf(null)
    var paidPayments: List<Payment>? by mutableStateOf(null)
    var unpaidSum: Float by mutableStateOf(0f)
    val model: PaymentsPageModel = PaymentsPageModel()

    suspend fun fetchPayments() {
        withContext(Dispatchers.IO) {
            if (unpaidPayments != null && paidPayments != null) {
                return@withContext
            }
            try {
                val payments: List<Payment> = model.getAllPayments()
                unpaidPayments = payments.filter { it.state == "unpaid" }
                paidPayments   = payments.filter { it.state == "paid"   }
                sumUnpaidPayments()
            } catch (e: Exception) {
                return@withContext
            }
        }
    }

    private fun sumUnpaidPayments() {
        var sum: Float = 0.0f
        unpaidPayments?.forEach { sum += it.total_amount }
        unpaidSum = sum
    }
}