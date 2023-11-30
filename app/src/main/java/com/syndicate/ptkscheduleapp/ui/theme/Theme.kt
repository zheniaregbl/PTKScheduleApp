package com.syndicate.ptkscheduleapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val firstTheme = lightColorScheme(
    onPrimary = FirstThemeBackground,
    inversePrimary = SecondThemeBackground
)

private val secondTheme = lightColorScheme(
    onPrimary = SecondThemeBackground,
    inversePrimary = FirstThemeBackground
)

private val thirdTheme = darkColorScheme(
    onPrimary = ThirdThemeBackground,
    inversePrimary = GrayThirdTheme
)

private val fourthTheme = darkColorScheme(
    onPrimary = FourthThemeBackground,
    inversePrimary = ThirdThemeBackground
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PTKScheduleAppTheme(
    themeMode: ThemeMode = ThemeMode.FIRST,
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        ThemeMode.FIRST -> firstTheme
        ThemeMode.SECOND -> secondTheme
        ThemeMode.THIRD -> thirdTheme
        ThemeMode.FOURTH -> fourthTheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = colorScheme.onPrimary.toArgb()

            WindowCompat
                .getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
            WindowCompat
                .getInsetsController(window, view)
                .isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null,
            content = content
        )
    }
}