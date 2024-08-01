package com.syndicate.ptkscheduleapp.presentation.screens.course_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.syndicate.ptkscheduleapp.extension.colorsPalette
import com.syndicate.ptkscheduleapp.presentation.common.ConfirmButton
import com.syndicate.ptkscheduleapp.presentation.screens.course_screen.components.SelectionCourseSection
import com.syndicate.ptkscheduleapp.presentation.screens.destinations.GroupScreenDestination
import com.syndicate.ptkscheduleapp.presentation.utils.FadeTransitions
import com.syndicate.ptkscheduleapp.presentation.utils.setupSystemBars
import com.syndicate.ptkscheduleapp.ui.theme.AppTheme
import com.syndicate.ptkscheduleapp.ui.theme.utils.LocalColorsPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.ThemeMode

@RootNavGraph
@Destination(
    style = FadeTransitions::class
)
@Composable
fun CourseScreen(
    navigator: DestinationsNavigator
) {

    setupSystemBars(
        LocalContext.current,
        LocalColorsPalette.current.themeMode,
        isSplashScreen = false
    )

    CourseScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        onConfirm = {
            navigator.navigate(
                GroupScreenDestination,
                onlyIfResumed = true
            )
        }
    )
}

@Composable
fun CourseScreenContent(
    modifier: Modifier = Modifier,
    onConfirm: (Int) -> Unit = { }
) {

    val courseList = listOf("1 курс", "2 курс", "3 курс", "4 курс")
    var selectedCourse by rememberSaveable {
        mutableIntStateOf(0)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Выберите курс",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 36.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorsPalette.contentColor
            )

            Spacer(modifier = Modifier.height(96.dp))

            SelectionCourseSection(
                courseList = courseList,
                courseProvider = { selectedCourse },
                onCourseClick = { selectedCourse = it }
            )

            Spacer(modifier = Modifier.height(96.dp))

            ConfirmButton(
                backgroundColor = MaterialTheme.colorsPalette.otherColor,
                contentColor = MaterialTheme.colorsPalette.contentColor,
                onClick = { onConfirm(selectedCourse) }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCourseScreenLight() {
    AppTheme {
        CourseScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorsPalette.backgroundColor
                )
                .systemBarsPadding()
        )
    }
}

@Preview
@Composable
private fun PreviewCourseScreenCappuccino() {
    AppTheme(
        themeMode = ThemeMode.CAPPUCCINO
    ) {
        CourseScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorsPalette.backgroundColor
                )
                .systemBarsPadding()
        )
    }
}

@Preview
@Composable
private fun PreviewCourseScreenGray() {
    AppTheme(
        themeMode = ThemeMode.GRAY
    ) {
        CourseScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorsPalette.backgroundColor
                )
                .systemBarsPadding()
        )
    }
}

@Preview
@Composable
private fun PreviewCourseScreenDark() {
    AppTheme(
        themeMode = ThemeMode.DARK
    ) {
        CourseScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorsPalette.backgroundColor
                )
                .systemBarsPadding()
        )
    }
}