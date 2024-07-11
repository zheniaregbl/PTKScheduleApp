package com.syndicate.ptkscheduleapp.ui.theme.utils

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.syndicate.ptkscheduleapp.ui.theme.FirstThemeBackground
import com.syndicate.ptkscheduleapp.ui.theme.FourthThemeBackground
import com.syndicate.ptkscheduleapp.ui.theme.GrayThirdTheme
import com.syndicate.ptkscheduleapp.ui.theme.SecondThemeBackground
import com.syndicate.ptkscheduleapp.ui.theme.ThirdThemeBackground

data class ColorsPalette(
    val backgroundColor: Color = FirstThemeBackground,
    val contentColor: Color = Color.Black,
    val otherColor: Color = SecondThemeBackground,
    val themeMode: ThemeMode = ThemeMode.LIGHT
)

val LightColorPalette = ColorsPalette()

val CappuccinoColorPalette = ColorsPalette(
    backgroundColor = SecondThemeBackground,
    contentColor = Color.Black,
    otherColor = FirstThemeBackground,
    themeMode = ThemeMode.CAPPUCCINO
)

val GrayColorPalette = ColorsPalette(
    backgroundColor = ThirdThemeBackground,
    contentColor = Color.White,
    otherColor = GrayThirdTheme,
    themeMode = ThemeMode.GRAY
)

val DarkColorPalette = ColorsPalette(
    backgroundColor = FourthThemeBackground,
    contentColor = Color.White,
    otherColor = ThirdThemeBackground,
    themeMode = ThemeMode.DARK
)

val LocalColorsPalette = staticCompositionLocalOf { ColorsPalette() }