package com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.data.model.PanelState
import com.syndicate.ptkscheduleapp.data.model.SwipeDirection
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.getCurrentTypeWeek
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.getCurrentWeek
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.getWeeksFromStartDate
import com.syndicate.ptkscheduleapp.ui.theme.GrayText
import com.syndicate.ptkscheduleapp.ui.theme.GrayThirdTheme
import com.syndicate.ptkscheduleapp.view_model.schedule_screen_view_model.ScheduleEvent
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import kotlin.math.abs

@SuppressLint("UnnecessaryComposedModifier")
@Composable
fun TopDatePanel(
    modifier: Modifier = Modifier,
    panelState: MutableState<PanelState> = mutableStateOf(PanelState.WeekPanel),
    selectedDateState: MutableState<LocalDate> = mutableStateOf(LocalDate.now()),
    weekType: Boolean = true,
    weeks: List<List<LocalDate>> = getWeeksFromStartDate(
        LocalDate.of(LocalDate.now().year, Month.JANUARY, 1),
        78
    ),
    pagerWeekStateSaved: MutableState<Int> = mutableIntStateOf(0),
    change: (ScheduleEvent) -> Unit = { },
    hideCalendar: () -> Unit = { },
    isDarkTheme: Boolean = false
) {

    val months = getMonthsFromWeeks(weeks)

    val pagerMonthStateSaved = remember {
        mutableIntStateOf(getCurrentMonth(months, LocalDate.now()))
    }

    var direction by remember {
        mutableStateOf(SwipeDirection.UP)
    }

    val monthValue = remember {
        mutableStateOf(LocalDate.now().month)
    }
    val monthText = remember {
        mutableStateOf(
            when (selectedDateState.value.month) {
                Month.JANUARY -> "Январь"
                Month.FEBRUARY -> "Февраль"
                Month.MARCH -> "Март"
                Month.APRIL -> "Апрель"
                Month.MAY -> "Май"
                Month.JUNE -> "Июнь"
                Month.JULY -> "Июль"
                Month.AUGUST -> "Август"
                Month.SEPTEMBER -> "Сентябрь"
                Month.OCTOBER -> "Октябрь"
                Month.NOVEMBER -> "Ноябрь"
                Month.DECEMBER -> "Декабрь"
                else -> "Январь"
            }
        )
    }

    val yearText = remember {
        mutableIntStateOf(selectedDateState.value.year)
    }

    val colorBorder = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)

    Box(
        modifier = modifier
    ) {

        AnimatedVisibility(
            visible = panelState.value == PanelState.CalendarPanel,
            enter = fadeIn(),
            exit = fadeOut()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.Black.copy(alpha = 0.35f)
                    )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        bottomStart = 25.dp,
                        bottomEnd = 25.dp
                    )
                )
                .background(
                    color = MaterialTheme.colorScheme.onPrimary
                )
                .drawBehind {

                    drawLine(
                        color = colorBorder,
                        start = Offset(x = 25.dp.toPx(), y = size.height),
                        end = Offset(x = size.width - 25.dp.toPx(), y = size.height),
                        strokeWidth = 4.dp.toPx()
                    )

                    drawLine(
                        color = colorBorder,
                        start = Offset(x = 0.dp.toPx(), y = 0.dp.toPx()),
                        end = Offset(x = 0.dp.toPx(), y = size.height - 25.dp.toPx()),
                        strokeWidth = 4.dp.toPx()
                    )

                    drawLine(
                        color = colorBorder,
                        start = Offset(x = size.width, y = 0.dp.toPx()),
                        end = Offset(x = size.width, y = size.height - 25.dp.toPx()),
                        strokeWidth = 4.dp.toPx()
                    )

                    drawArc(
                        color = colorBorder,
                        startAngle = 180f,
                        sweepAngle = -90f,
                        useCenter = false,
                        topLeft = Offset(x = 0f, y = size.height - 25.dp.toPx() * 2),
                        size = Size(25.dp.toPx() * 2, 25.dp.toPx() * 2),
                        style = Stroke(width = 4.dp.toPx())
                    )

                    drawArc(
                        color = colorBorder,
                        startAngle = 360f,
                        sweepAngle = 90f,
                        useCenter = false,
                        topLeft = Offset(
                            x = size.width - 25.dp.toPx() * 2,
                            y = size.height - 25.dp.toPx() * 2
                        ),
                        size = Size(25.dp.toPx() * 2, 25.dp.toPx() * 2),
                        style = Stroke(width = 4.dp.toPx())
                    )
                }
                .clickable(
                    indication = null,
                    interactionSource = remember {
                        MutableInteractionSource()
                    }
                ) {
                    if (panelState.value == PanelState.CalendarPanel)
                        panelState.value = PanelState.CalendarPanel
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()

                            val (x, y) = dragAmount

                            if (abs(x) <= abs(y)) {
                                when {
                                    y > 0 -> direction = SwipeDirection.DOWN

                                    y < 0 -> direction = SwipeDirection.UP
                                }
                            }
                        },
                        onDragEnd = {
                            panelState.value = when (direction) {
                                SwipeDirection.UP -> PanelState.WeekPanel
                                SwipeDirection.DOWN -> PanelState.CalendarPanel
                            }
                        }
                    )
                }
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 120,
                        easing = EaseOutQuad
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            bottom = 12.dp
                        )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${monthText.value}, ${yearText.intValue}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 30.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС").forEach {
                        Box(
                            modifier = Modifier
                                .width(36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isDarkTheme) Color.White.copy(alpha = 0.6f)
                                else GrayThirdTheme
                            )
                        }
                    }
                }

                AnimatedVisibility(visible = panelState.value == PanelState.WeekPanel) {

                    WeekPanel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 6.dp,
                                bottom = 12.dp
                            ),
                        selectedDate = selectedDateState,
                        weekType = weekType,
                        weeks = weeks,
                        monthValue = monthValue,
                        pagerWeekStateSaved = pagerWeekStateSaved,
                        monthText = monthText,
                        yearText = yearText,
                        change = change,
                        isDarkTheme = isDarkTheme
                    )
                }

                AnimatedVisibility(visible = panelState.value == PanelState.CalendarPanel) {
                    Box(
                        modifier = Modifier
                            .padding(
                                top = 6.dp
                            )
                    ) {
                        Calendar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    bottom = 12.dp
                                ),
                            selectedDate = selectedDateState,
                            weekType = weekType,
                            weekList = weeks,
                            months = months,
                            monthValue = monthValue,
                            pagerMonthStateSaved = pagerMonthStateSaved,
                            monthText = monthText,
                            yearText = yearText,
                            change = change,
                            hideCalendar = hideCalendar,
                            isDarkTheme = isDarkTheme
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeekPanel(
    modifier: Modifier = Modifier,
    selectedDate: MutableState<LocalDate> = mutableStateOf(LocalDate.now()),
    weekType: Boolean,
    weeks: List<List<LocalDate>>,
    monthValue: MutableState<Month>,
    pagerWeekStateSaved: MutableState<Int>,
    monthText: MutableState<String>,
    yearText: MutableState<Int>,
    change: (ScheduleEvent) -> Unit,
    isDarkTheme: Boolean = false
) {

    LaunchedEffect(Unit) {

        pagerWeekStateSaved.value = syncPanelRework(
            weeks,
            selectedDate.value
        )
    }

    val pagerState = rememberPagerState(
        initialPage = pagerWeekStateSaved.value,
        initialPageOffsetFraction = 0f,
        pageCount = { weeks.size }
    )

    LaunchedEffect(key1 = pagerWeekStateSaved.value) {
        pagerState.scrollToPage(pagerWeekStateSaved.value)
    }

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            pagerWeekStateSaved.value = page

            val weekDates = weeks[page]

            if (selectedDate.value !in weekDates) {

                monthText.value = getStringByMonth(weekDates[3].month)
                monthValue.value = weekDates[3].month
                yearText.value = weekDates[3].year
            } else {

                monthText.value = getStringByMonth(selectedDate.value.month)
                monthValue.value = selectedDate.value.month
                yearText.value = selectedDate.value.year
            }
        }
    }

    LaunchedEffect(key1 = selectedDate.value) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            pagerWeekStateSaved.value = page

            val weekDates = weeks[page]

            if (selectedDate.value !in weekDates) {

                monthText.value = getStringByMonth(weekDates[3].month)
                monthValue.value = weekDates[3].month
                yearText.value = weekDates[3].year
            } else {

                monthText.value = getStringByMonth(selectedDate.value.month)
                monthValue.value = selectedDate.value.month
                yearText.value = selectedDate.value.year
            }
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 30.dp
                ),
            state = pagerState
        ) { page ->
            val weekDates = weeks[page]

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weekDates.forEach { date ->

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable {
                                change(
                                    ScheduleEvent.ChangeSchedule(
                                        date.dayOfWeek,
                                        getCurrentTypeWeek(
                                            weekType,
                                            getCurrentWeek(weeks, LocalDate.now()),
                                            getCurrentWeek(weeks, date)
                                        ),
                                        date
                                    )
                                )
                                selectedDate.value = date
                            }
                            .border(
                                width = 1.5.dp,
                                color = if (date == selectedDate.value) GrayText else Color.Transparent,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDarkTheme) Color.White.copy(alpha = 0.6f)
                            else GrayThirdTheme
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    selectedDate: MutableState<LocalDate>,
    weekType: Boolean,
    weekList: List<List<LocalDate>>,
    months: List<List<LocalDate>>,
    monthValue: MutableState<Month>,
    pagerMonthStateSaved: MutableState<Int>,
    monthText: MutableState<String>,
    yearText: MutableState<Int>,
    change: (ScheduleEvent) -> Unit,
    hideCalendar: () -> Unit,
    isDarkTheme: Boolean = false
) {
    pagerMonthStateSaved.value = syncPanels(
        months,
        monthValue.value,
        yearText.value,
        selectedDate.value
    )

    val pagerState = rememberPagerState(
        initialPage = pagerMonthStateSaved.value,
        initialPageOffsetFraction = 0f,
        pageCount = { months.size }
    )

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            pagerMonthStateSaved.value = page

            val monthDates = months[page]

            monthText.value = when (monthDates.first().month) {
                Month.JANUARY -> "Январь"
                Month.FEBRUARY -> "Февраль"
                Month.MARCH -> "Март"
                Month.APRIL -> "Апрель"
                Month.MAY -> "Май"
                Month.JUNE -> "Июнь"
                Month.JULY -> "Июль"
                Month.AUGUST -> "Август"
                Month.SEPTEMBER -> "Сентябрь"
                Month.OCTOBER -> "Октябрь"
                Month.NOVEMBER -> "Ноябрь"
                Month.DECEMBER -> "Декабрь"
                else -> "Январь"
            }

            monthValue.value = monthDates.first().month

            yearText.value = monthDates.first().year
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 30.dp
                ),
            state = pagerState
        ) { page ->
            val monthDates = months[page]
            val weeks = getWeeksFromMonth(monthDates)

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                weeks.forEach { week ->

                    WeekRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        weekType = weekType,
                        weeks = weekList,
                        week = week,
                        selectedDate = selectedDate,
                        change = change,
                        hideCalendar = hideCalendar,
                        isDarkTheme = isDarkTheme
                    )
                }
            }
        }
    }
}

@Composable
fun WeekRow(
    modifier: Modifier = Modifier,
    weekType: Boolean,
    weeks: List<List<LocalDate>>,
    week: List<LocalDate>,
    selectedDate: MutableState<LocalDate>,
    change: (ScheduleEvent) -> Unit,
    hideCalendar: () -> Unit,
    isDarkTheme: Boolean = false
) {
    var currentDayOfWeek = DayOfWeek.MONDAY
    var currentIndex = 0
    var counterDays = 7

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        while (counterDays != 0) {
            if (week[currentIndex].dayOfWeek == currentDayOfWeek) {

                DayItem(
                    selectedDate = selectedDate,
                    value = week[currentIndex],
                    isEmpty = false,
                    weekType = weekType,
                    weeks = weeks,
                    change = change,
                    hideCalendar = hideCalendar,
                    isDarkTheme = isDarkTheme
                )

                if (currentIndex != week.size - 1)
                    currentIndex++

            } else {
                DayItem(
                    selectedDate = selectedDate,
                    value = week[currentIndex],
                    isEmpty = true,
                    weekType = weekType,
                    weeks = weeks,
                    change = change,
                    hideCalendar = hideCalendar,
                    isDarkTheme = isDarkTheme
                )
            }

            counterDays--
            currentDayOfWeek = currentDayOfWeek.plus(1)
        }
    }
}

@Composable
fun DayItem(
    selectedDate: MutableState<LocalDate>,
    value: LocalDate,
    isEmpty: Boolean,
    weekType: Boolean,
    weeks: List<List<LocalDate>>,
    change: (ScheduleEvent) -> Unit,
    hideCalendar: () -> Unit,
    isDarkTheme: Boolean = false
) {

    if (!isEmpty)
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .clickable {

                    change(
                        ScheduleEvent.ChangeSchedule(
                            value.dayOfWeek,
                            getCurrentTypeWeek(
                                weekType,
                                getCurrentWeek(weeks, LocalDate.now()),
                                getCurrentWeek(weeks, value)
                            ),
                            value
                        )
                    )

                    selectedDate.value = value

                    hideCalendar()
                }
                .border(
                    width = 1.5.dp,
                    color = if (value == selectedDate.value) GrayText else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDarkTheme) Color.White.copy(alpha = 0.6f)
                else GrayThirdTheme
            )
        }
    else
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = GrayThirdTheme
            )
        }
}

private fun getStringByMonth(month: Month) = when (month) {
    Month.JANUARY -> "Январь"
    Month.FEBRUARY -> "Февраль"
    Month.MARCH -> "Март"
    Month.APRIL -> "Апрель"
    Month.MAY -> "Май"
    Month.JUNE -> "Июнь"
    Month.JULY -> "Июль"
    Month.AUGUST -> "Август"
    Month.SEPTEMBER -> "Сентябрь"
    Month.OCTOBER -> "Октябрь"
    Month.NOVEMBER -> "Ноябрь"
    Month.DECEMBER -> "Декабрь"
    else -> "Январь"
}

private fun getWeeksFromMonth(month: List<LocalDate>): List<List<LocalDate>> {
    var currentDayOfWeek = DayOfWeek.MONDAY
    var arrayDaysOfWeek = ArrayList<LocalDate>()
    val arrayWeeksOfMonth = ArrayList<List<LocalDate>>()
    var currentIndex = 0
    var monthSize = month.size

    while (monthSize != 0) {
        if (month[currentIndex].dayOfWeek == currentDayOfWeek) {
            arrayDaysOfWeek.add(month[currentIndex])

            if (currentIndex != month.size - 1)
                currentIndex++
            monthSize--
        }

        if (currentDayOfWeek == DayOfWeek.SUNDAY /*DayOfWeek.SATURDAY*/) {

            arrayWeeksOfMonth.add(arrayDaysOfWeek)
            arrayDaysOfWeek = ArrayList()
        }

        currentDayOfWeek = currentDayOfWeek.plus(1)
    }

    if (arrayDaysOfWeek.isNotEmpty())
        arrayWeeksOfMonth.add(arrayDaysOfWeek)

    return arrayWeeksOfMonth
}

private fun getMonthsFromWeeks(weeks: List<List<LocalDate>>): List<List<LocalDate>> {
    var currentMonth = Month.JANUARY
    var arrayDaysOfMonth = ArrayList<LocalDate>()
    val arrayOfMonths = ArrayList<List<LocalDate>>()

    for (i in weeks.indices) {

        for (j in weeks[i].indices) {

            if (weeks[i][j].month == currentMonth)
                arrayDaysOfMonth.add(weeks[i][j])
            else {
                if (arrayDaysOfMonth.size in 28..31) {

                    arrayOfMonths.add(arrayDaysOfMonth)

                    arrayDaysOfMonth = ArrayList()

                    currentMonth = currentMonth.plus(1)

                    arrayDaysOfMonth.add(weeks[i][j])
                }
            }
        }
    }

    if (arrayDaysOfMonth.size != 0) {

        var day = arrayDaysOfMonth.last().plusDays(1)

        while (day.month == currentMonth) {

            arrayDaysOfMonth.add(day)
            day = day.plusDays(1)
        }
    }

    arrayOfMonths.add(arrayDaysOfMonth)

    return arrayOfMonths
}

private fun getCurrentMonth(months: List<List<LocalDate>>, currentDate: LocalDate): Int {
    for (i in months.indices)
        if (months[i][3].month == currentDate.month) return i

    return 0
}

private fun syncPanels(
    content: List<List<LocalDate>>,
    month: Month,
    year: Int = LocalDate.now().year,
    selectedDate: LocalDate
): Int {

    Log.d("syncPanel", content.size.toString())

    if (selectedDate.month == month && selectedDate.year == year) {

        for (i in content.indices) {

            for (j in content[i].indices)
                if (content[i][j].dayOfMonth == selectedDate.dayOfMonth
                    && content[i][j].month == selectedDate.month
                    && content[i][j].year == selectedDate.year
                )
                    return i
        }

    } else {
        for (i in content.indices) {

            if (content[i].first().month == month && content[i].first().year == year)
                return i
        }
    }

    return 0
}

private fun syncPanelRework(
    content: List<List<LocalDate>>,
    selectedDate: LocalDate
): Int {

    for (i in content.indices) {

        if (content[i].indexOf(selectedDate) != -1)
            return i
    }

    return 0
}

private fun syncWeekPanel(
    content: List<List<LocalDate>>,
    month: Month,
    year: Int = LocalDate.now().year,
    selectedDate: LocalDate,
    pagerWeekState: Int
): Int {

    Log.d("syncWeekPanel", pagerWeekState.toString())

    if (selectedDate.month == month && selectedDate.year == year) {
        return returnToSelectedDate(content, selectedDate)
    } else {

        if (content[pagerWeekState].first().month == month &&
            content[pagerWeekState].last().month == month ||
            content[pagerWeekState][3].month == month) {

            return pagerWeekState
        } else {

            for (i in content.indices) {

                if (content[i].first().month == month && content[i].first().year == year)
                    return i
            }
        }
    }

    return 0
}

private fun returnToSelectedDate(
    content: List<List<LocalDate>>,
    selectedDate: LocalDate
): Int {

    for (i in content.indices) {

        for (j in content[i].indices)
            if (content[i][j].dayOfMonth == selectedDate.dayOfMonth
                && content[i][j].month == selectedDate.month
                && content[i][j].year == selectedDate.year
            )
                return i
    }

    return 0
}

@Preview
@Composable
fun PreviewTopDatePanel() {
    TopDatePanel()
}