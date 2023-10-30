package com.syndicate.ptkscheduleapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.syndicate.ptkscheduleapp.domain.repository.ScheduleRepository
import com.syndicate.ptkscheduleapp.navigation.AppNavGraph
import com.syndicate.ptkscheduleapp.ui.theme.PTKScheduleAppTheme
import com.syndicate.ptkscheduleapp.view_model.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var repository: ScheduleRepository

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val viewModel = hiltViewModel<ScheduleViewModel>()
            val state by viewModel.state.collectAsState()
            val scheduleList = viewModel.scheduleList.observeAsState()

            PTKScheduleAppTheme {
                AppNavGraph(
                    state = state,
                    scheduleList = scheduleList.value,
                    viewModel = viewModel
                )
            }
        }
    }
}