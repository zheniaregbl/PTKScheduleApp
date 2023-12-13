package com.syndicate.ptkscheduleapp.ui.screens.setting_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.R
import com.syndicate.ptkscheduleapp.ui.screens.course_selection_screen.components.SimpleButton
import com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components.CustomDialog
import com.syndicate.ptkscheduleapp.ui.theme.FirstThemeBackground
import com.syndicate.ptkscheduleapp.ui.theme.MainRed
import com.syndicate.ptkscheduleapp.ui.theme.ThirdThemeBackground

@Composable
fun NetworkConnectionDialogWithRetry(
    showDialog: Boolean = true,
    onDismissRequest: () -> Unit = { },
    havaRetry: Boolean = true,
    onRetry: () -> Unit = { },
    isDarkTheme: Boolean = false
) {
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
                        .padding(
                            vertical = 14.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "У вас нет интернета",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    Image(
                        modifier = Modifier
                            .size(140.dp),
                        imageVector = ImageVector.vectorResource(
                            id = if (isDarkTheme) R.drawable.svg_wifi_white else R.drawable.svg_wifi_black
                        ),
                        contentDescription = null
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(30.dp)
                )

                if (havaRetry)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                bottom = 20.dp
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        SimpleButton(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    color = MainRed
                                )
                                .padding(
                                    horizontal = 40.dp,
                                    vertical = 18.dp
                                ),
                            onClick = {
                                onRetry()
                                onDismissRequest()
                            },
                            text = "Попробовать снова",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            textColor = Color.White
                        )
                    }
                else
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                bottom = 20.dp
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Расписание и замены \n" +
                                    "не будут обновлены",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center
                        )
                    }
            }
        }
    }
}