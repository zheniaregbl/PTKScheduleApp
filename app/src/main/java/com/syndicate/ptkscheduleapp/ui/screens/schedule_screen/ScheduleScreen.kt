package com.syndicate.ptkscheduleapp.ui.screens.schedule_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components.LessonCard
import com.syndicate.ptkscheduleapp.ui.theme.FirstThemeBackground

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LessonCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(10.dp),
                        clip = false,
                        ambientColor = Color.Black.copy(alpha = 0.15f)
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        color = FirstThemeBackground
                    ),
                lessonItem = LessonItem()
            )
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            LessonCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(10.dp),
                        clip = false,
                        ambientColor = Color.Black.copy(alpha = 0.15f)
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        color = FirstThemeBackground
                    ),
                lessonList = listOf(
                    LessonItem(),
                    LessonItem()
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScheduleScreen() {
    ScheduleScreen()
}