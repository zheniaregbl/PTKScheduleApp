package com.syndicate.ptkscheduleapp.data.model

import com.syndicate.ptkscheduleapp.ui.theme.ThemeMode

data class MainState(
    val colorThemeMode: ThemeMode = ThemeMode.FIRST,
    val isUpperWeek: Boolean = true,
    val userMode: UserMode = UserMode.Student,
    val isFirstStart: Boolean = true
)