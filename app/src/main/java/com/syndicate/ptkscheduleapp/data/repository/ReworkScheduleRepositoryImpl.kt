package com.syndicate.ptkscheduleapp.data.repository

import com.syndicate.ptkscheduleapp.data.model.response.ScheduleResponse
import com.syndicate.ptkscheduleapp.data.model.response.WeekTypeResponse
import com.syndicate.ptkscheduleapp.data.remote.ReworkScheduleApi
import com.syndicate.ptkscheduleapp.domain.model.RequestState
import com.syndicate.ptkscheduleapp.domain.repository.ReworkScheduleRepository
import retrofit2.Response
import javax.inject.Inject

class ReworkScheduleRepositoryImpl @Inject constructor(
    private val scheduleApi: ReworkScheduleApi
) : ReworkScheduleRepository {

    override suspend fun getSchedule(group: String): RequestState<ScheduleResponse> {
        return request {
            scheduleApi.getSchedule(group)
        }
    }

    override suspend fun getCurrentWeekType(): RequestState<WeekTypeResponse> {
        return request {
            scheduleApi.getCurrentWeekType()
        }
    }
}

suspend fun<T> request(request: suspend ()-> Response<T>): RequestState<T> {
    try {
        request().also {
            return if (it.isSuccessful) {
                RequestState.Success(it.body()!!)
            } else {
                RequestState.Error(it.errorBody()?.string().toString())
            }
        }
    } catch (e: Exception) {
        return RequestState.Error(e.message.toString())
    }
}