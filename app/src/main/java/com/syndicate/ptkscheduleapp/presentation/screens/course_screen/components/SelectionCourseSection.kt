package com.syndicate.ptkscheduleapp.presentation.screens.course_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.extension.colorsPalette
import com.syndicate.ptkscheduleapp.ui.theme.AppTheme
import com.syndicate.ptkscheduleapp.ui.theme.MainBlue
import com.syndicate.ptkscheduleapp.ui.theme.utils.LocalColorsPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.ThemeMode

@Composable
fun SelectionCourseSection(
    courseList: List<String> = listOf("1 курс", "2 курс", "3 курс", "4 курс"),
    courseProvider: () -> Int = { 0 },
    onCourseClick: (Int) -> Unit = { }
) {

    Column {

        courseList.forEachIndexed { index, label ->

            CourseItem(
                modifier = Modifier
                    .height(30.dp)
                    .clickable(
                        indication = null,
                        interactionSource = MutableInteractionSource()
                    ) {
                        onCourseClick(index)
                    },
                label = label,
                isSelected = index == courseProvider()
            )

            if (index != courseList.lastIndex) {

                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )
            }
        }
    }
}

@Composable
fun CourseItem(
    modifier: Modifier,
    label: String,
    isSelected: Boolean
) {

    val themeMode = LocalColorsPalette.current.themeMode

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(30.dp)
                .border(
                    width = 1.dp,
                    shape = CircleShape,
                    color = when {
                        isSelected && (themeMode == ThemeMode.LIGHT ||
                                themeMode == ThemeMode.CAPPUCCINO) -> MainBlue

                        else -> MaterialTheme.colorsPalette.contentColor
                    }
                ),
            contentAlignment = Alignment.Center
        ) {

            Row {

                AnimatedVisibility(
                    visible = isSelected,
                    enter = fadeIn(
                        animationSpec = spring()
                    ),
                    exit = fadeOut(
                        animationSpec = spring()
                    )
                ) {

                    Box(
                        modifier = Modifier
                            .padding(7.dp)
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(
                                color = when (themeMode) {
                                    ThemeMode.LIGHT, ThemeMode.CAPPUCCINO -> MainBlue
                                    else -> MaterialTheme.colorsPalette.contentColor
                                }
                            )
                    )
                }
            }
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
            color = MaterialTheme.colorsPalette.contentColor
        )
    }
}

@Preview
@Composable
private fun PreviewSelectionCourseSection() {
    AppTheme {
        SelectionCourseSection()
    }
}