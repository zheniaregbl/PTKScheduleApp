package com.syndicate.ptkscheduleapp.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.syndicate.ptkscheduleapp.data.model.MainState
import com.syndicate.ptkscheduleapp.data.model.PanelState
import com.syndicate.ptkscheduleapp.data.model.UserMode
import com.syndicate.ptkscheduleapp.ui.screens.course_selection_screen.CourseSelectionScreen
import com.syndicate.ptkscheduleapp.ui.screens.group_selection_screen.GroupSelectionScreen
import com.syndicate.ptkscheduleapp.ui.screens.role_selection_screen.RoleSelectionScreen
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.ScheduleScreen
import com.syndicate.ptkscheduleapp.ui.screens.setting_screen.SettingScreen
import com.syndicate.ptkscheduleapp.ui.screens.splash_screen.SplashScreen
import com.syndicate.ptkscheduleapp.ui.screens.theme_screen.ThemeScreen
import com.syndicate.ptkscheduleapp.ui.theme.ThemeMode
import com.syndicate.ptkscheduleapp.view_model.app_view_model.MainEvent
import com.syndicate.ptkscheduleapp.view_model.app_view_model.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    navController: NavHostController,
    state: MainState,
    firstVisitSchedule: Boolean,
    panelState: MutableState<PanelState>,
    viewModel: MainViewModel,
    paddingValues: PaddingValues
) {
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = ScreenRoute.SplashScreen.route
    ) {

        composable(
            route = ScreenRoute.SplashScreen.route,
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(800))
            }
        ) {
            SplashScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(
                        top = 10.dp
                    ),
                isFirstStart = state.isFirstStart,
                navigateToRole = {
                    navController.navigate(ScreenRoute.RoleSelectionScreen.route) {
                        popUpTo(0)
                    }
                },
                navigateToSchedule = {
                    viewModel.onEvent(MainEvent.ReceiveScheduleFromServer)

                    navController.navigate(ScreenRoute.ScheduleScreen.route) {
                        popUpTo(0)
                    }
                },
                userThemeMode = state.colorThemeMode
            )
        }

        composable(
            route = ScreenRoute.RoleSelectionScreen.route,
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400))
            }
        ) {
            RoleSelectionScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                navigateToNext = { userMode ->
                    if (userMode == UserMode.Student) {
                        navController.navigate(ScreenRoute.CourseSelectionScreen.route)
                    } else {
                        navController.navigate(ScreenRoute.GroupSelectionScreen.route)
                    }
                },
                changeUserMode = { userMode ->
                    viewModel.onEvent(MainEvent.ChangeUserMode(userMode))
                }
            )
        }

        composable(
            route = ScreenRoute.CourseSelectionScreen.route,
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400))
            }
        ) {
            CourseSelectionScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                navigateToNext = { userCourse ->
                    viewModel.onEvent(MainEvent.ChangeUserCourse(userCourse))

                    navController.navigate(ScreenRoute.GroupSelectionScreen.route)
                }
            )
        }

        composable(
            route = ScreenRoute.GroupSelectionScreen.route,
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400))
            }
        ) {
            GroupSelectionScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                navigateToNext = {

                    scope.launch(Dispatchers.Main) {

                        viewModel.onEvent(MainEvent.ReceiveScheduleFromServer)
                        viewModel.onEvent(MainEvent.CreateConfiguration)

                        delay(1500)

                        navController.navigate(ScreenRoute.ScheduleScreen.route) {
                            popUpTo(0)
                        }
                    }
                },
                changeUserGroup = { group ->
                    viewModel.onEvent(MainEvent.ChangeUserGroup(group))
                },
                userMode = state.userMode
            )
        }

        composable(
            route = ScreenRoute.ScheduleScreen.route,
            enterTransition = {
                fadeIn(animationSpec = tween(800))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400))
            }
        ) {
            ScheduleScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                firstVisitSchedule = firstVisitSchedule,
                onFirstVisit = {
                    viewModel.onEvent(MainEvent.FirstVisitSchedule)
                },
                panelState = panelState,
                isUpperWeek = state.isUpperWeek,
                isDarkTheme = when (state.colorThemeMode) {
                    ThemeMode.FIRST, ThemeMode.SECOND -> false
                    ThemeMode.THIRD, ThemeMode.FOURTH -> true
                }
            )
        }

        composable(
            route = ScreenRoute.SettingScreen.route,
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400))
            }
        ) {
            SettingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                isDarkTheme = when (state.colorThemeMode) {
                    ThemeMode.FIRST, ThemeMode.SECOND -> false
                    ThemeMode.THIRD, ThemeMode.FOURTH -> true
                },
                navigateToTheme = {
                    navController.navigate(ScreenRoute.ThemeScreen.route)
                },
                navigateToReselectGroup = {
                    navController.navigate(ScreenRoute.CourseSelectionScreen.route)
                }
            )
        }

        composable(
            route = ScreenRoute.ThemeScreen.route,
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400))
            }
        ) {
            ThemeScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                navigateToBack = {
                    navController.navigate(ScreenRoute.SettingScreen.route) {
                        popUpTo(0)
                    }
                },
                changeTheme = { newThemeMode ->
                    viewModel.onEvent(MainEvent.ChangeAppTheme(newThemeMode))
                },
                userThemeMode = state.colorThemeMode
            )
        }
    }
}