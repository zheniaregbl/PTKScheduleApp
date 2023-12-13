package com.syndicate.ptkscheduleapp

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.syndicate.ptkscheduleapp.domain.repository.ScheduleRepository
import com.syndicate.ptkscheduleapp.navigation.AppNavGraph
import com.syndicate.ptkscheduleapp.ui.theme.PTKScheduleAppTheme
import com.syndicate.ptkscheduleapp.ui.theme.ThemeMode
import com.syndicate.ptkscheduleapp.ui.utils.LockScreenOrientation
import com.syndicate.ptkscheduleapp.view_model.app_view_model.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val viewModel = hiltViewModel<MainViewModel>()
            val state by viewModel.state.collectAsState()
            val firstVisitSchedule by viewModel.firstVisitSchedule.collectAsState()

            PTKScheduleAppTheme(
                themeMode = state.colorThemeMode,
                darkTheme = when (state.colorThemeMode) {
                    ThemeMode.FIRST, ThemeMode.SECOND -> false
                    ThemeMode.THIRD, ThemeMode.FOURTH -> true
                }
            ) {

                LockScreenOrientation(
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                )

                AppNavGraph(
                    state = state,
                    firstVisitSchedule = firstVisitSchedule,
                    viewModel = viewModel
                )
            }
        }
    }
}