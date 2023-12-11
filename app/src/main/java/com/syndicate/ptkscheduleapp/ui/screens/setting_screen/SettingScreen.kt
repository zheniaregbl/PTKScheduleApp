package com.syndicate.ptkscheduleapp.ui.screens.setting_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.R
import com.syndicate.ptkscheduleapp.ui.screens.setting_screen.components.SettingItem

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    navigateToTheme: () -> Unit = { }
) {

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp
                )
                .padding(
                    top = 35.dp
                ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 35.dp
                    )
            ) {
                Text(
                    text = "Настройки",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
            }

            LazyColumn {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        SettingItem(
                            modifier = Modifier
                                .fillMaxWidth(),
                            titleSetting = "Тема приложения",
                            descriptionSetting = "Изменение цветовой темы приложения",
                            imageResource = R.drawable.svg_theme,
                            sizeImage = 30.dp,
                            onClick = navigateToTheme
                        )

                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        SettingItem(
                            modifier = Modifier
                                .fillMaxWidth(),
                            titleSetting = "Измененить группу",
                            descriptionSetting = "Изменение группы для получения расписания",
                            imageResource = R.drawable.svg_loop,
                            sizeImage = 30.dp,
                            onClick = { }
                        )

                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        SettingItem(
                            modifier = Modifier
                                .fillMaxWidth(),
                            titleSetting = "Сохранение замен",
                            descriptionSetting = "Изменение количества недель, замены о которых " +
                                    "сохраняются в памяти вашего устройства",
                            imageResource = R.drawable.svg_folder,
                            sizeImage = 30.dp,
                            onClick = { }
                        )

                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                        )
                    }
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
fun PreviewSettingScreen() {
    SettingScreen()
}