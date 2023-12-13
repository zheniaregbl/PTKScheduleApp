package com.syndicate.ptkscheduleapp.view_model.app_view_model

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syndicate.ptkscheduleapp.data.model.MainState
import com.syndicate.ptkscheduleapp.data.model.UserMode
import com.syndicate.ptkscheduleapp.domain.repository.ScheduleRepository
import com.syndicate.ptkscheduleapp.info_functions.isNetworkAvailable
import com.syndicate.ptkscheduleapp.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ScheduleRepository,
    private val sharedPreferences: SharedPreferences,
    @ApplicationContext application: Context
) : ViewModel() {

    val firstVisitSchedule = MutableStateFlow(true)

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

            is MainEvent.ChangeUserCourse -> {
                state.update {
                    it.copy(
                        course = event.newUserCourse
                    )
                }

                sharedPreferences.edit().putInt("user_course", event.newUserCourse).apply()
            }

            is MainEvent.ChangeUserGroup -> {
                sharedPreferences.edit().putString("user_group", event.newUserGroup).apply()

                state.update {
                    it.copy(
                        group = event.newUserGroup
                    )
                }
            }

            MainEvent.CreateConfiguration -> {
                initConfiguration()
            }

            is MainEvent.ChangeUserMode -> {
                changeUserMode(event.newUserMode)
            }

            is MainEvent.ChangeAppTheme -> {
                changeAppTheme(event.newTheme)
            }

            MainEvent.FirstVisitSchedule -> {
                firstVisitSchedule.update { false }
            }
        }
    }

    private fun receiveSchedule(context: Context) {

        if (isNetworkAvailable(context)) {
            viewModelScope.launch(Dispatchers.IO) {

                val userGroup = sharedPreferences.getString("user_group", "1991")
                val scheduleJson = repository.getScheduleOnWeek(userGroup!!)
                sharedPreferences.edit().putString("schedule", scheduleJson.toString()).apply()
            }
        }
    }

    private fun initState(context: Context) {

        val networkState = isNetworkAvailable(context)

        viewModelScope.launch(Dispatchers.IO) {
            val isFirstStart = sharedPreferences.getInt("firstStart", 1) == 1
            val appTheme = when (sharedPreferences.getInt("app_theme", 0)) {
                1 -> ThemeMode.FIRST
                2 -> ThemeMode.SECOND
                3 -> ThemeMode.THIRD
                4 -> ThemeMode.FOURTH
                else -> ThemeMode.FIRST
            }

            if (networkState) {
                val isUpperWeek = repository.getCurrentWeek().getBoolean("week_is_upper")
                val replacementJson = repository.getReplacement()

                sharedPreferences.edit().putBoolean("is_upper_week", isUpperWeek).apply()
                sharedPreferences.edit().putString("replacement", replacementJson.toString()).apply()

                state.update {
                    it.copy(
                        colorThemeMode = appTheme,
                        isUpperWeek = isUpperWeek,
                        isFirstStart = isFirstStart
                    )
                }
            } else
                state.update {
                    it.copy(
                        colorThemeMode = appTheme,
                        isUpperWeek = sharedPreferences.getBoolean("is_upper_week", true),
                        isFirstStart = isFirstStart
                    )
                }
        }
    }

    private fun changeUserMode(newUserMode: UserMode) {
        viewModelScope.launch(Dispatchers.IO) {
            state.update {
                it.copy(
                    userMode = newUserMode
                )
            }
        }
    }

    private fun changeAppTheme(newTheme: ThemeMode) {
        viewModelScope.launch(Dispatchers.IO) {
            val numberTheme = when (newTheme) {
                ThemeMode.FIRST -> 1
                ThemeMode.SECOND -> 2
                ThemeMode.THIRD -> 3
                ThemeMode.FOURTH -> 4
            }

            sharedPreferences.edit().putInt("app_theme", numberTheme).apply()

            state.update {
                it.copy(
                    colorThemeMode = newTheme
                )
            }
        }
    }

    private fun initConfiguration() {
        viewModelScope.launch(Dispatchers.IO) {
            sharedPreferences.edit().putInt("firstStart", 0).apply()
        }
    }
}