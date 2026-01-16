package com.cdkentertainment.bux.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cdkentertainment.bux.models.Payment
import com.cdkentertainment.bux.models.PaymentsPageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentsPageViewModel: ViewModel() {
    var loading: Boolean by mutableStateOf(false)
    var loaded : Boolean by mutableStateOf(false)
    var error  : Boolean by mutableStateOf(false)
    var unpaidPayments: List<Payment>? by mutableStateOf(null)
    var paidPayments: List<Payment>? by mutableStateOf(null)
    var unpaidSum: Float by mutableStateOf(0f)
    val model: PaymentsPageModel = PaymentsPageModel()

    fun fetchPayments() {
        if (unpaidPayments != null && paidPayments != null) {
            loading = false
            error = false
            loaded = true
            return
        }
        if (loading) return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loading = true
                error = false
                loaded = false
                try {
                    val payments: List<Payment> = model.getAllPayments()
                    unpaidPayments = payments.filter { it.state == "unpaid" }
                    paidPayments   = payments.filter { it.state == "paid"   }
                    sumUnpaidPayments()
                    loading = false
                    error = false
                    loaded = true
                } catch (e: Exception) {
                    error = true
                    loading = false
                    loaded = false
                    return@withContext
                }
            }
        }
    }

    private fun sumUnpaidPayments() {
        var sum: Float = 0.0f
        unpaidPayments?.forEach { sum += it.total_amount }
        unpaidSum = sum
    }
}