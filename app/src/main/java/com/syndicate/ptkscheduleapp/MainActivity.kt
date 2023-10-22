package com.syndicate.ptkscheduleapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.syndicate.ptkscheduleapp.domain.repository.ScheduleRepository
import com.syndicate.ptkscheduleapp.ui.screens.course_selection_screen.CourseSelectionScreen
import com.syndicate.ptkscheduleapp.ui.screens.group_selection_screen.GroupSelectionScreen
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.ScheduleScreen
import com.syndicate.ptkscheduleapp.ui.screens.splash_screen.SplashScreen
import com.syndicate.ptkscheduleapp.ui.theme.PTKScheduleAppTheme
import com.syndicate.ptkscheduleapp.view_model.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var repository: ScheduleRepository

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val viewModel = hiltViewModel<ScheduleViewModel>()
            val state by viewModel.state.collectAsState()
            val scheduleList = viewModel.scheduleList.observeAsState()

            PTKScheduleAppTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentWindowInsets = WindowInsets.systemBars
                ) { paddingValues ->
                    /*CourseSelectionScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            .padding(paddingValues)
                    )*/
                    /*SplashScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            .padding(paddingValues)
                    )*/
                    ScheduleScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            .padding(paddingValues),
                        isUpperWeek = state.isUpperWeek,
                        scheduleList = scheduleList.value
                    )
                    /*GroupSelectionScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            .padding(paddingValues)
                    )*/
                }
            }
        }
    }
}