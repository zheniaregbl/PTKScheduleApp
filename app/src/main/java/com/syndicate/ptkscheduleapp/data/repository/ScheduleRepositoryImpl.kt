package com.syndicate.ptkscheduleapp.data.repository

import com.syndicate.ptkscheduleapp.data.remote.ScheduleApi
import com.syndicate.ptkscheduleapp.domain.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleApi: ScheduleApi
): ScheduleRepository {

    override suspend fun getScheduleOnWeek(group: String): JSONArray = withContext(Dispatchers.IO) {
        val response = scheduleApi.getScheduleOnWeek(
            group,
            "week"
        )

        if (response.isSuccessful) JSONArray(response.body().toString()) else JSONArray()
    }
}