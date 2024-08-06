package com.syndicate.ptkscheduleapp.presentation.screens.schedule_screen

import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.syndicate.ptkscheduleapp.common.utils.ScheduleUtils
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import com.syndicate.ptkscheduleapp.data.model.PanelState
import com.syndicate.ptkscheduleapp.domain.model.PairItem
import com.syndicate.ptkscheduleapp.domain.model.RequestState
import com.syndicate.ptkscheduleapp.extension.colorsPalette
import com.syndicate.ptkscheduleapp.presentation.screens.schedule_screen.components.DatePanel
import com.syndicate.ptkscheduleapp.presentation.screens.schedule_screen.components.OptionSheetContent
import com.syndicate.ptkscheduleapp.presentation.screens.schedule_screen.components.ScheduleScaffold
import com.syndicate.ptkscheduleapp.presentation.utils.FadeTransitions
import com.syndicate.ptkscheduleapp.presentation.utils.setupSystemBars
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components.PairCard
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components.ShimmerItem
import com.syndicate.ptkscheduleapp.ui.theme.AppTheme
import com.syndicate.ptkscheduleapp.ui.theme.utils.LocalColorsPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.ThemeMode
import com.syndicate.ptkscheduleapp.view_model.app_view_model.AppEvent
import com.syndicate.ptkscheduleapp.view_model.app_view_model.AppViewModel
import com.syndicate.ptkscheduleapp.view_model.schedule_view_model.ReworkScheduleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month

@OptIn(ExperimentalMaterialApi::class)
@RootNavGraph
@Destination(
    style = FadeTransitions::class
)
@Composable
fun ScheduleScreen(
    navigator: DestinationsNavigator,
    appViewModel: AppViewModel
) {

    val context = LocalContext.current

    val themeModeState by appViewModel.themeModeState.collectAsState()
    val initialSheetValue by appViewModel.initialSheetValue.collectAsState()
    val enableChangeTheme by appViewModel.enableChangeTheme.collectAsState()

    val scheduleViewModel = hiltViewModel<ReworkScheduleViewModel>()

    val schedule by scheduleViewModel.schedule.collectAsState()

    LaunchedEffect(Unit) {
        delay(500)
        setupSystemBars(
            context = context,
            currentThemeMode = themeModeState,
            isSplashScreen = false
        )
    }

    ScheduleScreenContent(
        modifier = Modifier
            .fillMaxSize(),
        schedule = schedule,
        initialSheetValue = initialSheetValue,
        enableChangeTheme = enableChangeTheme,
        onChangeTheme = { themeMode, offset ->
            appViewModel.onEvent(AppEvent.ChangeTheme(themeMode, offset))
        },
        confirmStateSheetChange = {
            appViewModel.onEvent(AppEvent.ChangeSheetState(it))
        },
        lockChangeTheme = { appViewModel.onEvent(AppEvent.LockChangeTheme) },
        unlockChangeTheme = { appViewModel.onEvent(AppEvent.UnlockChangeTheme) }
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreenContent(
    modifier: Modifier = Modifier,
    schedule: RequestState<List<List<PairItem>>> = RequestState.Success(listOf(emptyList())),
    isUpperWeek: RequestState<Boolean> = RequestState.Success(false),
    initialSheetValue: BottomSheetValue = BottomSheetValue.Collapsed,
    enableChangeTheme: Boolean = true,
    onChangeTheme: (ThemeMode, Offset) -> Unit = { _: ThemeMode, _: Offset -> },
    confirmStateSheetChange: (BottomSheetValue) -> Unit = { },
    lockChangeTheme: () -> Unit = { },
    unlockChangeTheme: () -> Unit = { }
) {

    val scope = rememberCoroutineScope()

    val peekHeight = 40.dp + WindowInsets
        .navigationBars
        .asPaddingValues()
        .calculateBottomPadding()

    val bottomSheetState = rememberBottomSheetState(
        initialValue = initialSheetValue,
        animationSpec = TweenSpec(
            durationMillis = 200,
            easing = Ease
        ),
        confirmStateChange = {
            confirmStateSheetChange(it)
            true
        }
    )
    val scheduleScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState)

    val panelState = remember { mutableStateOf(PanelState.WeekPanel) }
    val selectedDateState = remember { mutableStateOf(LocalDate.now()) }

    val weeks = ScheduleUtils.getWeeksFromStartDate(
        LocalDate.of(LocalDate.now().year, Month.JANUARY, 1),
        78
    )

    val initWeekNumber = ScheduleUtils.getCurrentWeek(
        weeks,
        LocalDate.now()
    )
    val currentSchedulePage = (initWeekNumber * 7) + weeks[initWeekNumber].indexOf(selectedDateState.value)
    val pagerWeekStateSaved = remember {
        mutableIntStateOf(initWeekNumber)
    }

    val schedulePagerState = rememberPagerState(
        initialPage = currentSchedulePage,
        initialPageOffsetFraction = 0f,
        pageCount = { 78 * 7 }
    )

    LaunchedEffect(schedulePagerState.currentPage) {

        val currentPage = schedulePagerState.currentPage
        val weekNumber = currentPage / 7
        val indexInWeek = currentPage % 7

        selectedDateState.value = weeks[weekNumber][indexInWeek]
    }

    Box(
        modifier = modifier
    ) {

        ScheduleScaffold(
            scaffoldState = scheduleScaffoldState,
            sheetGesturesEnabled = panelState.value != PanelState.CalendarPanel,
            sheetPeekHeight = peekHeight,
            sheetShape = RoundedCornerShape(
                topStart = 25.dp,
                topEnd = 25.dp
            ),
            backgroundColor = MaterialTheme.colorsPalette.backgroundColor,
            onDismiss = { scope.launch { bottomSheetState.collapse() } },
            sheetContent = {
                Box {
                    OptionSheetContent(
                        enableChangeTheme = enableChangeTheme,
                        onChangeTheme = { themeMode, offset ->
                            scope.launch { onChangeTheme(themeMode, offset) }
                        },
                        lockChangeTheme = lockChangeTheme,
                        unlockChangeTheme = unlockChangeTheme
                    )

                    ScrimSheetContent(
                        color = Color.Black.copy(alpha = 0.32f),
                        height = peekHeight,
                        visible = panelState.value == PanelState.CalendarPanel
                    )
                }
            }
        ) {

            schedule.DisplayResult(
                onLoading = {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .padding(
                                horizontal = 16.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        item {
                            Spacer(
                                modifier = Modifier
                                    .height(160.dp)
                            )
                        }

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
                },
                onSuccess = {

                    if (isUpperWeek.isSuccess()) {

                        val scheduleList = listOf(
                            ScheduleUtils.getWeekScheduleByWeekType(schedule.getSuccessData(), true),
                            ScheduleUtils.getWeekScheduleByWeekType(schedule.getSuccessData(), false)
                        )

                        val startScheduleIndex = if (
                            ScheduleUtils.getCurrentTypeWeek(
                                isUpperWeek.getSuccessData(),
                                initWeekNumber,
                                0
                            )
                        ) 0 else 1

                        HorizontalPager(
                            modifier = Modifier
                                .fillMaxSize()
                                .statusBarsPadding(),
                            state = schedulePagerState
                        ) { page ->

                            val currentScheduleIndex = if (page / 7 % 2 == 0) startScheduleIndex
                                else 1 - startScheduleIndex

                            val currentSchedule = scheduleList[currentScheduleIndex][page % 7]

                            var listSeveralLessons = ArrayList<LessonItem>()

                            if (currentSchedule.isNotEmpty()) {

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
                                                .height(160.dp)
                                        )
                                    }

                                    itemsIndexed(currentSchedule) { index, pair ->

                                        if (pair.subgroupNumber == 0) {

                                            PairCard(
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
                                                        color = MaterialTheme.colorsPalette.backgroundColor
                                                    )
                                                    .border(
                                                        width = 2.dp,
                                                        color = MaterialTheme.colorsPalette.otherColor,
                                                        shape = RoundedCornerShape(10.dp)
                                                    ),
                                                lessonItem = LessonItem(
                                                    time = pair.time,
                                                    lessonTitle = pair.subject,
                                                    teacher = pair.teacher,
                                                    room = pair.room,
                                                    pairNumber = pair.pairNumber
                                                ),
                                                isDark = when (LocalColorsPalette.current.themeMode) {
                                                    ThemeMode.LIGHT, ThemeMode.CAPPUCCINO -> false
                                                    else -> true
                                                }
                                            )

                                            if (index != currentSchedule.lastIndex)
                                                Spacer(modifier = Modifier.height(20.dp))

                                        } else {

                                            listSeveralLessons.add(
                                                LessonItem(
                                                    time = pair.time,
                                                    lessonTitle = pair.subject,
                                                    teacher = pair.teacher,
                                                    room = pair.room,
                                                    pairNumber = pair.pairNumber,
                                                    subgroupNumber = pair.subgroupNumber
                                                )
                                            )

                                            val list = ArrayList<LessonItem>()

                                            currentSchedule.forEach {
                                                if (it.pairNumber == pair.pairNumber)
                                                    list.add(
                                                        LessonItem(
                                                            time = it.time,
                                                            lessonTitle = it.subject,
                                                            teacher = it.teacher,
                                                            room = it.room,
                                                            pairNumber = it.pairNumber,
                                                            subgroupNumber = it.subgroupNumber
                                                        )
                                                    )
                                            }

                                            if (index != currentSchedule.lastIndex && currentSchedule[index + 1].subgroupNumber == 0
                                                || index == currentSchedule.lastIndex && currentSchedule.isNotEmpty()
                                                || index != currentSchedule.lastIndex && currentSchedule[index + 1].pairNumber != pair.pairNumber
                                            ) {

                                                PairCard(
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
                                                            color = MaterialTheme.colorsPalette.backgroundColor
                                                        )
                                                        .border(
                                                            width = 2.dp,
                                                            color = MaterialTheme.colorsPalette.otherColor,
                                                            shape = RoundedCornerShape(10.dp)
                                                        ),
                                                    lessonList = listSeveralLessons,
                                                    isDark = when (LocalColorsPalette.current.themeMode) {
                                                        ThemeMode.LIGHT, ThemeMode.CAPPUCCINO -> false
                                                        else -> true
                                                    }
                                                )

                                                if (index != currentSchedule.lastIndex)
                                                    Spacer(modifier = Modifier.height(20.dp))

                                                listSeveralLessons = ArrayList()
                                            }
                                        }
                                    }

                                    item {
                                        Spacer(
                                            modifier = Modifier
                                                .height(80.dp)
                                        )
                                    }
                                }

                            } else {

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
                                        color = MaterialTheme.colorsPalette.contentColor
                                    )
                                }

                            }
                        }
                    }
                },
                onError = {

                }
            )

            DatePanel(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                panelState = panelState,
                selectedDateState = selectedDateState,
                weekType = true,
                weeks = weeks,
                pagerWeekStateSaved = pagerWeekStateSaved,
                change = { },
                hideCalendar = {
                    if (panelState.value == PanelState.CalendarPanel) {
                        panelState.value = PanelState.WeekPanel
                    }
                }
            )
        }
    }
}

@Composable
private fun ScrimSheetContent(
    color: Color,
    height: Dp,
    visible: Boolean
) {

    if (color.isSpecified) {
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = TweenSpec(
                easing = Ease
            ),
            label = "scrim"
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        ) {
            drawRect(color = color, alpha = alpha)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun PreviewScheduleScreenLight() {
    AppTheme {
        ScheduleScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorsPalette.backgroundColor
                )
                .systemBarsPadding()
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun PreviewScheduleScreenCappuccino() {
    AppTheme(
        themeMode = ThemeMode.CAPPUCCINO
    ) {
        ScheduleScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorsPalette.backgroundColor
                )
                .systemBarsPadding()
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun PreviewScheduleScreenGray() {
    AppTheme(
        themeMode = ThemeMode.GRAY
    ) {
        ScheduleScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorsPalette.backgroundColor
                )
                .systemBarsPadding()
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun PreviewScheduleScreenDark() {
    AppTheme(
        themeMode = ThemeMode.DARK
    ) {
        ScheduleScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorsPalette.backgroundColor
                )
                .systemBarsPadding()
        )
    }
}