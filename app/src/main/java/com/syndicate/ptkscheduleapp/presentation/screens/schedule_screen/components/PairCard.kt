package com.syndicate.ptkscheduleapp.presentation.screens.schedule_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.R
import com.syndicate.ptkscheduleapp.domain.model.PairItem
import com.syndicate.ptkscheduleapp.ui.theme.GrayText
import java.util.Random
import kotlin.math.max
import kotlin.math.min

@Composable
fun PairCard(
    modifier: Modifier = Modifier,
    pairItem: PairItem = PairItem(),
    isDark: Boolean = false,
    replacement: List<PairItem> = emptyList(),
) {
    Box(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(
                    top = 11.dp, start = 20.dp, bottom = 11.dp
                )
        ) {

            ColorLine(
                modifier = Modifier
                    .fillMaxHeight()
            )

            Spacer(
                modifier = Modifier
                    .width(10.dp)
            )

            PairInfo(
                pairItem = pairItem,
                isLast = true,
                isDark = isDark
            )
        }

        if (replacement.isNotEmpty())
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = 10.dp,
                        end = 10.dp
                    )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.svg_replacement),
                    contentDescription = null,
                    tint = GrayText
                )
            }
    }
}

@Composable
fun PairCard(
    modifier: Modifier = Modifier,
    pairList: List<PairItem> = listOf(
        PairItem(),
        PairItem()
    ),
    isDark: Boolean = false,
    replacement: List<PairItem> = emptyList()
) {
    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(
                    top = 11.dp, start = 20.dp, bottom = 11.dp, end = 50.dp
                )
        ) {
            ColorLine(
                modifier = Modifier
                    .fillMaxHeight()
            )
            Spacer(
                modifier = Modifier
                    .width(10.dp)
            )
            Column {
                pairList.forEachIndexed { index, lessonItem ->
                    PairInfo(
                        pairItem = lessonItem,
                        isDivision = true,
                        subgroup = lessonItem.subgroupNumber,
                        isLast = index == pairList.lastIndex,
                        isDark = isDark
                    )
                }
            }
        }

        if (replacement.isNotEmpty())
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = 10.dp,
                        end = 10.dp
                    )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.svg_replacement),
                    contentDescription = null,
                    tint = GrayText
                )
            }
    }
}

@Composable
fun PairInfo(
    pairItem: PairItem = PairItem(),
    isDivision: Boolean = false,
    subgroup: Int = 1,
    isLast: Boolean = false,
    isDark: Boolean = false
) {
    Column(
        modifier = Modifier
            .padding(vertical = 2.dp)
            .padding(end = 50.dp)
    ) {
        Text(
            text = pairItem.time,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color.White else Color.Black
        )
        Spacer(
            modifier = Modifier
                .height(3.dp)
        )
        Text(
            text = if (pairItem.subject == "") "Не будет" else pairItem.subject,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = GrayText
        )

        Spacer(
            modifier = Modifier
                .height(3.dp)
        )

        Text(
            text = "${pairItem.teacher}, кабинет ${pairItem.room.lowercase()}",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = if (isDark) Color.White else Color.Black
        )

        if (isDivision) {
            Text(
                text = "п/г $subgroup",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = if (isDark) Color.White else Color.Black
            )
        }

        if (!isLast) {

            Spacer(
                modifier = Modifier
                    .height(6.dp)
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        color = GrayText
                    )
            )
        }
    }
}

@Composable
fun ColorLine(
    modifier: Modifier = Modifier
) {
    val hsl by remember { mutableStateOf(generateColor()) }

    Spacer(
        modifier = modifier
            .width(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = Color.hsl(
                    hue = hsl.first,
                    saturation = if (hsl.second > 0.75f) 0.75f else hsl.second,
                    lightness = if (hsl.third < 0.6f) 0.6f else hsl.third
                )
            )
    )
}

@Preview(showBackground = false)
@Composable
fun PreviewLessonCardOneLesson() {
    PairCard(
        pairItem = PairItem()
    )
}

@Preview(showBackground = false)
@Composable
fun PreviewLessonCardSomeLesson() {
    PairCard(
        pairList = listOf(
            PairItem(),
            PairItem()
        )
    )
}

fun generateColor(): Triple<Float, Float, Float> {
    val rnd = Random()
    val intColor = android.graphics.Color.argb(
        255,
        rnd.nextInt(256),
        rnd.nextInt(256),
        rnd.nextInt(256)
    )

    return Color(intColor).toHsl()
}

fun Color.toHsl(): Triple<Float, Float, Float> {
    val r = red
    val g = green
    val b = blue

    val max = max(r, max(g, b))
    val min = min(r, min(g, b))

    val h: Float
    val s: Float
    val l = (max + min) / 2

    if (max == min) {
        // achromatic
        h = 0f
        s = 0f
    } else {
        val d = max - min
        s = if (l > 0.5f) d / (2 - max - min) else d / (max + min)
        h = when (max) {
            r -> (g - b) / d + (if (g < b) 6 else 0)
            g -> (b - r) / d + 2
            b -> (r - g) / d + 4
            else -> 0f
        }
    }

    return Triple(h * 60, s, l)
}