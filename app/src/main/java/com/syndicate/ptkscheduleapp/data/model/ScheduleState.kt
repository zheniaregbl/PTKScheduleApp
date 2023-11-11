package com.syndicate.ptkscheduleapp.data.model

import java.time.DayOfWeek
import java.time.LocalDate

data class ScheduleState(
    val dayOfWeek: DayOfWeek = LocalDate.now().dayOfWeek,
    val isUpperWeek: Boolean = true,
    val selectedDate: LocalDate = LocalDate.now()
)
