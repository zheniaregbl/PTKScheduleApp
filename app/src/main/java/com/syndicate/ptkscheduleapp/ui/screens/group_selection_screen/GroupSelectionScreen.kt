package com.syndicate.ptkscheduleapp.ui.screens.group_selection_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.ui.screens.group_selection_screen.components.GroupPicker
import com.syndicate.ptkscheduleapp.ui.screens.group_selection_screen.components.rememberPickerState
import com.syndicate.ptkscheduleapp.ui.theme.GrayText

@Composable
fun GroupSelectionScreen(
    modifier: Modifier = Modifier
) {
    val values = remember { (1..99).map { it.toString() } }
    val valuesPickerState = rememberPickerState()
    val listGroup = listOf(
        "1991", "1992", "1993", "1994", "1995",
        "1996", "1997", "1998", "1999", "1990"
    )

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 70.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Выберите группу",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 36.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
            GroupPicker(
                state = valuesPickerState,
                items = listGroup,
                visibleItemsCount = 5,
                modifier = Modifier.width(130.dp),
                textModifier = Modifier.padding(8.dp),
                textStyle = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGroupSelectionScreen() {
    GroupSelectionScreen()
}