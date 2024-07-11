package com.syndicate.ptkscheduleapp.view_model.main_view_model

import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.geometry.Offset
import com.syndicate.ptkscheduleapp.ui.theme.utils.ThemeMode

sealed interface AppEvent {

    data class ChangeTheme(
        val themeMode: ThemeMode,
        val offset: Offset
    ) : AppEvent

    data object LockChangeTheme : AppEvent

    data object UnlockChangeTheme : AppEvent

    data class ChangeSheetState @OptIn(ExperimentalMaterialApi::class) constructor(
        val sheetState: BottomSheetValue
    ) : AppEvent
}