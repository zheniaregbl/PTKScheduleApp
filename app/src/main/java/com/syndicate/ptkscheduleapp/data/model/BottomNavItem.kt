package com.syndicate.ptkscheduleapp.data.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector

@Stable
data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)
