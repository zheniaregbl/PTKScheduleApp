package com.syndicate.ptkscheduleapp.view_model.schedule_view_model

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.syndicate.ptkscheduleapp.common.utils.ScheduleUtils
import com.syndicate.ptkscheduleapp.data.model.response.PairObject
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
import java.time.LocalDate
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class ReworkScheduleViewModel @Inject constructor(
    private val repository: ReworkScheduleRepositoryImpl,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _isUpperWeek: MutableStateFlow<RequestState<Boolean>> = MutableStateFlow(RequestState.Idle)
    val isUpperWeek: StateFlow<RequestState<Boolean>> = _isUpperWeek.asStateFlow()

    private val _schedule: MutableStateFlow<RequestState<List<List<PairItem>>>> = MutableStateFlow(RequestState.Loading)
    val schedule: StateFlow<RequestState<List<List<PairItem>>>> = _schedule.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _currentSchedulePage = MutableStateFlow(0)
    val currentSchedulePage: StateFlow<Int> = _currentSchedulePage.asStateFlow()

    private val _previousSchedulePage = MutableStateFlow(0)
    val previousSchedulePage: StateFlow<Int> = _previousSchedulePage.asStateFlow()

    init {
        initCurrentSchedulePage()
        getCurrentWeekType()
        getSchedule()
    }

    fun onEvent(event: ReworkScheduleEvent) {

        when (event) {

            is ReworkScheduleEvent.ChangeSelectedDate -> _selectedDate.update { event.date }

            is ReworkScheduleEvent.ChangeSchedulePage -> {

                val previousValue = _currentSchedulePage.value

                _previousSchedulePage.update { if (it != previousValue) previousValue else it }
                _currentSchedulePage.update { event.page }
            }
        }
    }

    private fun initCurrentSchedulePage() {

        val weeks = ScheduleUtils.getWeeksFromStartDate(
            LocalDate.of(LocalDate.now().year, Month.JANUARY, 1),
            78
        )

        val initWeekNumber = ScheduleUtils.getCurrentWeek(
            weeks,
            _selectedDate.value
        )
        val initPage = (initWeekNumber * 7) + weeks[initWeekNumber].indexOf(_selectedDate.value)

        _currentSchedulePage.update { initPage }
    }

    private fun getCurrentWeekType() {

        viewModelScope.launch(Dispatchers.IO) {

            _isUpperWeek.update { RequestState.Loading }

            repository.getCurrentWeekType().also { result ->

                if (result.isSuccess()) {

                    sharedPreferences
                        .edit()
                        .putBoolean("local_week_type", result.getSuccessData().isUpper)
                        .apply()

                    _isUpperWeek.update {
                        RequestState.Success(
                            result
                                .getSuccessData()
                                .isUpper
                        )
                    }
                } else if (result.isError()) {
                    _isUpperWeek.update {
                        RequestState.Success(
                            sharedPreferences
                                .getBoolean("local_week_type", false)
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

                    updateLocalSchedule(result.getSuccessData().listPair)

                    _schedule.update {
                        RequestState.Success(
                            getWeekSchedule(
                                result
                                    .getSuccessData()
                                    .listPair
                                    .filter { it.subject != "" }
                                    .map { it.toPairItem() }
                            )
                        )
                    }

                } else if (result.isError()) {

                    val localSchedule = getScheduleFromSharedPreferences()

                    _schedule.update {
                        RequestState.Success(
                            getWeekSchedule(
                                localSchedule
                                    .filter { it.subject != "" }
                                    .map { it.toPairItem() }
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getScheduleFromSharedPreferences(): List<PairObject> {

        val localSchedule = PairObjectUtils
            .jsonAdapter
            .fromJson(sharedPreferences.getString("local_schedule", "")!!)

        return localSchedule as List<PairObject>
    }

    private fun updateLocalSchedule(schedule: List<PairObject>) {

        val localSchedule = PairObjectUtils.jsonAdapter.toJson(schedule)

        sharedPreferences.edit().putString("local_schedule", localSchedule).apply()
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

object PairObjectUtils {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val type = Types.newParameterizedType(List::class.java, PairObject::class.java)
    val jsonAdapter: JsonAdapter<List<PairObject>> = moshi.adapter(type)
}