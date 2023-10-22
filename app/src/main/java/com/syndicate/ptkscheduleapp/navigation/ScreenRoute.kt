package com.syndicate.ptkscheduleapp.navigation

sealed class ScreenRoute(val route: String) {
    data object SplashScreen: ScreenRoute("splash_screen")
    data object ScheduleScreen: ScreenRoute("schedule_screen")
}
