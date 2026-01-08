package com.cdkentertainment.mobilny_usos_enhanced.models

import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.Serial
import java.time.LocalDate

class PaymentsPageModel {
    private val parser: Json = Json { ignoreUnknownKeys = true }
    private val paymentsUrl: String = "Payments"
    private fun parsePayments(responseString: String): List<Payment> {
        val parsedPayments: List<Payment> = parser.decodeFromString<List<Payment>>(responseString)
        return parsedPayments
    }
    public suspend fun getAllPayments(): List<Payment> {
        return withContext(Dispatchers.IO) {
            val response = BackendDataSender.get(paymentsUrl)
            if (response.statusCode == 200 && response.body != null) {
                val parsedPayments: List<Payment> = parsePayments(response.body!!)
                return@withContext parsedPayments
            } else {
                throw(Exception("API Error"))
            }
        }
    }

}

@Serializable
data class Payment (
    val id: String,
    val user: SharedDataClasses.Human,
    val saldo_amount: Float,
    val type: SharedDataClasses.IdAndDescription,
    val description: SharedDataClasses.LangDict,
    val state: String,
    val account_number: String,
    val payment_deadline: String,
    val interest: Float?,
    val total_amount: Float,
    val currency: SharedDataClasses.IdAndName,
    val faculty: SharedDataClasses.IdAndName,
    val default_choice_date: String?
)