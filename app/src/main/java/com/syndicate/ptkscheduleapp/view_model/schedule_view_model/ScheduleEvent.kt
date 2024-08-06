package com.syndicate.ptkscheduleapp.view_model.schedule_view_model

import java.time.DayOfWeek
import java.time.LocalDate

sealed interface ScheduleEvent {

    data class ChangeSchedule(
        val dayOfWeek: DayOfWeek,
        val isUpperWeek: Boolean,
        val selectedDate: LocalDate
    ): ScheduleEvent
}