package com.syndicate.ptkscheduleapp.ui.screens.course_selection_screen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.R
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

@Composable
fun SelectionCourseSection(
    modifier: Modifier = Modifier,
    radioOptions: List<String> = listOf("1 курс", "2 курс", "3 курс", "4 курс"),
    radioState: MutableState<String> = mutableStateOf("1 курс")
) {
    Column(
        modifier = modifier
            .selectableGroup()
    ) {
        radioOptions.forEach { label ->
            Row(
                modifier = Modifier
                    .height(30.dp)
                    .selectable(
                        selected = (radioState.value == label),
                        onClick = { radioState.value = label },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Crossfade(
                    targetState = radioState.value,
                    animationSpec = tween(225),
                    label = ""
                ) { targetState ->
                    Image(
                        modifier = Modifier
                            .size(30.dp),
                        imageVector = ImageVector.vectorResource(
                            id = if (targetState == label)
                                R.drawable.svg_radio_checked else R.drawable.svg_radio_default
                        ),
                        contentDescription = null
                    )
                }
                Spacer(
                    modifier = Modifier
                        .width(20.dp)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            }
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun PreviewSelectionCourseSection() {
    SelectionCourseSection()
}

@Composable
fun SimpleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    text: String = "Далее",
    fontSize: TextUnit = 20.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    textColor: Color = Color.Black
) {
    Box(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = textColor
        )
    }
}

@Preview(showBackground = false)
@Composable
fun PreviewSimpleButton() {
    SimpleButton()
}