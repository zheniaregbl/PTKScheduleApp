package com.syndicate.ptkscheduleapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import com.syndicate.ptkscheduleapp.data.model.MainState
import com.syndicate.ptkscheduleapp.data.model.UserMode
import com.syndicate.ptkscheduleapp.ui.screens.course_selection_screen.CourseSelectionScreen
import com.syndicate.ptkscheduleapp.ui.screens.group_selection_screen.GroupSelectionScreen
import com.syndicate.ptkscheduleapp.ui.screens.role_selection_screen.RoleSelectionScreen
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.ScheduleScreen
import com.syndicate.ptkscheduleapp.ui.screens.splash_screen.SplashScreen
import com.syndicate.ptkscheduleapp.view_model.ScheduleEvent
import com.syndicate.ptkscheduleapp.view_model.ScheduleViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    state: MainState,
    scheduleList: List<List<LessonItem>>?,
    viewModel: ScheduleViewModel,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.RoleSelectionScreen.route
    ) {
        composable(
            route = ScreenRoute.SplashScreen.route
        ) {
            SplashScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(
                        top = 10.dp
                    ),
                navigateToNext = {
                    navController.navigate(ScreenRoute.RoleSelectionScreen.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(
            route = ScreenRoute.RoleSelectionScreen.route
        ) {
            RoleSelectionScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                navigateToNext = { userMode ->
                    if (userMode == UserMode.Student) {
                        navController.navigate(ScreenRoute.CourseSelectionScreen.route) {
                            popUpTo(0)
                        }
                    } else {
                        navController.navigate(ScreenRoute.GroupSelectionScreen.route) {
                            popUpTo(0)
                        }
                    }
                },
                changeUserMode = { userMode ->
                    viewModel.onEvent(ScheduleEvent.ChangeUserMode(userMode))
                }
            )
        }
        composable(
            route = ScreenRoute.CourseSelectionScreen.route
        ) {
            CourseSelectionScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                navigateToNext = {
                    navController.navigate(ScreenRoute.GroupSelectionScreen.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(
            route = ScreenRoute.GroupSelectionScreen.route
        ) {
            GroupSelectionScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                navigateToNext = {
                    navController.navigate(ScreenRoute.ScheduleScreen.route) {
                        popUpTo(0)
                    }
                },
                userMode = state.userMode
            )
        }
        composable(
            route = ScreenRoute.ScheduleScreen.route
        ) {
            ScheduleScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(
                        top = 10.dp
                    ),
                isUpperWeek = state.isUpperWeek,
                scheduleList = scheduleList
            )
        }
    }
}