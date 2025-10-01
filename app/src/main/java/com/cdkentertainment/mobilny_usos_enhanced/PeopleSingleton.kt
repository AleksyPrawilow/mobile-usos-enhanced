package com.cdkentertainment.mobilny_usos_enhanced

import androidx.compose.runtime.mutableStateMapOf
import com.cdkentertainment.mobilny_usos_enhanced.models.LecturerAvgRates
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import kotlinx.serialization.Serializable

object PeopleSingleton {
    val lecturers: MutableMap<String, Lecturer> = mutableStateMapOf()
    val students: MutableMap<String, Student> = mutableStateMapOf()
}

data class Lecturer(
    val human: SharedDataClasses.Human,
    var lecturerData: LecturerData? = null,
    val rating: LecturerAvgRates
)

//titles|staff_status|phone_numbers|office_hours|has_photo|photo_urls[100x100]|room
@Serializable
data class LecturerData(
    val id: String,
    val first_name: String,
    val last_name: String,
    val titles: Map<String?, String?>,
    val staff_status: Int,
    val phone_numbers: List<String>,
    val office_hours: SharedDataClasses.LangDict,
    val has_photo: Boolean,
    val photo_urls: Map<String, String>,
    val room: Room?
)

@Serializable
data class Room(
    val id: Int,
    val number: String,
)

data class Student(
    val human: SharedDataClasses.Human,
    val studentData: String = "placeholder"
)