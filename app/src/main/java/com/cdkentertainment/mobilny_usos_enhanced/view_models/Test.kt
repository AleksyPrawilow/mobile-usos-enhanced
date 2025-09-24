package com.cdkentertainment.mobilny_usos_enhanced.view_models

import com.cdkentertainment.mobilny_usos_enhanced.models.BackendDataSender
import com.cdkentertainment.mobilny_usos_enhanced.models.Payment
import com.cdkentertainment.mobilny_usos_enhanced.models.PaymentsPageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.Schedule
import com.cdkentertainment.mobilny_usos_enhanced.models.SchedulePageModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json


fun main(): Unit = runBlocking {
    val model = SchedulePageModel()
    launch {
       val schedule: Schedule = model.getSingleDaySchedule()
        println(schedule)
    }
}

