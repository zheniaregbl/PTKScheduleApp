package com.syndicate.ptkscheduleapp.view_model.schedule_view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syndicate.ptkscheduleapp.data.repository.ReworkScheduleRepositoryImpl
import com.syndicate.ptkscheduleapp.domain.model.PairItem
import com.syndicate.ptkscheduleapp.domain.model.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReworkScheduleViewModel @Inject constructor(
    private val repository: ReworkScheduleRepositoryImpl
) : ViewModel() {

    private val _isUpperWeek: MutableStateFlow<RequestState<Boolean>> = MutableStateFlow(RequestState.Idle)
    val isUpperWeek: StateFlow<RequestState<Boolean>> = _isUpperWeek.asStateFlow()

    private val _schedule: MutableStateFlow<RequestState<List<List<PairItem>>>> = MutableStateFlow(RequestState.Loading)
    val schedule: StateFlow<RequestState<List<List<PairItem>>>> = _schedule.asStateFlow()

    init {
        getCurrentWeekType()
        getSchedule()
    }

    private fun getCurrentWeekType() {

        viewModelScope.launch(Dispatchers.IO) {

            _isUpperWeek.update { RequestState.Loading }

            repository.getCurrentWeekType().also { result ->

                if (result.isSuccess()) {
                    _isUpperWeek.update {
                        RequestState.Success(
                            result
                                .getSuccessData()
                                .isUpper
                        )
                    }
                } else if (result.isError()) {
                    _isUpperWeek.update {
                        RequestState.Error(
                            result
                                .getErrorMessage()
                        )
                    }
                }
            }
        }
    }

    private fun getSchedule() {

        viewModelScope.launch(Dispatchers.IO) {

            repository.getSchedule("3921").also { result ->

                delay(500)

                if (result.isSuccess()) {

                    _schedule.update {
                        RequestState.Success(
                            getWeekSchedule(
                                result
                                    .getSuccessData()
                                    .listPair
                                    .filter {
                                        it.subject != ""
                                    }
                                    .map {
                                        it.toPairItem()
                                    }
                            )
                        )
                    }

                } else {
                    Log.d("scheduleResponse", result.getErrorMessage())
                }
            }
        }
    }

    private fun getWeekSchedule(listPair: List<PairItem>): List<List<PairItem>> {

        val schedule = ArrayList<List<PairItem>>()
        var tempList = ArrayList<PairItem>()
        var currentDayOfWeek = ""

        for (index in listPair.indices) {

            val pair = listPair[index]
            val tempDayOfWeek = pair.dayOfWeek

            if (currentDayOfWeek.isEmpty())
                currentDayOfWeek = tempDayOfWeek
            else {
                if (currentDayOfWeek != tempDayOfWeek) {
                    schedule.add(tempList)
                    tempList = ArrayList()
                    currentDayOfWeek = tempDayOfWeek
                }
            }

            tempList.add(pair)
        }

        schedule.add(tempList)

        if (schedule.size < 6)
            schedule.add(emptyList())

        schedule.add(emptyList())

        return schedule
    }
}