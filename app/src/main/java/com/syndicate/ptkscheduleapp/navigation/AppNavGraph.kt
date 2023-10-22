package com.syndicate.ptkscheduleapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import com.syndicate.ptkscheduleapp.data.model.MainState
import com.syndicate.ptkscheduleapp.view_model.ScheduleViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    state: MainState,
    scheduleList: List<List<LessonItem>>?,
    viewModel: ScheduleViewModel
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.onPrimary
            ),
        contentWindowInsets = WindowInsets.systemBars
    ) { paddingValues ->
        AppNavigation(
            navController = navController,
            state = state,
            scheduleList = scheduleList,
            viewModel = viewModel,
            paddingValues = paddingValues
        )
    }
}