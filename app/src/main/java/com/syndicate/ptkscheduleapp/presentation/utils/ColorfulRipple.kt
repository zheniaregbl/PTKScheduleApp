package com.syndicate.ptkscheduleapp.presentation.utils

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class ColorfulRipple(private val color: Color): RippleTheme {
    @Composable
    override fun defaultColor() = color

    @Composable
    override fun rippleAlpha() = RippleAlpha(
        draggedAlpha = 0.1f,
        focusedAlpha = 0.1f,
        hoveredAlpha = 0.1f,
        pressedAlpha = 0.1f
    )
}