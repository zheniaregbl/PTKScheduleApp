package com.syndicate.ptkscheduleapp.view_model.schedule_screen_view_model

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import com.syndicate.ptkscheduleapp.data.model.ScheduleState
import com.syndicate.ptkscheduleapp.info_functions.filterSchedule
import com.syndicate.ptkscheduleapp.info_functions.getScheduleFromJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    private val _state = MutableStateFlow(ScheduleState())
    private var _scheduleList = MutableLiveData<List<List<LessonItem>>>()
    private var _currentSchedule = MutableLiveData<List<LessonItem>>()
    var currentSchedule: LiveData<List<LessonItem>> = _currentSchedule

    init {
        initState()
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentSchedule(_state.value.dayOfWeek, _state.value.isUpperWeek)
        }
    }

    fun onEvent(event: ScheduleEvent) {
        when (event) {

            is ScheduleEvent.ChangeSchedule -> {
                getCurrentSchedule(event.dayOfWeek, event.isUpperWeek)
            }
        }
    }

    private fun initState() {
        viewModelScope.launch(Dispatchers.Main) {
            val scheduleJson = sharedPreferences.getString("schedule", "")
            val schedule = getScheduleFromJson(JSONArray(scheduleJson))
            _scheduleList.postValue(schedule)

            _state.update { it.copy(
                isUpperWeek = sharedPreferences.getBoolean("is_upper_week", true)
            ) }
        }
    }

    private fun getCurrentSchedule(
        dayOfWeek: DayOfWeek,
        isUpperWeek: Boolean
    ) {
        val daySchedule = when(dayOfWeek) {
            DayOfWeek.MONDAY -> _scheduleList.value?.get(0)
            DayOfWeek.TUESDAY -> _scheduleList.value?.get(1)
            DayOfWeek.WEDNESDAY -> _scheduleList.value?.get(2)
            DayOfWeek.THURSDAY -> _scheduleList.value?.get(3)
            DayOfWeek.FRIDAY -> _scheduleList.value?.get(4)
            DayOfWeek.SATURDAY -> _scheduleList.value?.get(5)
            DayOfWeek.SUNDAY -> emptyList()
        }

        _currentSchedule.postValue(
            filterSchedule(daySchedule, isUpperWeek)
        )
    }
}