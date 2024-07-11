package com.syndicate.ptkscheduleapp.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.presentation.utils.ColorfulRipple
import com.syndicate.ptkscheduleapp.ui.theme.DarkRipple
import com.syndicate.ptkscheduleapp.ui.theme.LightRipple
import com.syndicate.ptkscheduleapp.ui.theme.utils.LocalColorsPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.ThemeMode

@Composable
fun ConfirmButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {

    val themeMode = LocalColorsPalette.current.themeMode

    CompositionLocalProvider(
        LocalRippleTheme provides ColorfulRipple(
            if (themeMode == ThemeMode.DARK) LightRipple else DarkRipple
        )
    ) {

        Box(
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .background(
                    color = backgroundColor
                )
                .clickable { onClick() }
                .padding(
                    horizontal = 82.dp,
                    vertical = 18.dp
                )
        ) {

            Text(
                text = "Далее",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                color = contentColor
            )
        }
    }
}