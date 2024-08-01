package com.syndicate.ptkscheduleapp.data.repository

import com.syndicate.ptkscheduleapp.domain.model.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TestRepository @Inject constructor() {
    val dataList = 1

    fun fetchData(): Flow<RequestState<Int>> {
        return flow {
            emit(RequestState.Loading)
            delay(2000)
            emit(RequestState.Success(data = dataList))
        }
    }


}