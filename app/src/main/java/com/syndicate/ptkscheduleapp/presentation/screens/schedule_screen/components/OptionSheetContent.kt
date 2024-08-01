package com.syndicate.ptkscheduleapp.presentation.screens.schedule_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.R
import com.syndicate.ptkscheduleapp.extension.advanceShadow
import com.syndicate.ptkscheduleapp.extension.colorPalette
import com.syndicate.ptkscheduleapp.extension.colorsPalette
import com.syndicate.ptkscheduleapp.presentation.utils.ColorfulRipple
import com.syndicate.ptkscheduleapp.presentation.utils.setupSystemBars
import com.syndicate.ptkscheduleapp.ui.theme.AppTheme
import com.syndicate.ptkscheduleapp.ui.theme.CustomGreen
import com.syndicate.ptkscheduleapp.ui.theme.DarkRipple
import com.syndicate.ptkscheduleapp.ui.theme.DarkShadow
import com.syndicate.ptkscheduleapp.ui.theme.GrayText
import com.syndicate.ptkscheduleapp.ui.theme.LightBlue
import com.syndicate.ptkscheduleapp.ui.theme.LightRed
import com.syndicate.ptkscheduleapp.ui.theme.LightRipple
import com.syndicate.ptkscheduleapp.ui.theme.LightShadow
import com.syndicate.ptkscheduleapp.ui.theme.SandColor
import com.syndicate.ptkscheduleapp.ui.theme.SelectedBlue
import com.syndicate.ptkscheduleapp.ui.theme.utils.LocalColorsPalette
import com.syndicate.ptkscheduleapp.ui.theme.utils.ThemeMode
import kotlinx.coroutines.delay

@Composable
fun OptionSheetContent(
    enableChangeTheme: Boolean = true,
    onChangeTheme: (ThemeMode, Offset) -> Unit = { _: ThemeMode, _: Offset -> },
    lockChangeTheme: () -> Unit = { },
    unlockChangeTheme: () -> Unit = { }
) {

    val colorBorder = MaterialTheme.colorsPalette.contentColor.copy(alpha = 0.3f)

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = 25.dp,
            topEnd = 25.dp
        ),
        color = MaterialTheme.colorsPalette.backgroundColor
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {

                    drawLine(
                        color = colorBorder,
                        start = Offset(x = 25.dp.toPx(), y = 0.dp.toPx()),
                        end = Offset(x = size.width - 25.dp.toPx(), y = 0.dp.toPx()),
                        strokeWidth = 4.dp.toPx()
                    )

                    drawLine(
                        color = colorBorder,
                        start = Offset(x = 0.dp.toPx(), y = 25.dp.toPx()),
                        end = Offset(x = 0.dp.toPx(), y = size.height),
                        strokeWidth = 4.dp.toPx()
                    )

                    drawLine(
                        color = colorBorder,
                        start = Offset(x = size.width, y = 25.dp.toPx()),
                        end = Offset(x = size.width, y = size.height),
                        strokeWidth = 4.dp.toPx()
                    )

                    drawArc(
                        color = colorBorder,
                        startAngle = -90f,
                        sweepAngle = -90f,
                        useCenter = false,
                        topLeft = Offset(x = 0f, y = 0.dp.toPx()),
                        size = Size(25.dp.toPx() * 2, 25.dp.toPx() * 2),
                        style = Stroke(width = 4.dp.toPx())
                    )

                    drawArc(
                        color = colorBorder,
                        startAngle = 270f,
                        sweepAngle = 90f,
                        useCenter = false,
                        topLeft = Offset(
                            x = size.width - 25.dp.toPx() * 2,
                            y = 0.dp.toPx()
                        ),
                        size = Size(25.dp.toPx() * 2, 25.dp.toPx() * 2),
                        style = Stroke(width = 4.dp.toPx())
                    )
                }
                .padding(
                    horizontal = 10.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .size(
                        width = 60.dp,
                        height = 3.dp
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = colorBorder)
            )

            Spacer(modifier = Modifier.height(40.dp))

            ChangeGroupSection()

            ChangeThemeSection(
                enableChangeTheme = enableChangeTheme,
                onClickThemeItem = onChangeTheme,
                lockChangeTheme = lockChangeTheme,
                unlockChangeTheme = unlockChangeTheme
            )

            Spacer(modifier = Modifier.height(20.dp))

            Spacer(
                modifier = Modifier
                    .height(
                        WindowInsets
                            .navigationBars
                            .asPaddingValues()
                            .calculateBottomPadding()
                    )
            )
        }
    }
}

@Composable
private fun ChangeGroupSection(
    onClick: () -> Unit = { }
) {

    val themeMode = LocalColorsPalette.current.themeMode
    val colorBorder = MaterialTheme.colorsPalette.contentColor.copy(alpha = 0.1f)

    CompositionLocalProvider(
        LocalRippleTheme provides ColorfulRipple(
            when (themeMode) {
                ThemeMode.DARK, ThemeMode.GRAY -> LightRipple
                else -> DarkRipple
            }
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .advanceShadow(
                    color = when (themeMode) {
                        ThemeMode.DARK, ThemeMode.GRAY -> LightShadow
                        else -> DarkShadow
                    },
                    blurRadius = 6.dp,
                    offsetY = 4.dp
                )
                .clip(RoundedCornerShape(10.dp))
                .background(
                    color = MaterialTheme.colorsPalette.backgroundColor
                )
                .clickable { onClick() }
                .border(
                    width = 3.dp,
                    shape = RoundedCornerShape(10.dp),
                    color = colorBorder
                )
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.svg_group),
                    contentDescription = null,
                    tint = GrayText
                )

                Spacer(modifier = Modifier.width(20.dp))

                Text(
                    text = "Изменить группу",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorsPalette.contentColor
                )
            }

            Icon(
                modifier = Modifier
                    .rotate(180f)
                    .size(18.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.svg_arrow_back),
                contentDescription = null,
                tint = GrayText
            )
        }
    }
}

@Composable
private fun ChangeThemeSection(
    enableChangeTheme: Boolean = true,
    onClickThemeItem: (ThemeMode, Offset) -> Unit = { _: ThemeMode, _: Offset -> },
    lockChangeTheme: () -> Unit = { },
    unlockChangeTheme: () -> Unit = { }
) {

    val themeMode = LocalColorsPalette.current.themeMode
    val colorBorder = MaterialTheme.colorsPalette.contentColor.copy(alpha = 0.1f)

    LaunchedEffect(enableChangeTheme) {
        if (!enableChangeTheme) {
            delay(800)
            unlockChangeTheme()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .advanceShadow(
                color = when (themeMode) {
                    ThemeMode.DARK, ThemeMode.GRAY -> LightShadow
                    else -> DarkShadow
                },
                blurRadius = 6.dp,
                offsetY = 4.dp
            )
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = MaterialTheme.colorsPalette.backgroundColor
            )
            .border(
                width = 3.dp,
                shape = RoundedCornerShape(10.dp),
                color = colorBorder
            )
            .padding(
                top = 20.dp,
                bottom = 10.dp
            ),
    ) {

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.svg_theme),
                contentDescription = null,
                tint = GrayText
            )

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = "Изменить тему",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = MaterialTheme.colorsPalette.contentColor
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            ThemeMode.entries.forEach { item ->

                CompositionLocalProvider(
                    LocalRippleTheme provides ColorfulRipple(
                        when (item) {
                            ThemeMode.DARK, ThemeMode.GRAY -> LightRipple
                            else -> DarkRipple
                        }
                    )
                ) {

                    ThemeBox(
                        modifier = Modifier
                            .padding(10.dp)
                            .advanceShadow(
                                color = when (themeMode) {
                                    ThemeMode.DARK, ThemeMode.GRAY -> LightShadow
                                    else -> DarkShadow
                                },
                                blurRadius = 8.dp,
                                offsetY = 2.dp
                            )
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(10.dp)),
                        themeMode = item,
                        enable = enableChangeTheme,
                        onClick = {
                            lockChangeTheme()
                            onClickThemeItem(item, it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeBox(
    modifier: Modifier,
    themeMode: ThemeMode,
    enable: Boolean,
    onClick: (Offset) -> Unit
) {

    val currentThemeMode = LocalColorsPalette.current.themeMode
    var offset = remember { Offset(0f, 0f) }

    Box(
        modifier = modifier
            .clickable(
                enabled = enable && themeMode != currentThemeMode
            ) { onClick(offset) }
            .onGloballyPositioned {
                offset = Offset(
                    x = it.positionInWindow().x + it.size.width / 2,
                    y = it.positionInWindow().y + it.size.height / 2
                )
            }
            .background(
                color = themeMode.colorPalette.backgroundColor
            )
            .border(
                width = 2.dp,
                shape = RoundedCornerShape(10.dp),
                color = if (currentThemeMode == themeMode) SelectedBlue
                else themeMode.colorPalette.otherColor
            )
    ) {

        Box(
            modifier = Modifier
                .padding(
                    start = 8.dp
                )
                .fillMaxHeight()
                .padding(vertical = 12.dp)
                .clip(RoundedCornerShape(100))
                .width(4.dp)
                .background(
                    color = when (themeMode) {
                        ThemeMode.LIGHT -> LightRed
                        ThemeMode.CAPPUCCINO -> CustomGreen
                        ThemeMode.GRAY -> SandColor
                        ThemeMode.DARK -> LightBlue
                    }
                )
        )
    }
}

@Preview
@Composable
private fun PreviewOptionSheetContentLight() {
    AppTheme {
        OptionSheetContent()
    }
}

@Preview
@Composable
private fun PreviewOptionSheetContentCappuccino() {
    AppTheme(
        themeMode = ThemeMode.CAPPUCCINO
    ) {
        OptionSheetContent()
    }
}

@Preview
@Composable
private fun PreviewOptionSheetContentGray() {
    AppTheme(
        themeMode = ThemeMode.GRAY
    ) {
        OptionSheetContent()
    }
}

@Preview
@Composable
private fun PreviewOptionSheetContentDark() {
    AppTheme(
        themeMode = ThemeMode.DARK
    ) {
        OptionSheetContent()
    }
}