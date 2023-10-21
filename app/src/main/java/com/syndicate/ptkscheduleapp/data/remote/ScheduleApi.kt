package com.syndicate.ptkscheduleapp.data.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleApi {

    @GET("getschedule")
    suspend fun getScheduleOnWeek(
        @Query("group") group: String,
        @Query("schedule_type") scheduleType: String
    ): Response<JsonArray>

    @GET("getcurrentweek")
    suspend fun getCurrentWeek(): Response<JsonObject>
}