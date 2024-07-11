package com.syndicate.ptkscheduleapp.ui.theme

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.syndicate.ptkscheduleapp.ui.theme.utils.CappuccinoColorPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.DarkColorPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.GrayColorPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.LightColorPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.LocalColorsPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.ThemeMode

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppTheme(
    themeMode: ThemeMode = ThemeMode.LIGHT,
    content: @Composable () -> Unit
) {

    val colorsPalette = when (themeMode) {
        ThemeMode.LIGHT -> LightColorPalette
        ThemeMode.CAPPUCCINO -> CappuccinoColorPalette
        ThemeMode.GRAY -> GrayColorPalette
        ThemeMode.DARK -> DarkColorPalette
    }

    AnimatedContent(
        modifier = Modifier
            .background(
                color = Color.Black
            ),
        targetState = colorsPalette,
        transitionSpec = {
            fadeIn(
                initialAlpha = 0f,
                animationSpec = tween(100)
            ) togetherWith  fadeOut(
                targetAlpha = .9f,
                animationSpec = tween(800)
            )
        },
        label = "Theme animation"
    ) { currentPalette ->

        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null,
            LocalColorsPalette provides currentPalette
        ) {

            MaterialTheme(
                colorScheme = lightColorScheme(),
                typography = Typography,
                content = content
            )
        }
    }
}