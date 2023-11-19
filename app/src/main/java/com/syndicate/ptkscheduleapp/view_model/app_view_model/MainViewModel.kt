package com.syndicate.ptkscheduleapp.view_model.app_view_model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syndicate.ptkscheduleapp.data.model.MainState
import com.syndicate.ptkscheduleapp.data.model.UserMode
import com.syndicate.ptkscheduleapp.domain.repository.ScheduleRepository
import com.syndicate.ptkscheduleapp.info_functions.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ScheduleRepository,
    private val sharedPreferences: SharedPreferences,
    @ApplicationContext application: Context
): ViewModel() {

    val state = MutableStateFlow(MainState())
    private val context = MutableStateFlow(application)

    init {
        initState(context.value)
    }

    fun onEvent(event: MainEvent) {
        when (event) {

            MainEvent.ReceiveScheduleFromServer -> {
                receiveSchedule(context.value)
            }

            is MainEvent.ChangeUserGroup -> {
                sharedPreferences.edit().putString("user_group", event.newUserGroup).apply()

                state.update { it.copy(
                    group = event.newUserGroup
                ) }
            }

            MainEvent.CreateConfiguration -> {
                initConfiguration()
            }

            is MainEvent.ChangeUserMode -> {
                changeUserMode(event.newUserMode)
            }
        }
    }

    private fun receiveSchedule(context: Context) {

        if (isNetworkAvailable(context)) {
            viewModelScope.launch(Dispatchers.IO) {

                val scheduleJson = repository.getScheduleOnWeek(state.value.group)
                sharedPreferences.edit().putString("schedule", scheduleJson.toString()).apply()
            }
        }
    }

    private fun initState(context: Context) {

        val networkState = isNetworkAvailable(context)

        viewModelScope.launch(Dispatchers.IO) {
            val isFirstStart = sharedPreferences.getInt("firstStart", 1) == 1

            if (networkState) {
                var isUpperWeek = repository.getCurrentWeek().getBoolean("week_is_upper")

                /*if (LocalDate.now().dayOfWeek == DayOfWeek.SUNDAY)
                    isUpperWeek = !isUpperWeek*/

                sharedPreferences.edit().putBoolean("is_upper_week", isUpperWeek).apply()
                state.update { it.copy(
                    isUpperWeek = isUpperWeek,
                    isFirstStart = isFirstStart
                ) }
            } else
                state.update { it.copy(
                    isUpperWeek = sharedPreferences.getBoolean("is_upper_week", true),
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

    private fun initConfiguration() {
        viewModelScope.launch(Dispatchers.IO) {
            sharedPreferences.edit().putInt("firstStart", 0).apply()
        }
    }
}