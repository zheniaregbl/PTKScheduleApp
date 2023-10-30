package com.syndicate.ptkscheduleapp.ui.screens.group_selection_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.data.model.UserMode
import com.syndicate.ptkscheduleapp.ui.screens.course_selection_screen.components.SimpleButton
import com.syndicate.ptkscheduleapp.ui.screens.group_selection_screen.components.GroupPicker
import com.syndicate.ptkscheduleapp.ui.screens.group_selection_screen.components.rememberPickerState
import com.syndicate.ptkscheduleapp.ui.theme.GrayText
import com.syndicate.ptkscheduleapp.ui.theme.SecondThemeBackground

@Composable
fun GroupSelectionScreen(
    modifier: Modifier = Modifier,
    navigateToNext: () -> Unit = { },
    userMode: UserMode = UserMode.Student
) {
    val valuesPickerState = rememberPickerState()
    val listGroup = if (userMode == UserMode.Student) listOf(
        "1991", "1992", "1993", "1994", "1995",
        "1996", "1997", "1998", "1999", "1990"
    ) else listOf(
        "Цымбалюк Л.Н.", "Кручинина О.А.", "Сазонова Н.В.", "Дубогрей А.Е."
    )

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
                text = if (userMode == UserMode.Student) "Выберите группу"
                    else "Выберите\nпреподавателя",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 36.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier
                    .height(70.dp)
            )
            GroupPicker(
                state = valuesPickerState,
                items = listGroup,
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
                onClick = navigateToNext,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                textColor = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGroupSelectionScreen() {
    GroupSelectionScreen()
}