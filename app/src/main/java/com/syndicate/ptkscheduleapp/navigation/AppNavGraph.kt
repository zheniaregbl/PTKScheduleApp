package com.syndicate.ptkscheduleapp.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.syndicate.ptkscheduleapp.data.model.MainState
import com.syndicate.ptkscheduleapp.data.model.PanelState
import com.syndicate.ptkscheduleapp.navigation.utils.getCurrentRoute
import com.syndicate.ptkscheduleapp.ui.bottom_navigation_bar.BottomMenu
import com.syndicate.ptkscheduleapp.view_model.app_view_model.MainViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    state: MainState,
    viewModel: MainViewModel
) {
    val routeList = listOf(
        ScreenRoute.ScheduleScreen.route,
        ScreenRoute.SettingScreen.route
    )

    val showNavigationMenu = navController
        .currentBackStackEntryAsState().value?.destination?.route in routeList.map { it }

    val topDatePanelState = remember {
        mutableStateOf(PanelState.WeekPanel)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars,
        bottomBar = {
            if (showNavigationMenu)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    BottomMenu(
                        modifier = Modifier
                            .fillMaxWidth(),
                        navController = navController,
                        panelState = topDatePanelState,
                        currentRoute = getCurrentRoute(navController = navController)
                    )
                    Spacer(
                        modifier = Modifier
                            .navigationBarsPadding()
                    )
                }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.onPrimary
        ) {
            AppNavigation(
                navController = navController,
                state = state,
                panelState = topDatePanelState,
                viewModel = viewModel,
                paddingValues = paddingValues
            )
        }
    }
}