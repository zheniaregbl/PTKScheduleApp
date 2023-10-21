package com.syndicate.ptkscheduleapp.ui.screens.course_selection_screen

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
import com.syndicate.ptkscheduleapp.ui.screens.course_selection_screen.components.SelectionCourseSection
import com.syndicate.ptkscheduleapp.ui.screens.course_selection_screen.components.SimpleButton
import com.syndicate.ptkscheduleapp.ui.theme.SecondThemeBackground

@Composable
fun CourseSelectionScreen(
    modifier: Modifier = Modifier
) {
    val radioOptions = listOf("1 курс", "2 курс", "3 курс", "4 курс")
    val radioState = remember {
        mutableStateOf(radioOptions[0])
    }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 150.dp
                ),
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
                text = "Выберите ваш курс",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
            Spacer(
                modifier = Modifier
                    .height(96.dp)
            )
            SelectionCourseSection(
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
                onClick = { },
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                textColor = Color.Black
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun PreviewCourseSelectionScreen() {
    CourseSelectionScreen()
}