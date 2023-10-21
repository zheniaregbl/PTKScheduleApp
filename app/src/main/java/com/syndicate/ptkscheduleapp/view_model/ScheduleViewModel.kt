package com.syndicate.ptkscheduleapp.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import com.syndicate.ptkscheduleapp.domain.repository.ScheduleRepository
import com.syndicate.ptkscheduleapp.info_functions.getScheduleFromJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: ScheduleRepository
): ViewModel() {

    private var _listLessonsOnWeek = MutableLiveData<List<List<LessonItem>>>()
    var listLessonsOnWeek:LiveData<List<List<LessonItem>>> = _listLessonsOnWeek

    init {

        viewModelScope.launch(Dispatchers.IO) {
            val scheduleJson = repository.getScheduleOnWeek("1991")
            val schedule = getScheduleFromJson(scheduleJson)
            _listLessonsOnWeek.postValue(schedule)
        }
    }

    fun onEvent(event: ScheduleEvent) {
        when (event) {
            ScheduleEvent.GetScheduleOnWeek -> {

            }
        }
    }
}