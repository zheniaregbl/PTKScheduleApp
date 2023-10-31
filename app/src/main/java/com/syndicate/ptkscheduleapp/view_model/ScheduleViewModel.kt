package com.syndicate.ptkscheduleapp.view_model

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import com.syndicate.ptkscheduleapp.data.model.MainState
import com.syndicate.ptkscheduleapp.data.model.UserMode
import com.syndicate.ptkscheduleapp.domain.repository.ScheduleRepository
import com.syndicate.ptkscheduleapp.info_functions.getScheduleFromJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: ScheduleRepository,
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    val state = MutableStateFlow(MainState())
    private var _scheduleList = MutableLiveData<List<List<LessonItem>>>()
    var scheduleList: LiveData<List<List<LessonItem>>> = _scheduleList

    init {
        initState()
        initLiveData()
    }

    fun onEvent(event: ScheduleEvent) {
        when (event) {

            ScheduleEvent.GetScheduleOnWeek -> {
                initState()
                initLiveData()
            }

            is ScheduleEvent.ChangeUserMode -> {
                changeUserMode(event.newUserMode)
            }

            ScheduleEvent.CreateConfiguration -> {
                createConfiguration()
            }
        }
    }

    private fun initLiveData() {
        viewModelScope.launch(Dispatchers.IO) {
            val scheduleJson = repository.getScheduleOnWeek("1991")
            val schedule = getScheduleFromJson(scheduleJson)
            _scheduleList.postValue(schedule)
        }
    }

    private fun initState() {
        viewModelScope.launch(Dispatchers.IO) {
            val isFirstStart = sharedPreferences.getInt("firstStart", 1) == 1

            state.update { it.copy(
                isUpperWeek = repository.getCurrentWeek().getBoolean("week_is_upper"),
                isFirstStart = isFirstStart
            ) }
        }
    }

    private fun changeUserMode(newUserMode: UserMode) {
        viewModelScope.launch(Dispatchers.IO) {
            state.update { it.copy(
                userMode = newUserMode
            ) }
        }
    }

    private fun createConfiguration() {
        viewModelScope.launch(Dispatchers.IO) {
            sharedPreferences.edit().putInt("firstStart", 0).apply()
        }
    }
}