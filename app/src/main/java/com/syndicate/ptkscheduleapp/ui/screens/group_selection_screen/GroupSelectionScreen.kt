package com.syndicate.ptkscheduleapp.ui.screens.group_selection_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.syndicate.ptkscheduleapp.R
import com.syndicate.ptkscheduleapp.data.model.UserMode
import com.syndicate.ptkscheduleapp.ui.screens.course_selection_screen.components.SimpleButton
import com.syndicate.ptkscheduleapp.ui.screens.group_selection_screen.components.GroupPicker
import com.syndicate.ptkscheduleapp.ui.screens.group_selection_screen.components.rememberPickerState
import com.syndicate.ptkscheduleapp.ui.theme.SecondThemeBackground
import com.syndicate.ptkscheduleapp.view_model.group_selection_screen_view_model.GroupSelectionEvent
import com.syndicate.ptkscheduleapp.view_model.group_selection_screen_view_model.GroupSelectionViewModel
import kotlinx.coroutines.delay

@Composable
fun GroupSelectionScreen(
    modifier: Modifier = Modifier,
    navigateToNext: () -> Unit = { },
    changeUserGroup: (String) -> Unit = { },
    userMode: UserMode = UserMode.Student
) {
    val viewModel = hiltViewModel<GroupSelectionViewModel>()
    val listGroup = viewModel.listGroup.observeAsState()

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resId = R.raw.loading_lottie2)
    )
    var isLoading by remember {
        mutableStateOf(false)
    }
    val progress by animateLottieCompositionAsState(
        composition =  composition,
        isPlaying = isLoading,
        speed = 1f
    )

    val valuesPickerState = rememberPickerState()
    val groups = if (userMode == UserMode.Student)
        listGroup.value
    else listOf(
        "Цымбалюк Л.Н.", "Кручинина О.А.", "Сазонова Н.В.", "Дубогрей А.Е."
    )

    LaunchedEffect(Unit) {
        viewModel.onEvent(GroupSelectionEvent.FillListGroup)
        delay(200)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Text(
                    text = if (userMode == UserMode.Student) "Выберите группу"
                    else "Выберите\nпреподавателя",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }

            item {
                Spacer(
                    modifier = Modifier
                        .height(70.dp)
                )
            }

            item {
                AnimatedContent(
                    targetState = groups,
                    label = ""
                ) { groups ->
                    GroupPicker(
                        state = valuesPickerState,
                        items = groups,
                        visibleItemsCount = 5,
                        modifier = Modifier.width(
                            if (userMode == UserMode.Student) 130.dp
                            else 250.dp
                        ),
                        textModifier = Modifier.padding(8.dp),
                        textStyle = MaterialTheme.typography.bodyMedium,
                        fontSize = if (userMode == UserMode.Student) 40.sp
                        else 26.sp
                    )
                }
            }

            item {
                Spacer(
                    modifier = Modifier
                        .height(50.dp)
                )
            }

            item {
                SimpleButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            color = SecondThemeBackground
                        )
                        .padding(
                            horizontal = 82.dp,
                            vertical = 18.dp
                        ),
                    enable = !isLoading && !groups.isNullOrEmpty(),
                    onClick = {
                        isLoading = !isLoading
                        changeUserGroup(valuesPickerState.selectedItem)
                        navigateToNext()
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    textColor = Color.Black
                )
            }
        }

        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.Black.copy(alpha = 0.55f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        .padding(
                            16.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(
                        modifier = Modifier
                            .size(100.dp),
                        composition = composition,
                        progress = { progress }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGroupSelectionScreen() {
    GroupSelectionScreen()
}