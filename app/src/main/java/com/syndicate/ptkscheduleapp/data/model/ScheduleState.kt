package com.syndicate.ptkscheduleapp.data.model

import androidx.compose.runtime.Stable
import java.time.DayOfWeek
import java.time.LocalDate

@Stable
data class ScheduleState(
    val dayOfWeek: DayOfWeek = LocalDate.now().dayOfWeek,
    val isUpperWeek: Boolean = true,
    val selectedDate: LocalDate = LocalDate.now()
)
