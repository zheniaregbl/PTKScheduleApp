package com.syndicate.ptkscheduleapp.view_model

import androidx.lifecycle.ViewModel
import com.syndicate.ptkscheduleapp.domain.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: ScheduleRepository
): ViewModel() {

}