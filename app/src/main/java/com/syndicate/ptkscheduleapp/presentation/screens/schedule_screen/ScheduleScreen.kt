package com.syndicate.ptkscheduleapp.presentation.screens.schedule_screen

import android.util.Log
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.runtime.snapshotFlow
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
import com.syndicate.ptkscheduleapp.presentation.screens.schedule_screen.components.PairCard
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components.ShimmerItem
import com.syndicate.ptkscheduleapp.ui.theme.AppTheme
import com.syndicate.ptkscheduleapp.ui.theme.utils.LocalColorsPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.ThemeMode
import com.syndicate.ptkscheduleapp.view_model.app_view_model.AppEvent
import com.syndicate.ptkscheduleapp.view_model.app_view_model.AppViewModel
import com.syndicate.ptkscheduleapp.view_model.schedule_view_model.ReworkScheduleEvent
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

    val scheduleViewModel = hiltViewModel<ReworkScheduleViewModel>()

    val themeModeState by appViewModel.themeModeState.collectAsState()
    val initialSheetValue by appViewModel.initialSheetValue.collectAsState()
    val enableChangeTheme by appViewModel.enableChangeTheme.collectAsState()

    val schedule by scheduleViewModel.schedule.collectAsState()
    val isUpperWeek by scheduleViewModel.isUpperWeek.collectAsState()
    val currentSchedulePage by scheduleViewModel.currentSchedulePage.collectAsState()
    val previousSchedulePage by scheduleViewModel.previousSchedulePage.collectAsState()
    val selectedDate by scheduleViewModel.selectedDate.collectAsState()

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
        isUpperWeek = isUpperWeek,
        currentSchedulePage = currentSchedulePage,
        previousSchedulePage = previousSchedulePage,
        selectedDate = selectedDate,
        initialSheetValue = initialSheetValue,
        enableChangeTheme = enableChangeTheme,
        onDateChange = {
            Log.d("schedulePagerState", "onDateChange")
            scheduleViewModel.onEvent(ReworkScheduleEvent.ChangeSelectedDate(it))
        },
        onSchedulePageChange = { scheduleViewModel.onEvent(ReworkScheduleEvent.ChangeSchedulePage(it)) },
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
    schedule: RequestState<List<List<PairItem>>> = RequestState.Loading,
    isUpperWeek: RequestState<Boolean> = RequestState.Success(false),
    currentSchedulePage: Int = 0,
    previousSchedulePage: Int = 0,
    selectedDate: LocalDate = LocalDate.now(),
    initialSheetValue: BottomSheetValue = BottomSheetValue.Collapsed,
    enableChangeTheme: Boolean = true,
    onDateChange: (LocalDate) -> Unit = { },
    onSchedulePageChange: (Int) -> Unit = { },
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

    val weeks = ScheduleUtils.getWeeksFromStartDate(
        LocalDate.of(LocalDate.now().year, Month.JANUARY, 1),
        78
    )

    val initWeekNumber = ScheduleUtils.getCurrentWeek(
        weeks,
        selectedDate
    )
    val pagerWeekStateSaved = remember {
        mutableIntStateOf(initWeekNumber)
    }

    val weekPanelPagerState = rememberPagerState(
        initialPage = pagerWeekStateSaved.intValue,
        initialPageOffsetFraction = 0f,
        pageCount = { weeks.size }
    )

    val schedulePagerState = rememberPagerState(
        initialPage = currentSchedulePage,
        initialPageOffsetFraction = 0f,
        pageCount = { 78 * 7 }
    )

    LaunchedEffect(schedulePagerState) {

        snapshotFlow { schedulePagerState.currentPage }.collect { page ->

            Log.d("schedulePagerState", "schedulePagerState.currentPage")

            val weekNumber = page / 7
            val indexInWeek = page % 7

            onSchedulePageChange(page)
            onDateChange(weeks[weekNumber][indexInWeek])
        }
    }

    LaunchedEffect(currentSchedulePage) {

        val weekNumber = currentSchedulePage / 7

        if (weekPanelPagerState.currentPage != weekNumber)
            weekPanelPagerState.animateScrollToPage(weekNumber)
    }

    LaunchedEffect(previousSchedulePage) {
        if (currentSchedulePage / 7 != previousSchedulePage / 7) {
            weekPanelPagerState.animateScrollToPage(currentSchedulePage / 7)
        }
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

                    ScrimSpacer(
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
                            ScheduleUtils.getWeekScheduleByWeekType(
                                schedule.getSuccessData(),
                                true
                            ),
                            ScheduleUtils.getWeekScheduleByWeekType(
                                schedule.getSuccessData(),
                                false
                            ),
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

                            val currentSchedule = ScheduleUtils.groupDailyScheduleBySubgroup(
                                scheduleList[currentScheduleIndex][page % 7]
                            )

                            if (scheduleList[currentScheduleIndex][page % 7].isNotEmpty()) {

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

                                    item {
                                        Text(
                                            text = "${
                                                LocalDate.of(2024, 1, 1).plusDays(page.toLong())
                                            }"
                                        )
                                    }

                                    itemsIndexed(
                                        items = currentSchedule,
                                        key = { index, _ ->
                                            index
                                        }
                                    ) { index, pair ->

                                        if (pair.size > 1) {

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
                                                pairList = pair,
                                                isDark = when (LocalColorsPalette.current.themeMode) {
                                                    ThemeMode.LIGHT, ThemeMode.CAPPUCCINO -> false
                                                    else -> true
                                                }
                                            )

                                        } else {

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
                                                pairItem = pair.first(),
                                                isDark = when (LocalColorsPalette.current.themeMode) {
                                                    ThemeMode.LIGHT, ThemeMode.CAPPUCCINO -> false
                                                    else -> true
                                                }
                                            )

                                        }

                                        if (index != currentSchedule.lastIndex)
                                            Spacer(modifier = Modifier.height(20.dp))

                                    }

                                    item {
                                        Spacer(
                                            modifier = Modifier
                                                .height(
                                                    WindowInsets
                                                        .systemBars
                                                        .asPaddingValues()
                                                        .calculateBottomPadding()
                                                            + 60.dp
                                                )
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
                                        text = "Нет занятий ${
                                            LocalDate.of(2024, 1, 1).plusDays(page.toLong())
                                        }",
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
                weekPanelPagerState = weekPanelPagerState,
                selectedDate = selectedDate,
                weeks = weeks,
                pagerWeekStateSaved = pagerWeekStateSaved,
                changeDate = { date ->

                    onDateChange(date)

                    val weekNumber = ScheduleUtils.getCurrentWeek(weeks, date)
                    val page = (weekNumber * 7) + weeks[weekNumber].indexOf(date)

                    scope.launch {
                        schedulePagerState.animateScrollToPage(
                            page = page
                        )
                    }
                },
                hideCalendar = {
                    if (panelState.value == PanelState.CalendarPanel) {
                        panelState.value = PanelState.WeekPanel
                    }
                }
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        WindowInsets
                            .systemBars
                            .asPaddingValues()
                            .calculateBottomPadding()
                    )
                    .background(
                        color = MaterialTheme.colorsPalette.backgroundColor
                    )
            )

            ScrimSpacer(
                color = Color.Black.copy(alpha = 0.32f),
                height = WindowInsets
                    .systemBars
                    .asPaddingValues()
                    .calculateBottomPadding(),
                visible = panelState.value == PanelState.CalendarPanel
            )
        }
    }
}

@Composable
private fun ScrimSpacer(
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