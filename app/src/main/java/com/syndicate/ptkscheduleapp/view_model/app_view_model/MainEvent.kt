package com.syndicate.ptkscheduleapp.view_model.app_view_model

import com.syndicate.ptkscheduleapp.data.model.UserMode
import com.syndicate.ptkscheduleapp.ui.theme.ThemeMode

sealed interface MainEvent {

    data object ReceiveScheduleFromServer: MainEvent

    data class ChangeUserCourse(
        val newUserCourse: Int
    ): MainEvent

    data class ChangeUserGroup(
        val newUserGroup: String
    ): MainEvent

    data object CreateConfiguration: MainEvent

    data class ChangeUserMode(
        val newUserMode: UserMode
    ): MainEvent

    data class ChangeAppTheme(
        val newTheme: ThemeMode
    ): MainEvent

    data object FirstVisitSchedule: MainEvent
}