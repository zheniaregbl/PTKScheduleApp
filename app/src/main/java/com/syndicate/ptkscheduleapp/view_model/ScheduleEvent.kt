package com.syndicate.ptkscheduleapp.view_model

import com.syndicate.ptkscheduleapp.data.model.UserMode

sealed interface ScheduleEvent {
    data object GetScheduleOnWeek: ScheduleEvent
    data class ChangeUserMode(
        val newUserMode: UserMode
    ): ScheduleEvent
}