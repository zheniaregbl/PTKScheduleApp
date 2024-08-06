package com.syndicate.ptkscheduleapp.domain.repository

import com.syndicate.ptkscheduleapp.data.model.response.ScheduleResponse
import com.syndicate.ptkscheduleapp.data.model.response.WeekTypeResponse
import com.syndicate.ptkscheduleapp.domain.model.RequestState

interface ReworkScheduleRepository {
    suspend fun getSchedule(group: String): RequestState<ScheduleResponse>
    suspend fun getCurrentWeekType(): RequestState<WeekTypeResponse>
}