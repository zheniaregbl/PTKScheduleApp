package com.syndicate.ptkscheduleapp.domain.repository

import org.json.JSONArray

interface ScheduleRepository {
    suspend fun getScheduleOnWeek(group: String): JSONArray
}