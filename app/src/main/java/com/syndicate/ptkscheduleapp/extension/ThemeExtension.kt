package com.syndicate.ptkscheduleapp.extension

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.syndicate.ptkscheduleapp.ui.theme.utils.CappuccinoColorPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.ColorsPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.DarkColorPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.GrayColorPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.LightColorPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.LocalColorsPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.ThemeMode

val MaterialTheme.colorsPalette: ColorsPalette
    @Composable
    @ReadOnlyComposable
    get() = LocalColorsPalette.current

val ThemeMode.colorPalette: ColorsPalette
    get() = when (this) {
        ThemeMode.LIGHT -> LightColorPalette
        ThemeMode.CAPPUCCINO -> CappuccinoColorPalette
        ThemeMode.GRAY -> GrayColorPalette
        ThemeMode.DARK -> DarkColorPalette
    }

