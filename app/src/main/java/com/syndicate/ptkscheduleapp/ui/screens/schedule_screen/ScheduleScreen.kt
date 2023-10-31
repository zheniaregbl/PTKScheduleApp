package com.syndicate.ptkscheduleapp.ui.screens.schedule_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.syndicate.ptkscheduleapp.info_functions.filterSchedule
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components.LessonCard
import com.syndicate.ptkscheduleapp.ui.theme.FirstThemeBackground
import java.util.Calendar

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    isUpperWeek: Boolean = true,
    scheduleList: List<List<LessonItem>>? = listOf(
        listOf(
            LessonItem(), LessonItem(), LessonItem(), LessonItem()
        ),
        listOf(
            LessonItem(), LessonItem(), LessonItem(), LessonItem()
        ),
        listOf(
            LessonItem(), LessonItem(), LessonItem(), LessonItem()
        ),
        listOf(
            LessonItem(), LessonItem(), LessonItem(), LessonItem()
        ),
        listOf(
            LessonItem(), LessonItem(), LessonItem(), LessonItem()
        ),
        listOf(
            LessonItem(), LessonItem(), LessonItem(), LessonItem()
        )
    )
) {
    val calendar = Calendar.getInstance()
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    val list = scheduleList
        ?: listOf(
            listOf(
                LessonItem(),
                LessonItem(
                    subgroupNumber = 1
                ),
                LessonItem(
                    subgroupNumber = 2
                ),
                LessonItem(
                    subgroupNumber = 3
                ),
                LessonItem(
                    subgroupNumber = 4
                ),
                LessonItem(),
            ),
            listOf(
                LessonItem(), LessonItem(), LessonItem(), LessonItem()
            ),
            listOf(
                LessonItem(), LessonItem(), LessonItem(), LessonItem()
            ),
            listOf(
                LessonItem(), LessonItem(), LessonItem(), LessonItem()
            ),
            listOf(
                LessonItem(), LessonItem(), LessonItem(), LessonItem()
            ),
            listOf(
                LessonItem(), LessonItem(), LessonItem(), LessonItem()
            )
        )

    val currentSchedule = when (dayOfWeek) {
        Calendar.MONDAY -> filterSchedule(list[0], isUpperWeek)
        Calendar.TUESDAY -> filterSchedule(list[1], isUpperWeek)
        Calendar.WEDNESDAY -> filterSchedule(list[2], isUpperWeek)
        Calendar.THURSDAY -> filterSchedule(list[3], isUpperWeek)
        Calendar.FRIDAY -> filterSchedule(list[4], isUpperWeek)
        Calendar.SATURDAY -> filterSchedule(list[5], isUpperWeek)
        else -> filterSchedule(list[0], isUpperWeek)
    }

    var listSeveralLessons = ArrayList<LessonItem>()
    var prevLessonNumber = -1

    Box(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            itemsIndexed(currentSchedule) { index, item ->

                if (index != 0) prevLessonNumber = currentSchedule[index - 1].pairNumber

                if (item.lessonTitle != "") {
                    if (item.subgroupNumber == 0) {
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
                            lessonItem = item
                        )

                        if (index != currentSchedule.lastIndex)
                            Spacer(
                                modifier = Modifier
                                    .height(20.dp)
                            )
                    } else {
                        listSeveralLessons.add(item)

                        if (index != currentSchedule.lastIndex && currentSchedule[index + 1].subgroupNumber == 0
                            || index == currentSchedule.lastIndex && currentSchedule.isNotEmpty()
                            || index != 0 && prevLessonNumber == item.pairNumber) {
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
                                lessonList = listSeveralLessons
                            )

                            if (index != currentSchedule.lastIndex)
                                Spacer(
                                    modifier = Modifier
                                        .height(20.dp)
                                )

                            listSeveralLessons = ArrayList()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScheduleScreen() {
    ScheduleScreen()
}