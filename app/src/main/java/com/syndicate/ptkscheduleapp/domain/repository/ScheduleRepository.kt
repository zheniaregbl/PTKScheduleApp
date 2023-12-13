package com.syndicate.ptkscheduleapp.domain.repository

import org.json.JSONArray
import org.json.JSONObject

interface ScheduleRepository {
    suspend fun getScheduleOnWeek(group: String): JSONArray
    suspend fun getCurrentWeek(): JSONObject
    suspend fun getListGroupByCourse(course: String): JSONArray
    suspend fun getReplacement(): JSONObject
}