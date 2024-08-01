package com.syndicate.ptkscheduleapp.presentation.screens.schedule_screen

import android.util.Log
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import com.syndicate.ptkscheduleapp.data.model.PanelState
import com.syndicate.ptkscheduleapp.extension.colorsPalette
import com.syndicate.ptkscheduleapp.presentation.screens.schedule_screen.components.DatePanel
import com.syndicate.ptkscheduleapp.presentation.screens.schedule_screen.components.OptionSheetContent
import com.syndicate.ptkscheduleapp.presentation.screens.schedule_screen.components.ScheduleScaffold
import com.syndicate.ptkscheduleapp.presentation.utils.FadeTransitions
import com.syndicate.ptkscheduleapp.presentation.utils.setupSystemBars
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components.LessonCard
import com.syndicate.ptkscheduleapp.ui.theme.AppTheme
import com.syndicate.ptkscheduleapp.ui.theme.utils.LocalColorsPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.ThemeMode
import com.syndicate.ptkscheduleapp.view_model.main_view_model.AppEvent
import com.syndicate.ptkscheduleapp.view_model.main_view_model.AppViewModel
import com.syndicate.ptkscheduleapp.view_model.schedule_screen_view_model.ReworkScheduleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
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

    LaunchedEffect(themeModeState) {
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

    val weeks = getWeeksFromStartDate(
        LocalDate.of(LocalDate.now().year, Month.JANUARY, 1),
        78
    )

    val initWeekNumber = getCurrentWeek(
        weeks,
        LocalDate.now()
    )
    val currentSchedulePage = (initWeekNumber * 7) + weeks[initWeekNumber].indexOf(selectedDateState.value)
    Log.d("currentSchedulePage", "$initWeekNumber $currentSchedulePage")
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

        Log.d("schedulePagerState", "$currentPage")
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

            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                state = schedulePagerState
            ) {

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
                                    color = MaterialTheme.colorsPalette.backgroundColor
                                )
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorsPalette.otherColor,
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            lessonItem = LessonItem()
                        )
                    }

                    item {
                        Text(
                            text = "$it",
                            color = MaterialTheme.colorsPalette.contentColor
                        )
                    }

                    item {
                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                        )
                    }
                }
            }

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

private fun getWeeksFromStartDate(
    startDate: LocalDate,
    weeksCount: Int
): List<List<LocalDate>> {
    val weeks = mutableListOf<List<LocalDate>>()
    var currentStartOfWeek = startDate

    while (currentStartOfWeek.dayOfWeek != DayOfWeek.MONDAY) {
        currentStartOfWeek = currentStartOfWeek.minusDays(1)
    }

    repeat(weeksCount) {
        val week = (0 until 7).map { currentStartOfWeek.plusDays(it.toLong()) }
        weeks.add(week)
        currentStartOfWeek = currentStartOfWeek.plusWeeks(1)
    }

    return weeks
}

private fun getCurrentWeek(weeks: List<List<LocalDate>>, currentDate: LocalDate): Int {
    for (i in weeks.indices) {

        for (j in weeks[i].indices) {

            if (weeks[i][j].month == currentDate.month) {
                weeks[i].forEach { day ->
                    if (day.dayOfMonth == currentDate.dayOfMonth)
                        return i
                }
            }
        }
    }

    return 0
}

fun getCurrentTypeWeek(
    typeWeekNow: Boolean,
    prevPage: Int,
    currentPage: Int
) = if (prevPage % 2 == currentPage % 2) typeWeekNow else !typeWeekNow

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