package com.syndicate.ptkscheduleapp.view_model

sealed interface ScheduleEvent {
    data object GetScheduleOnWeek: ScheduleEvent
}