package com.syndicate.ptkscheduleapp.ui.screens.schedule_screen

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import com.syndicate.ptkscheduleapp.data.model.PanelState
import com.syndicate.ptkscheduleapp.data.model.ShowScheduleState
import com.syndicate.ptkscheduleapp.info_functions.applyReplacementSchedule
import com.syndicate.ptkscheduleapp.info_functions.deleteEmptyLesson
import com.syndicate.ptkscheduleapp.info_functions.fillListReplacementNumber
import com.syndicate.ptkscheduleapp.info_functions.isNetworkAvailable
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components.LessonCard
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components.ReplacementDialog
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components.TopDatePanel
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components.ShimmerItem
import com.syndicate.ptkscheduleapp.ui.screens.setting_screen.components.NetworkConnectionDialogWithRetry
import com.syndicate.ptkscheduleapp.view_model.schedule_screen_view_model.ScheduleEvent
import com.syndicate.ptkscheduleapp.view_model.schedule_screen_view_model.ScheduleViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    firstVisitSchedule: Boolean = false,
    onFirstVisit: () -> Unit = { },
    panelState: MutableState<PanelState> = mutableStateOf(PanelState.WeekPanel),
    isUpperWeek: Boolean = true,
    isDarkTheme: Boolean = false
) {

    val context = LocalContext.current

    var showScheduleState by remember {
        mutableStateOf(ShowScheduleState.LOADING)
    }

    val viewModel = hiltViewModel<ScheduleViewModel>()
    val scheduleList = viewModel.currentSchedule.observeAsState()
    val replacement = viewModel.dayReplacement.observeAsState()

    val scheduleWithReplacement = applyReplacementSchedule(scheduleList.value, replacement.value)
    val replacementPairNumbers = fillListReplacementNumber(replacement.value)

    val scheduleNullable = deleteEmptyLesson(scheduleWithReplacement)

    val currentSchedule = if (scheduleNullable.isNullOrEmpty()) {
        emptyList()
    } else scheduleNullable

    val selectedDateState = remember {
        mutableStateOf(LocalDate.now())
    }

    var replacementDialogShow by remember {
        mutableStateOf(false)
    }
    var connectionDialogShow by remember {
        mutableStateOf(false)
    }

    var mainLesson by remember {
        mutableStateOf(emptyList<LessonItem>())
    }
    var replacementLesson by remember {
        mutableStateOf(emptyList<LessonItem>())
    }

    var listSeveralLessons = ArrayList<LessonItem>()
    var prevLessonNumber = -1

    LaunchedEffect(Unit) {
        delay(300)
        viewModel.onEvent(
            ScheduleEvent.ChangeSchedule(
                LocalDate.now().dayOfWeek,
                isUpperWeek,
                selectedDateState.value
            )
        )

        showScheduleState = ShowScheduleState.SHOW

        if (firstVisitSchedule && !isNetworkAvailable(context)) {

            connectionDialogShow = true
            onFirstVisit()
        }
    }

    Box(
        modifier = Modifier
            .pointerInput(panelState.value != PanelState.CalendarPanel) {
                detectTapGestures {
                    if (panelState.value == PanelState.CalendarPanel)
                        panelState.value = PanelState.WeekPanel
                }
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

                if (showScheduleState == ShowScheduleState.SHOW && currentSchedule.isNotEmpty()) {

                    itemsIndexed(currentSchedule) { index, item ->

                        if (index != 0) prevLessonNumber = currentSchedule[index - 1].pairNumber

                        if (item.lessonTitle != "") {
                            if (item.subgroupNumber == 0) {

                                val list = ArrayList<LessonItem>()

                                scheduleList.value?.forEach {
                                    if (it.pairNumber == item.pairNumber)
                                        list.add(it)
                                }

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
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                        .border(
                                            width = 2.dp,
                                            color = MaterialTheme.colorScheme.inversePrimary,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable {
                                            if (panelState.value == PanelState.WeekPanel) {

                                                if (item.pairNumber in replacementPairNumbers) {
                                                    replacementDialogShow = true

                                                    mainLesson = list
                                                    replacementLesson = listOf(item)
                                                }
                                            } else {
                                                Log.d(
                                                    "checkPanelState",
                                                    panelState.value.toString()
                                                )
                                                panelState.value = PanelState.WeekPanel
                                            }
                                        },
                                    lessonItem = item,
                                    isDark = isDarkTheme,
                                    isReplacement = item.pairNumber in replacementPairNumbers,
                                    replacement = list
                                )

                                if (index != currentSchedule.lastIndex)
                                    Spacer(
                                        modifier = Modifier
                                            .height(20.dp)
                                    )
                            } else {
                                listSeveralLessons.add(item)

                                val list = ArrayList<LessonItem>()

                                scheduleList.value?.forEach {
                                    if (it.pairNumber == item.pairNumber)
                                        list.add(it)
                                }

                                if (index != currentSchedule.lastIndex && currentSchedule[index + 1].subgroupNumber == 0
                                    || index == currentSchedule.lastIndex && currentSchedule.isNotEmpty()
                                    || index != currentSchedule.lastIndex && currentSchedule[index + 1].pairNumber != item.pairNumber
                                /*|| index != 0 && prevLessonNumber == item.pairNumber*/
                                ) {

                                    val lessons = listSeveralLessons

                                    LessonCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .shadow(
                                                elevation = 4.dp,
                                                shape = RoundedCornerShape(10.dp),
                                                clip = true,
                                                spotColor = if (isDarkTheme) Color.Transparent
                                                else Color.Black.copy(alpha = 0.3f),
                                                ambientColor = if (isDarkTheme) Color.Transparent
                                                else Color.Black.copy(alpha = 0.3f)
                                            )
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                            .border(
                                                width = 2.dp,
                                                color = MaterialTheme.colorScheme.inversePrimary,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .clickable {
                                                if (panelState.value == PanelState.WeekPanel) {

                                                    if (item.pairNumber in replacementPairNumbers) {
                                                        replacementDialogShow = true

                                                        mainLesson = list
                                                        replacementLesson = lessons
                                                    }
                                                } else {
                                                    Log.d(
                                                        "checkPanelState",
                                                        panelState.value.toString()
                                                    )
                                                    panelState.value = PanelState.WeekPanel
                                                }
                                            },
                                        lessonList = listSeveralLessons,
                                        isDark = isDarkTheme,
                                        isReplacement = item.pairNumber in replacementPairNumbers,
                                        replacement = list
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

                } else if (currentSchedule.isEmpty() && showScheduleState == ShowScheduleState.LOADING) {

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

        AnimatedVisibility(
            visible = currentSchedule.isEmpty() && showScheduleState == ShowScheduleState.SHOW,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(100))
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Нет занятий",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        TopDatePanel(
            modifier = Modifier
                .fillMaxSize(),
            panelState = panelState,
            selectedDateState = selectedDateState,
            weekType = isUpperWeek,
            change = viewModel::onEvent,
            hideCalendar = {
                if (panelState.value == PanelState.CalendarPanel) {
                    panelState.value = PanelState.WeekPanel
                }
            },
            isDarkTheme = isDarkTheme
        )

        ReplacementDialog(
            showDialog = replacementDialogShow,
            onDismissRequest = {
                replacementDialogShow = false
            },
            main = mainLesson,
            replacement = replacementLesson,
            isDarkTheme = isDarkTheme
        )

        NetworkConnectionDialogWithRetry(
            showDialog = connectionDialogShow,
            onDismissRequest = {
                connectionDialogShow = false
            },
            havaRetry = false,
            isDarkTheme = isDarkTheme
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScheduleScreen() {
    ScheduleScreen()
}