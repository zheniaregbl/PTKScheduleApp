package com.syndicate.ptkscheduleapp.view_model.main_view_model

import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.syndicate.ptkscheduleapp.ui.theme.utils.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor() : ViewModel() {

    private val _themeModeState = MutableStateFlow(ThemeMode.LIGHT)
    val themeModeState = _themeModeState.asStateFlow()

    private val _animationOffset = MutableStateFlow(Offset(0f, 0f))
    val animationOffset = _animationOffset.asStateFlow()

    private val _enableChangeTheme = MutableStateFlow(true)
    val enableChangeTheme = _enableChangeTheme.asStateFlow()

    @OptIn(ExperimentalMaterialApi::class)
    private val _initialSheetValue = MutableStateFlow(BottomSheetValue.Collapsed)
    @OptIn(ExperimentalMaterialApi::class)
    val initialSheetValue = _initialSheetValue.asStateFlow()

    @OptIn(ExperimentalMaterialApi::class)
    fun onEvent(event: AppEvent) {

        when (event) {
            is AppEvent.ChangeTheme -> {
                _themeModeState.update { event.themeMode }
                _animationOffset.update { event.offset }
            }

            is AppEvent.ChangeSheetState -> _initialSheetValue.update { event.sheetState }

            AppEvent.LockChangeTheme -> _enableChangeTheme.update { false }

            AppEvent.UnlockChangeTheme -> _enableChangeTheme.update { true }
        }
    }
}