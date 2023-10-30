package com.syndicate.ptkscheduleapp.ui.screens.role_selection_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.data.model.UserMode
import com.syndicate.ptkscheduleapp.ui.screens.course_selection_screen.components.SimpleButton
import com.syndicate.ptkscheduleapp.ui.screens.role_selection_screen.components.SelectionRoleSection
import com.syndicate.ptkscheduleapp.ui.theme.SecondThemeBackground
import com.syndicate.ptkscheduleapp.view_model.ScheduleEvent

@Composable
fun RoleSelectionScreen(
    modifier: Modifier = Modifier,
    navigateToNext: (UserMode) -> Unit = { },
    changeUserMode: (UserMode) -> Unit = { }
) {
    val radioOptions = listOf("Студент", "Преподаватель")
    val radioState = remember {
        mutableStateOf("Студент")
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
                text = "Здравствуйте!",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 36.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
            Text(
                text = "Выберите роль",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
            Spacer(
                modifier = Modifier
                    .height(96.dp)
            )
            SelectionRoleSection(
                radioOptions = radioOptions,
                radioState = radioState
            )
            Spacer(
                modifier = Modifier
                    .height(50.dp)
            )
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
                onClick = {
                    val userMode = if (radioState.value == "Студент") UserMode.Student
                                    else UserMode.Teacher

                    changeUserMode(userMode)
                    navigateToNext(userMode)
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                textColor = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRoleSelectionScreen() {
    RoleSelectionScreen()
}