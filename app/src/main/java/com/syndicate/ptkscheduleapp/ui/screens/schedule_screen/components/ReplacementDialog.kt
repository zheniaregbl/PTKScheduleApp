package com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.R
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import com.syndicate.ptkscheduleapp.presentation.screens.schedule_screen.components.PairCard
import com.syndicate.ptkscheduleapp.ui.theme.FirstThemeBackground
import com.syndicate.ptkscheduleapp.ui.theme.GrayText
import com.syndicate.ptkscheduleapp.ui.theme.GrayThirdTheme
import com.syndicate.ptkscheduleapp.ui.theme.SecondThemeBackground
import com.syndicate.ptkscheduleapp.ui.theme.ThirdThemeBackground

@Composable
fun ReplacementDialog(
    showDialog: Boolean = true,
    onDismissRequest: () -> Unit = { },
    main: List<LessonItem> = listOf(LessonItem()),
    replacement: List<LessonItem> = listOf(LessonItem()),
    isDarkTheme: Boolean = false
) {

    Log.d("checkMainLesson", main.joinToString())

    main.forEach {
        Log.d("checkMainLesson", (it.lessonTitle == "").toString())
    }

    CustomDialog(
        showDialog = showDialog,
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .background(
                    color = if (isDarkTheme) ThirdThemeBackground else FirstThemeBackground
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isDarkTheme) GrayThirdTheme else SecondThemeBackground
                        )
                        .padding(
                            vertical = 10.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Пара изменена!",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        fontSize = 24.sp,
                        color = GrayText,
                    )
                }

                /*Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 14.dp,
                            vertical = 20.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (main.size <= 1)
                        PairCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    clip = true,
                                    spotColor = if (isDarkTheme) Color.Transparent
                                    else Color.Black.copy(alpha = 0.3f),
                                    ambientColor = if (isDarkTheme) Color.Transparent
                                    else Color.Black.copy(alpha = 0.3f)
                                )
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.inversePrimary,
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            pairItem = if (main.isEmpty()) LessonItem(
                                time = replacement[0].time,
                                isAbsent = true,
                            ) else main[0],
                            isDark = isDarkTheme
                        )
                    else
                        PairCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    clip = true,
                                    spotColor = if (isDarkTheme) Color.Transparent
                                    else Color.Black.copy(alpha = 0.3f),
                                    ambientColor = if (isDarkTheme) Color.Transparent
                                    else Color.Black.copy(alpha = 0.3f)
                                )
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.inversePrimary,
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            pairList = main,
                            isDark = isDarkTheme
                        )

                    Spacer(
                        modifier = Modifier
                            .height(18.dp)
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.svg_arrow),
                        contentDescription = null,
                        tint = GrayText
                    )

                    Spacer(
                        modifier = Modifier
                            .height(18.dp)
                    )

                    if (replacement.size <= 1)
                        PairCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    clip = true,
                                    spotColor = if (isDarkTheme) Color.Transparent
                                    else Color.Black.copy(alpha = 0.3f),
                                    ambientColor = if (isDarkTheme) Color.Transparent
                                    else Color.Black.copy(alpha = 0.3f)
                                )
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.inversePrimary,
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            pairItem = replacement[0],
                            isDark = isDarkTheme
                        )
                    else
                        PairCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    clip = true,
                                    spotColor = if (isDarkTheme) Color.Transparent
                                    else Color.Black.copy(alpha = 0.3f),
                                    ambientColor = if (isDarkTheme) Color.Transparent
                                    else Color.Black.copy(alpha = 0.3f)
                                )
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.inversePrimary,
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            pairList = replacement,
                            isDark = isDarkTheme
                        )
                }*/
            }
        }
    }
}