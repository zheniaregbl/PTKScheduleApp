package com.syndicate.ptkscheduleapp.ui.screens.theme_screen.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components.LessonCard
import com.syndicate.ptkscheduleapp.ui.theme.SelectedBlue
import com.syndicate.ptkscheduleapp.ui.theme.ThemeMode

@Composable
fun ThemeCard(
    themeMode: ThemeMode,
    isDark: Boolean,
    isSelected: Boolean,
    backgroundColor: Color,
    borderColor: Color,
    textTheme: String,
    onClick: (ThemeMode) -> Unit = { },
) {

    AnimatedContent(
        targetState = isSelected,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = ""
    ) { selected ->
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = if (selected) SelectedBlue else Color.Transparent,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(
                        10.dp
                    )
            ) {
                LessonCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(10.dp),
                            clip = true,
                            spotColor = if (isDark) Color.Transparent else Color.Black.copy(alpha = 0.3f),
                            ambientColor = if (isDark) Color.Transparent else Color.Black.copy(alpha = 0.3f),
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            color = backgroundColor
                        )
                        .border(
                            width = 2.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            onClick(themeMode)
                        },
                    lessonItem = LessonItem(
                        time = "Время",
                        lessonTitle = "Название предмета",
                        teacher = "Преподаватель",
                        room = " "
                    ),
                    isDark = isDark
                )
            }

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )

            Text(
                text = textTheme,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = if (selected) SelectedBlue else MaterialTheme.colorScheme.secondary
            )

            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
        }
    }
}