package com.syndicate.ptkscheduleapp.view_model.schedule_screen_view_model

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import com.syndicate.ptkscheduleapp.data.model.ScheduleState
import com.syndicate.ptkscheduleapp.info_functions.filterSchedule
import com.syndicate.ptkscheduleapp.info_functions.getReplacementFromJson
import com.syndicate.ptkscheduleapp.info_functions.getScheduleFromJson
import com.syndicate.ptkscheduleapp.info_functions.getStartDate
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
    private val _replacement = MutableLiveData<List<List<List<LessonItem>>>>()
    private val _dayReplacement = MutableLiveData<List<LessonItem>>()

    var currentSchedule: LiveData<List<LessonItem>> = _currentSchedule
    var dayReplacement: LiveData<List<LessonItem>> = _dayReplacement

    init {
        initState()
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentSchedule(_state.value.dayOfWeek, _state.value.isUpperWeek)
        }
        getReplacement()
    }

    fun onEvent(event: ScheduleEvent) {
        when (event) {

            is ScheduleEvent.ChangeSchedule -> {
                getCurrentSchedule(event.dayOfWeek, event.isUpperWeek)
                getCurrentReplacement(event.selectedDate, event.dayOfWeek)
            }

            is ScheduleEvent.GetReplacement -> {
                getCurrentReplacement(event.selectedDate, event.selectedDate.dayOfWeek)
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

    private fun getCurrentReplacement(
        selectedDate: LocalDate,
        dayOfWeek: DayOfWeek
    ) {

        val startDate = getStartDate(LocalDate.now(), 2)

        val weekList = when {
            _replacement.value.isNullOrEmpty() -> emptyList()
            selectedDate <= startDate.plusWeeks(1) .minusDays(1) && selectedDate >= startDate -> _replacement.value?.get(0)
            selectedDate <= startDate.plusWeeks(2).minusDays(1) && selectedDate >= startDate.plusWeeks(1) -> _replacement.value?.get(1)
            selectedDate <= startDate.plusWeeks(3).minusDays(1) && selectedDate >= startDate.plusWeeks(2) -> _replacement.value?.get(2)
            else -> emptyList()
        }

        val dayReplacement = when (dayOfWeek) {
            DayOfWeek.MONDAY -> if (weekList.isNullOrEmpty()) emptyList() else weekList[0]
            DayOfWeek.TUESDAY -> if (weekList.isNullOrEmpty()) emptyList() else {
                if (weekList.size < 2) emptyList() else weekList[1]
            }
            DayOfWeek.WEDNESDAY -> if (weekList.isNullOrEmpty()) emptyList() else {
                if (weekList.size < 3) emptyList() else weekList[2]
            }
            DayOfWeek.THURSDAY -> if (weekList.isNullOrEmpty()) emptyList() else {
                if (weekList.size < 4) emptyList() else weekList[3]
            }
            DayOfWeek.FRIDAY -> if (weekList.isNullOrEmpty()) emptyList() else {
                if (weekList.size < 5) emptyList() else weekList[4]
            }
            DayOfWeek.SATURDAY -> if (weekList.isNullOrEmpty()) emptyList() else {
                if (weekList.size < 6) emptyList() else weekList[5]
            }
            DayOfWeek.SUNDAY -> emptyList()
        }

        _dayReplacement.postValue(
            dayReplacement.ifEmpty { emptyList() }
        )
    }

    private fun getReplacement() {
        viewModelScope.launch(Dispatchers.IO) {

            val stringJsonObject = sharedPreferences.getString("replacement", "")

            val replacementJson = if (stringJsonObject.isNullOrEmpty()) JSONObject()
                            else JSONObject(stringJsonObject)

            val replacement = getReplacementFromJson(
                LocalDate.now(),
                2,
                replacementJson,
                sharedPreferences.getString("user_group", "1991")!!
            )

            _replacement.postValue(
                replacement
            )
        }
    }
}