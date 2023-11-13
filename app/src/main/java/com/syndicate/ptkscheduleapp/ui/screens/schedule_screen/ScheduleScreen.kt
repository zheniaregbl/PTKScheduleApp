package com.syndicate.ptkscheduleapp.ui.screens.schedule_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import com.syndicate.ptkscheduleapp.data.model.PanelState
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components.LessonCard
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components.TopDatePanel
import com.syndicate.ptkscheduleapp.ui.theme.FirstThemeBackground
import com.syndicate.ptkscheduleapp.ui.theme.ThirdThemeBackground
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components.ShimmerItem
import com.syndicate.ptkscheduleapp.view_model.schedule_screen_view_model.ScheduleEvent
import com.syndicate.ptkscheduleapp.view_model.schedule_screen_view_model.ScheduleViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    isUpperWeek: Boolean = true
) {
    val viewModel = hiltViewModel<ScheduleViewModel>()
    val scheduleList = viewModel.currentSchedule.observeAsState()
    val currentSchedule = if (scheduleList.value.isNullOrEmpty()) {
        emptyList()
    } else scheduleList.value!!

    val topDatePanelState = remember {
        mutableStateOf(PanelState.WeekPanel)
    }
    val selectedDateState = remember {
        mutableStateOf(LocalDate.now())
    }

    var listSeveralLessons = ArrayList<LessonItem>()
    var prevLessonNumber = -1

    LaunchedEffect(Unit) {
        delay(300)
        viewModel.onEvent(ScheduleEvent.ChangeSchedule(LocalDate.now().dayOfWeek, isUpperWeek))
    }

    Box(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                }
            ) {
                if (topDatePanelState.value == PanelState.CalendarPanel)
                    topDatePanelState.value = PanelState.WeekPanel
            }
            .composed { modifier }
    ) {
        AnimatedContent(
            targetState = currentSchedule,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = ""
        ) { currentSchedule ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 16.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                    Spacer(
                        modifier = Modifier
                            .height(140.dp)
                    )
                }

                if (currentSchedule.isNotEmpty()) {

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
                                            clip = true,
                                            spotColor = Color.Black.copy(alpha = 0.3f),
                                            ambientColor = Color.Black.copy(alpha = 0.3f)
                                        )
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            color = FirstThemeBackground
                                        )
                                        .border(
                                            width = 2.dp,
                                            color = ThirdThemeBackground,
                                            shape = RoundedCornerShape(10.dp)
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
                                                clip = true,
                                                spotColor = Color.Black.copy(alpha = 0.3f),
                                                ambientColor = Color.Black.copy(alpha = 0.3f)
                                            )
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(
                                                color = FirstThemeBackground
                                            )
                                            .border(
                                                width = 2.dp,
                                                color = ThirdThemeBackground,
                                                shape = RoundedCornerShape(10.dp)
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
                    
                } else {

                    items(4) { index ->

                        ShimmerItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .size(80.dp)
                        )
                        if (index != 3)
                            Spacer(
                                modifier = Modifier
                                    .height(20.dp)
                            )
                    }
                }

                item {
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                    )
                }
            }
        }

        TopDatePanel(
            modifier = Modifier
                .fillMaxSize(),
            panelState = topDatePanelState,
            selectedDateState = selectedDateState,
            weekType = isUpperWeek,
            changeSchedule = { dayOfWeek, typeWeek ->
                viewModel.onEvent(ScheduleEvent.ChangeSchedule(dayOfWeek, typeWeek))
            },
            hideCalendar = {
                if (topDatePanelState.value == PanelState.CalendarPanel) {
                    topDatePanelState.value = PanelState.WeekPanel
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScheduleScreen() {
    ScheduleScreen()
}