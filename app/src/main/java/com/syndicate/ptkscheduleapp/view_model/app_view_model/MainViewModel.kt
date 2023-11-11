package com.syndicate.ptkscheduleapp.view_model.app_view_model

import android.content.SharedPreferences
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
class MainViewModel @Inject constructor(
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

    fun onEvent(event: MainEvent) {
        when (event) {

            MainEvent.GetScheduleOnWeek -> {
                initState()
                initLiveData()
            }

            is MainEvent.ChangeUserMode -> {
                changeUserMode(event.newUserMode)
            }

            MainEvent.CreateConfiguration -> {
                createConfiguration()
            }
        }
    }

    private fun initLiveData() {
        viewModelScope.launch(Dispatchers.IO) {
            val scheduleJson = repository.getScheduleOnWeek("1991")
            val schedule = getScheduleFromJson(scheduleJson)
            _scheduleList.postValue(schedule)

            sharedPreferences.edit().putString("schedule", scheduleJson.toString()).apply()
        }
    }

    private fun initState() {
        viewModelScope.launch(Dispatchers.IO) {
            val isFirstStart = sharedPreferences.getInt("firstStart", 1) == 1

            val isUpperWeek = repository.getCurrentWeek().getBoolean("week_is_upper")

            sharedPreferences.edit().putBoolean("is_upper_week", isUpperWeek).apply()

            state.update { it.copy(
                isUpperWeek = isUpperWeek,
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