package com.syndicate.ptkscheduleapp.view_model.group_selection_screen_view_model

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syndicate.ptkscheduleapp.domain.repository.ScheduleRepository
import com.syndicate.ptkscheduleapp.info_functions.getListGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupSelectionViewModel @Inject constructor(
    private val repository: ScheduleRepository,
    private val sharedPreferences: SharedPreferences,
): ViewModel() {

    val isListFill = MutableStateFlow(false)
    private var _listGroup = MutableLiveData<List<String>>()

    var listGroup: LiveData<List<String>> = _listGroup

    fun onEvent(event: GroupSelectionEvent) {
        when (event) {

            GroupSelectionEvent.FillListGroup -> {
                fillListGroup()
            }
        }
    }

    private fun fillListGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            val userCourse = sharedPreferences.getInt("user_course", 3)
            val groupJson = repository.getListGroupByCourse(userCourse.toString())

            _listGroup.postValue(
                getListGroup(groupJson)
            )

            delay(500)

            isListFill.update { true }
        }
    }
}