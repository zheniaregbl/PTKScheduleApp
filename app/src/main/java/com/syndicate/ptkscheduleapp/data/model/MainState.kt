package com.syndicate.ptkscheduleapp.data.model

import androidx.compose.runtime.Stable
import com.syndicate.ptkscheduleapp.ui.theme.ThemeMode

@Stable
data class MainState(
    val colorThemeMode: ThemeMode = ThemeMode.FIRST,
    val isUpperWeek: Boolean = true,
    val userMode: UserMode = UserMode.Student,
    val group: String = "1991",
    val course: Int = 3,
    val isFirstStart: Boolean = true
)