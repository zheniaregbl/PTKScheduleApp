package com.syndicate.ptkscheduleapp.view_model.schedule_view_model

import java.time.LocalDate

sealed interface ReworkScheduleEvent {

    data class ChangeSelectedDate(
        val date: LocalDate
    ) : ReworkScheduleEvent

    data class ChangeSchedulePage(
        val page: Int
    ) : ReworkScheduleEvent
}