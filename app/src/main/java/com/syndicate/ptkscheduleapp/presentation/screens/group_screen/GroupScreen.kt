package com.syndicate.ptkscheduleapp.presentation.screens.group_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.commandiron.compose_loading.Circle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.syndicate.ptkscheduleapp.domain.model.RequestState
import com.syndicate.ptkscheduleapp.extension.colorsPalette
import com.syndicate.ptkscheduleapp.presentation.common.ConfirmButton
import com.syndicate.ptkscheduleapp.presentation.screens.destinations.CourseScreenDestination
import com.syndicate.ptkscheduleapp.presentation.screens.destinations.ScheduleScreenDestination
import com.syndicate.ptkscheduleapp.presentation.screens.group_screen.components.GroupPicker
import com.syndicate.ptkscheduleapp.presentation.screens.group_screen.components.rememberPickerState
import com.syndicate.ptkscheduleapp.presentation.utils.FadeTransitions
import com.syndicate.ptkscheduleapp.ui.theme.AppTheme
import com.syndicate.ptkscheduleapp.ui.theme.MainBlue
import com.syndicate.ptkscheduleapp.ui.theme.utils.LocalColorsPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.ThemeMode
import com.syndicate.ptkscheduleapp.view_model.test_view_model.TestViewModel

@RootNavGraph
@Destination(
    style = FadeTransitions::class
)
@Composable
fun GroupScreen(
    navigator: DestinationsNavigator
) {

    val viewModel: TestViewModel = hiltViewModel()

    val data by viewModel.data.collectAsState(initial = RequestState.Idle)

    GroupScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        data = data,
        onConfirm = {
            navigator.navigate(ScheduleScreenDestination) {
                popUpTo(CourseScreenDestination.route) {
                    inclusive = true
                }
            }
        }
    )
}

@Composable
fun GroupScreenContent(
    modifier: Modifier = Modifier,
    data: RequestState<Int> = RequestState.Idle,
    onConfirm: () -> Unit = { }
) {

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
                text = "Выберите группу",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 36.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorsPalette.contentColor
            )

            Spacer(modifier = Modifier.height(70.dp))

            data.DisplayResult(
                onLoading = {
                    GroupContent()
                },
                onSuccess = {
                    GroupContent(data.getSuccessData().toString())
                },
                onError = {
                    GroupContent(data.getErrorMessage())
                }
            )

            Spacer(modifier = Modifier.height(50.dp))

            ConfirmButton(
                backgroundColor = MaterialTheme.colorsPalette.otherColor,
                contentColor = MaterialTheme.colorsPalette.contentColor,
                onClick = { onConfirm() }
            )
        }
    }
}

@Composable
fun GroupContent(
    text: String? = null
) {

    val themeMode = LocalColorsPalette.current.themeMode
    val groupPickerState = rememberPickerState()

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        if (text != null) {
            GroupPicker(
                state = groupPickerState,
                items = null,
                visibleItemsCount = 5,
                modifier = Modifier.width(130.dp),
                textModifier = Modifier.padding(8.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                fontSize = 40.sp
            )
        } else {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Circle(
                    modifier = Modifier
                        .padding(10.dp),
                    size = 70.dp,
                    color = when (themeMode) {
                        ThemeMode.LIGHT, ThemeMode.CAPPUCCINO -> MainBlue
                        else -> MaterialTheme.colorsPalette.contentColor
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewGroupScreenLight() {
    AppTheme {
        GroupScreenContent(
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
private fun PreviewGroupScreenCappuccino() {
    AppTheme(
        themeMode = ThemeMode.CAPPUCCINO
    ) {
        GroupScreenContent(
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
private fun PreviewGroupScreenGray() {
    AppTheme(
        themeMode = ThemeMode.GRAY
    ) {
        GroupScreenContent(
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
private fun PreviewGroupScreenDark() {
    AppTheme(
        themeMode = ThemeMode.DARK
    ) {
        GroupScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorsPalette.backgroundColor
                )
                .systemBarsPadding()
        )
    }
}