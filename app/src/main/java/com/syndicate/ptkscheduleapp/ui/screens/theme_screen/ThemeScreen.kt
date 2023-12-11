package com.syndicate.ptkscheduleapp.ui.screens.theme_screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.R
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components.LessonCard
import com.syndicate.ptkscheduleapp.ui.screens.theme_screen.components.ThemeCard
import com.syndicate.ptkscheduleapp.ui.theme.FirstThemeBackground
import com.syndicate.ptkscheduleapp.ui.theme.FourthThemeBackground
import com.syndicate.ptkscheduleapp.ui.theme.GrayText
import com.syndicate.ptkscheduleapp.ui.theme.GrayThirdTheme
import com.syndicate.ptkscheduleapp.ui.theme.MainBlue
import com.syndicate.ptkscheduleapp.ui.theme.SecondThemeBackground
import com.syndicate.ptkscheduleapp.ui.theme.SelectedBlue
import com.syndicate.ptkscheduleapp.ui.theme.ThemeMode
import com.syndicate.ptkscheduleapp.ui.theme.ThirdThemeBackground

@Composable
fun ThemeScreen(
    modifier: Modifier = Modifier,
    navigateToBack: () -> Unit = { },
    changeTheme: (ThemeMode) -> Unit = { },
    userThemeMode: ThemeMode = ThemeMode.FIRST
) {

    var selectedTheme by remember {
        mutableStateOf(userThemeMode)
    }

    val backgroundColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.onPrimary,
        label = "",
        animationSpec = tween(400, easing = LinearEasing)
    )

    val borderColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.inversePrimary,
        label = "",
        animationSpec = tween(400, easing = LinearEasing)
    )

    Box(
        modifier = modifier
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 20.dp
                )
                .padding(
                    top = 35.dp
                )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 35.dp
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Изменить тему",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(10.dp),
                                clip = true,
                                spotColor = Color.Black.copy(alpha = 0.3f),
                                ambientColor = Color.Black.copy(alpha = 0.3f)
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
                            .clickable(
                                indication = null,
                                interactionSource = remember {
                                    MutableInteractionSource()
                                }
                            ) { navigateToBack() },
                        contentAlignment = Alignment.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .size(44.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(
                                        end = 2.dp
                                    ),
                                imageVector = ImageVector.vectorResource(id = R.drawable.svg_arrow_back),
                                contentDescription = null,
                                tint = MainBlue
                            )
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    ThemeCard(
                        themeMode = ThemeMode.FIRST,
                        isDark = false,
                        isSelected = selectedTheme == ThemeMode.FIRST,
                        backgroundColor = FirstThemeBackground,
                        borderColor = SecondThemeBackground,
                        textTheme = "Светлый режим",
                        onClick = { themeMode ->

                            selectedTheme = themeMode
                            changeTheme(themeMode)
                        },
                    )
                }

                item {
                    ThemeCard(
                        themeMode = ThemeMode.SECOND,
                        isDark = false,
                        isSelected = selectedTheme == ThemeMode.SECOND,
                        backgroundColor = SecondThemeBackground,
                        borderColor = FirstThemeBackground,
                        textTheme = "Светло-серый режим",
                        onClick = { themeMode ->

                            selectedTheme = themeMode
                            changeTheme(themeMode)
                        },
                    )
                }

                item {
                    ThemeCard(
                        themeMode = ThemeMode.THIRD,
                        isDark = true,
                        isSelected = selectedTheme == ThemeMode.THIRD,
                        backgroundColor = ThirdThemeBackground,
                        borderColor = GrayThirdTheme,
                        textTheme = "Тёмно-серый режим",
                        onClick = { themeMode ->

                            selectedTheme = themeMode
                            changeTheme(themeMode)
                        },
                    )
                }

                item {
                    ThemeCard(
                        themeMode = ThemeMode.FOURTH,
                        isDark = true,
                        isSelected = selectedTheme == ThemeMode.FOURTH,
                        backgroundColor = FourthThemeBackground,
                        borderColor = ThirdThemeBackground,
                        textTheme = "Тёмный режим",
                        onClick = { themeMode ->

                            selectedTheme = themeMode
                            changeTheme(themeMode)
                        },
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewThemeScreen() {
    ThemeScreen(
        modifier = Modifier
            .fillMaxSize()
    )
}