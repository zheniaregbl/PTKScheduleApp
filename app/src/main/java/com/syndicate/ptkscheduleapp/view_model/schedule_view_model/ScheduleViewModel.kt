package com.syndicate.ptkscheduleapp.view_model.schedule_view_model

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import com.syndicate.ptkscheduleapp.data.model.ScheduleState
import com.syndicate.ptkscheduleapp.info_functions.filterSchedule
import com.syndicate.ptkscheduleapp.info_functions.getReplacementFromJsonByDay
import com.syndicate.ptkscheduleapp.info_functions.getScheduleFromJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(ScheduleState())
    private val _scheduleList = MutableLiveData<List<List<LessonItem>>>()
    private val _currentSchedule = MutableLiveData<List<LessonItem>>()
    private val _dayReplacement = MutableLiveData<List<LessonItem>>()

    // For pager on schedule screen (not use now)
    private val _filterWeekSchedule = MutableLiveData<List<List<LessonItem>>>()

    var currentSchedule: LiveData<List<LessonItem>> = _currentSchedule
    var dayReplacement: LiveData<List<LessonItem>> = _dayReplacement

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
                getCurrentReplacementRework(event.selectedDate)
            }
        }
    }

    private fun initState() {
        viewModelScope.launch(Dispatchers.Main) {
            val scheduleJson = sharedPreferences.getString("schedule", "")
            val schedule = getScheduleFromJson(JSONArray(scheduleJson))
            _scheduleList.postValue(schedule)

            _state.update {
                it.copy(
                    isUpperWeek = sharedPreferences.getBoolean("is_upper_week", true)
                )
            }
        }
    }

    private fun getCurrentSchedule(
        dayOfWeek: DayOfWeek,
        isUpperWeek: Boolean
    ) {

        val daySchedule = when (dayOfWeek) {
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

    // For pager on schedule screen (not use now)
    private fun getCurrentSchedule(
        isUpperWeek: Boolean
    ) {

        val listLessons = ArrayList<List<LessonItem>>()

        for (i in 0..6) {

            val daySchedule = if (!_scheduleList.value?.get(i).isNullOrEmpty()) _scheduleList.value?.get(i)
                else emptyList()

            listLessons.add(filterSchedule(daySchedule, isUpperWeek))
        }

        _filterWeekSchedule.postValue(
            listLessons
        )
    }

    private fun getCurrentReplacementRework(
        selectedDate: LocalDate
    ) {

        viewModelScope.launch(Dispatchers.IO) {

            val stringJsonObject = sharedPreferences.getString("replacement", "")

            val replacementJson = if (stringJsonObject.isNullOrEmpty()) JSONObject()
            else JSONObject(stringJsonObject)

            _dayReplacement.postValue(
                getReplacementFromJsonByDay(
                    selectedDate,
                    replacementJson,
                    sharedPreferences.getString("user_group", "1991")!!
                )
            )
        }
    }
}