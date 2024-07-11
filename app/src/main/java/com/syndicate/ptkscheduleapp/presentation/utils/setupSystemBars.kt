package com.syndicate.ptkscheduleapp.presentation.utils

import android.content.Context
import android.graphics.Color
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import com.syndicate.ptkscheduleapp.ui.findActivity
import com.syndicate.ptkscheduleapp.ui.theme.utils.ThemeMode

fun setupSystemBars(
    context: Context,
    currentThemeMode: ThemeMode,
    isSplashScreen: Boolean
) {

    context.findActivity()?.enableEdgeToEdge(
        statusBarStyle = when {
            currentThemeMode == ThemeMode.LIGHT ||
                    (currentThemeMode == ThemeMode.CAPPUCCINO && !isSplashScreen) -> SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            )
            else -> SystemBarStyle.dark(
                scrim = Color.TRANSPARENT
            )
        },
        navigationBarStyle = when {
            currentThemeMode == ThemeMode.LIGHT ||
                    (currentThemeMode == ThemeMode.CAPPUCCINO && !isSplashScreen) -> SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            )
            else -> SystemBarStyle.dark(
                scrim = Color.TRANSPARENT
            )
        }
    )
}