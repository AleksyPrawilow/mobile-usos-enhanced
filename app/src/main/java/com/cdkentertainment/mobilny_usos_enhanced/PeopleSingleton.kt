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
    val email: String,
    val titles: Map<String?, String?>,
    val room: Room?,
    val staff_status: Int,
    val photo_urls: Map<String, String>,
    val phone_numbers: List<String>,
    val sex: String
)

@Serializable
data class Room(
    val id: Int,
    val number: String,
    val building_name: SharedDataClasses.LangDict,
    val building_id: String
)

data class Student(
    val human: SharedDataClasses.Human,
    var studentData: StudentData? = null
)

@Serializable
data class StudentData(
    val id: String,
    val first_name: String,
    val last_name: String,
    val sex: String,
    val email: String?,
    val has_photo: Boolean,
    val photo_urls: Map<String, String>,
    val student_status: Int?,
    val has_email: Boolean,
    val mobile_numbers: List<String>
)