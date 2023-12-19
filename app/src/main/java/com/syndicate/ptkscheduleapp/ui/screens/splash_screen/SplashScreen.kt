package com.syndicate.ptkscheduleapp.ui.screens.splash_screen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.syndicate.ptkscheduleapp.R
import com.syndicate.ptkscheduleapp.ui.theme.GrayThirdTheme
import com.syndicate.ptkscheduleapp.ui.theme.MainBlue
import com.syndicate.ptkscheduleapp.ui.theme.ThemeMode
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    isFirstStart: Boolean = true,
    userThemeMode: ThemeMode = ThemeMode.FIRST,
    navigateToCourse: () -> Unit = { },
    navigateToSchedule: () -> Unit = { }
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            resId = when (userThemeMode) {
                ThemeMode.FIRST -> R.raw.yaroslav_lottie_blue
                ThemeMode.SECOND, ThemeMode.FOURTH -> R.raw.yaroslav_lottie_white
                ThemeMode.THIRD -> R.raw.yaroslav_lottie_gray
            }
        )
    )
    var isPlaying by remember {
        mutableStateOf(true)
    }
    val showDevs = remember {
        mutableStateOf("")
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
            showDevs.value = "from syndicate"

            delay(1000L)

            if (isFirstStart) navigateToCourse() else navigateToSchedule()
        }
    }

    Box(
        modifier = Modifier
            .background(
                color = if (userThemeMode == ThemeMode.SECOND) MainBlue else Color.Transparent
            )
            .composed { modifier }
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
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    50.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Crossfade(
                targetState = showDevs.value,
                animationSpec = tween(500),
                label = ""
            ) { showDevs ->
                Text(
                    text = showDevs,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = when (userThemeMode) {
                        ThemeMode.FIRST -> MainBlue
                        ThemeMode.SECOND, ThemeMode.FOURTH -> Color.White
                        ThemeMode.THIRD -> GrayThirdTheme
                    }
                )
            }
        }
    }
}