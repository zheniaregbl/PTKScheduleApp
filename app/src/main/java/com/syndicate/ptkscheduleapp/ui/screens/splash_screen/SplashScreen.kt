package com.syndicate.ptkscheduleapp.ui.screens.splash_screen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.syndicate.ptkscheduleapp.R
import com.syndicate.ptkscheduleapp.ui.theme.MainBlue

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resId = R.raw.yaroslav_lottie)
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
        speed = 0.5f
    )

    LaunchedEffect(key1 = progress) {
        if (progress == 1f) {
            isPlaying = false
            showDevs.value = "from syndicate"
        }

    }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 344.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                modifier = Modifier
                    .size(166.dp),
                composition = composition,
                progress = { progress }
            )
            Spacer(
                modifier = Modifier
                    .height(200.dp)
            )
            Crossfade(
                targetState = showDevs.value,
                animationSpec = tween(250),
                label = ""
            ) { showDevs ->
                Text(
                    text = showDevs,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MainBlue
                )
            }
        }
    }
}