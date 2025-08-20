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
    private val paymentsUrl: String = "payments/user_payments"
    private val paymentsFields: String
    = "id|user|saldo_amount|type|description|state|account_number|payment_deadline|interest|total_amount|currency|faculty|default_choice_date"
    private fun parsePayments(responseString: String): List<Payment> {
        val parsedPayments: List<Payment> = parser.decodeFromString<List<Payment>>(responseString)
        return parsedPayments
    }
    public suspend fun getAllPayments(): List<Payment> {
        return withContext(Dispatchers.IO) {
            val response = OAuthSingleton.get("$paymentsUrl?fields=$paymentsFields")
            if (response.containsKey("response") && response["response"] != null) {
                val parsedPayments: List<Payment> = parsePayments(response["response"]!!)
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
    val user: Human,
    val saldo_amount: Float,
    val type: IdAndDescription,
    val description: LangDict,
    val state: String,
    val account_number: String,
    val payment_deadline: String,
    val interest: Float?,
    val total_amount: Float,
    val currency: IdAndName,
    val faculty: IdAndName,
    val default_choice_date: String?
)

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