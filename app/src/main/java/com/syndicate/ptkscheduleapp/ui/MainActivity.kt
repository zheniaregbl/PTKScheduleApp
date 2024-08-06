package com.syndicate.ptkscheduleapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.syndicate.ptkscheduleapp.extension.colorsPalette
import com.syndicate.ptkscheduleapp.presentation.screens.NavGraphs
import com.syndicate.ptkscheduleapp.presentation.utils.CirclePath
import com.syndicate.ptkscheduleapp.ui.theme.AppTheme
import com.syndicate.ptkscheduleapp.presentation.utils.LockScreenOrientation
import com.syndicate.ptkscheduleapp.view_model.app_view_model.AppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val appViewModel = hiltViewModel<AppViewModel>()
            val appThemeMode by appViewModel.themeModeState.collectAsState()
            val animationOffset by appViewModel.animationOffset.collectAsState()
            val themeState = remember { mutableStateOf(appThemeMode) }
            val navController = rememberNavController()

            AppTheme(
                themeMode = appThemeMode
            ) {

                val revealSize = remember { Animatable(1f) }

                LaunchedEffect("reveal") {
                    if (animationOffset.x > 0f) {
                        revealSize.snapTo(0f)
                        revealSize.animateTo(1f, animationSpec = tween(800))
                    } else {
                        revealSize.snapTo(1f)
                    }
                }

                LockScreenOrientation(
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                )

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CirclePath(revealSize.value, animationOffset)),
                    color = MaterialTheme.colorsPalette.backgroundColor
                ) {

                    DestinationsNavHost(
                        navController = navController,
                        navGraph = NavGraphs.root,
                        dependenciesContainerBuilder = {
                            dependency(themeState)
                            dependency(appViewModel)
                        }
                    )
                }
            }
        }
    }
}

internal tailrec fun Context.findActivity(): ComponentActivity? =
    when (this) {
        is ComponentActivity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }