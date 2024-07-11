package com.syndicate.ptkscheduleapp.presentation.screens.splash_screen

import android.graphics.Color as AndroidColor
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.syndicate.ptkscheduleapp.R
import com.syndicate.ptkscheduleapp.extension.colorsPalette
import com.syndicate.ptkscheduleapp.presentation.screens.destinations.CourseScreenDestination
import com.syndicate.ptkscheduleapp.presentation.screens.destinations.ScheduleScreenDestination
import com.syndicate.ptkscheduleapp.presentation.screens.destinations.SplashScreenDestination
import com.syndicate.ptkscheduleapp.presentation.utils.FadeTransitions
import com.syndicate.ptkscheduleapp.presentation.utils.setupSystemBars
import com.syndicate.ptkscheduleapp.ui.findActivity
import com.syndicate.ptkscheduleapp.ui.theme.AppTheme
import com.syndicate.ptkscheduleapp.ui.theme.GrayThirdTheme
import com.syndicate.ptkscheduleapp.ui.theme.MainBlue
import com.syndicate.ptkscheduleapp.ui.theme.utils.LocalColorsPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.ThemeMode
import kotlinx.coroutines.delay

@RootNavGraph(
    start = true
)
@Destination(
    style = FadeTransitions::class
)
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator
) {

    val currentThemeMode = LocalColorsPalette.current.themeMode

    setupSystemBars(
        LocalContext.current,
        currentThemeMode,
        isSplashScreen = true
    )

    SplashScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (currentThemeMode == ThemeMode.CAPPUCCINO) MainBlue else ComposeColor.Transparent
            )
            .systemBarsPadding(),
        themeMode = currentThemeMode,
        onLaunchApp = {
            navigator.navigate(ScheduleScreenDestination) {
                popUpTo(SplashScreenDestination.route) {
                    inclusive = true
                }
            }
        }
    )
}

@Composable
fun SplashScreenContent(
    modifier: Modifier = Modifier,
    themeMode: ThemeMode = ThemeMode.LIGHT,
    onLaunchApp: () -> Unit = { }
) {

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            resId = when (themeMode) {
                ThemeMode.LIGHT -> R.raw.yaroslav_lottie_blue
                ThemeMode.CAPPUCCINO, ThemeMode.DARK -> R.raw.yaroslav_lottie_white
                ThemeMode.GRAY -> R.raw.yaroslav_lottie_gray
            }
        )
    )
    var isPlaying by remember {
        mutableStateOf(true)
    }
    var isShowText by remember {
        mutableStateOf(false)
    }
    val progress by animateLottieCompositionAsState(
        composition =  composition,
        isPlaying = isPlaying,
        iterations = 1,
        speed = 0.85f
    )

    LaunchedEffect(key1 = progress) {
        if (progress == 1f) {

            isPlaying = false
            isShowText = true

            delay(1000L)

            onLaunchApp()
        }
    }

    Box(
        modifier = modifier
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                modifier = Modifier
                    .size(166.dp),
                composition = composition,
                progress = { progress }
            )
        }

        AnimatedVisibility(
            label = "devsText",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    bottom = 50.dp
                ),
            visible = isShowText,
            enter = fadeIn(
                animationSpec = tween(500)
            )
        ) {

            Text(
                text = "by syndicate",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = when (themeMode) {
                    ThemeMode.LIGHT -> MainBlue
                    ThemeMode.CAPPUCCINO, ThemeMode.DARK -> ComposeColor.White
                    ThemeMode.GRAY -> GrayThirdTheme
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSplashScreenLight() {
    AppTheme {
        SplashScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorsPalette.backgroundColor
                )
        )
    }
}

@Preview
@Composable
private fun PreviewSplashScreenCappuccino() {
    AppTheme(
        themeMode = ThemeMode.CAPPUCCINO
    ) {
        SplashScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorsPalette.backgroundColor
                )
        )
    }
}

@Preview
@Composable
private fun PreviewSplashScreenGray() {
    AppTheme(
        themeMode = ThemeMode.GRAY
    ) {
        SplashScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorsPalette.backgroundColor
                )
        )
    }
}

@Preview
@Composable
private fun PreviewSplashScreenDark() {
    AppTheme(
        themeMode = ThemeMode.DARK
    ) {
        SplashScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorsPalette.backgroundColor
                )
        )
    }
}