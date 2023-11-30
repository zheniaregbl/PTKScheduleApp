package com.syndicate.ptkscheduleapp.navigation

sealed class ScreenRoute(val route: String) {
    data object SplashScreen: ScreenRoute("splash_screen")
    data object RoleSelectionScreen: ScreenRoute("role_selection_screen")
    data object CourseSelectionScreen: ScreenRoute("course_selection_screen")
    data object GroupSelectionScreen: ScreenRoute("group_selection_screen")
    data object ScheduleScreen: ScreenRoute("schedule_screen")
    data object SettingScreen: ScreenRoute("setting_screen")
    data object ThemeScreen: ScreenRoute("theme_screen")
}
