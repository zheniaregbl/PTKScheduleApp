package com.syndicate.ptkscheduleapp

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
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.syndicate.ptkscheduleapp.ui.screens.course_selection_screen.CourseSelectionScreen
import com.syndicate.ptkscheduleapp.ui.theme.PTKScheduleAppTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            PTKScheduleAppTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentWindowInsets = WindowInsets.systemBars
                ) { paddingValues ->
                    CourseSelectionScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            .padding(paddingValues)
                    )
                }
            }
        }
    }
}