package com.cdkentertainment.mobilny_usos_enhanced.models

import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/*
 This is a model class. It should not contain any mutableStateOf variables, and should only be used to process
 the data changes sent from the ViewModel
 */
class ExampleModel {
    // Async functions are defined by the "suspend" keyword
    suspend fun fetchUserData(): UserData {
        // Dispatchers.IO is for network
        return withContext(Dispatchers.IO) {
            // Don't forget about data types, even though android studio doesn't seem to like them for some reason
            val response: Map<String, String> = OAuthSingleton.get("users/user")
            /*
            I defined the response Map in a way that it can contain different keys
            based on the success of a call, so we need to check to see if it is a response and not an error
             */
            if (response.containsKey("response") && response["response"] != null) {
                // !! unwraps an optional, so that it can be used as a non-null type. Since we know it's not null, we can use it
                val responseString: String = response["response"]!!
                // Ignoring unknown keys is necessary as USOSApi can return more fields than specified
                val parser: Json = Json { ignoreUnknownKeys = true }
                // Ideally should be wrapped in a try-catch statement, but whatever
                return@withContext parser.decodeFromString<UserData>(responseString)
            } else {
                // Return default instance of UserData if the call fails
                return@withContext UserData()
            }
        }
    }
}

/*
 The serializable annotation is necessary for json parsing to work
 The keys should match the keys in the json response, otherwise they will be ignored
 If you want to have a custom name for a variable, use the "@SerialName("value as in the response")" annotation and then provide a custom key name
 */
@Serializable
data class UserData (
    @SerialName("first_name") val customFirstNameVar: String = "N/A",
    val last_name: String = "N/A",
    val id: String = "N/A",
    // ? in String? means that the variable can be null, which can happen
    val unnecessary_key: String? = null
)