package com.syndicate.ptkscheduleapp.data.remote

import com.syndicate.ptkscheduleapp.data.model.response.ScheduleResponse
import com.syndicate.ptkscheduleapp.data.model.response.WeekTypeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReworkScheduleApi {

    @GET("schedule/get")
    suspend fun getSchedule(
        @Query("group") group: String,
        @Query("schedule_type") type: String = "week",
        @Query("withReplacement") replacement: Boolean = false,
    ): Response<ScheduleResponse>

    @GET("settings/week/get")
    suspend fun getCurrentWeekType(): Response<WeekTypeResponse>

    @GET("replacements/get")
    suspend fun getReplacement()
}