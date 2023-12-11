package com.syndicate.ptkscheduleapp.ui.screens.setting_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.R
import com.syndicate.ptkscheduleapp.ui.theme.GrayText

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    titleSetting: String = "Настройка 1",
    descriptionSetting: String = "Описание настройки",
    imageResource: Int = R.drawable.svg_theme,
    sizeImage: Dp = 30.dp,
    onClick: () -> Unit = { }
) {

    Row(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                }
            ) {
                onClick()
            }
            .composed { modifier },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    end = 30.dp
                )
        ) {
            Text(
                text = titleSetting,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )

            Text(
                text = descriptionSetting,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
            )
        }

        Box(
            modifier = Modifier
                .size(sizeImage),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = imageResource),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun PreviewSettingItem() {
    SettingItem(
        modifier = Modifier
            .fillMaxWidth()
    )
}