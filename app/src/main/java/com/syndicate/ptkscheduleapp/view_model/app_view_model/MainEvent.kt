package com.syndicate.ptkscheduleapp.view_model.app_view_model

import com.syndicate.ptkscheduleapp.data.model.UserMode

sealed interface MainEvent {

    data object GetScheduleOnWeek: MainEvent

    data object CreateConfiguration: MainEvent

    data class ChangeUserMode(
        val newUserMode: UserMode
    ): MainEvent
}