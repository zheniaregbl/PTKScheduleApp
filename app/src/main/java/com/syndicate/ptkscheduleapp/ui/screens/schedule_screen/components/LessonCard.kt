package com.syndicate.ptkscheduleapp.ui.screens.schedule_screen.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import com.syndicate.ptkscheduleapp.ui.theme.GrayText
import com.syndicate.ptkscheduleapp.ui.theme.ThirdThemeBackground
import java.util.Random
import kotlin.math.max
import kotlin.math.min

@Composable
fun LessonCard(
    modifier: Modifier = Modifier,
    lessonItem: LessonItem = LessonItem(
        time = "8.30-10.10",
        lessonTitle = "Математика",
        teacher = "Ширина",
        room = "кабинет 410"
    )
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
            LessonInfo(
                lessonItem = lessonItem,
                isLast = true
            )
        }
    }
}

@Composable
fun LessonCard(
    modifier: Modifier = Modifier,
    lessonList: List<LessonItem> = listOf(
        LessonItem(),
        LessonItem()
    )
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
                lessonList.forEachIndexed { index, lessonItem ->
                    LessonInfo(
                        lessonItem = lessonItem,
                        isDivision = true,
                        subgroup = lessonItem.subgroupNumber,
                        isLast = index == lessonList.lastIndex
                    )
                }
            }
        }
    }
}

@Composable
fun LessonInfo(
    lessonItem: LessonItem = LessonItem(
        time = "8.30-10.10",
        lessonTitle = "Математика",
        teacher = "Ширина",
        room = "410"
    ),
    isDivision: Boolean = false,
    subgroup: Int = 1,
    isLast: Boolean = false
) {
    Column(
        modifier = Modifier
            .padding(vertical = 2.dp)
            .padding(end = 50.dp)
    ) {
        Text(
            text = lessonItem.time,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(
            modifier = Modifier
                .height(3.dp)
        )
        Text(
            text = lessonItem.lessonTitle,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = GrayText
        )
        Spacer(
            modifier = Modifier
                .height(3.dp)
        )
        if (lessonItem.teacher != "Не указан" && lessonItem.room != "Не указан")
            Text(
                text = "${lessonItem.teacher}, кабинет ${lessonItem.room}",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        if (isDivision) {
            Text(
                text = "п/г $subgroup",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
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
                        color = ThirdThemeBackground
                    )
            )
        }
    }
}

@Composable
fun ColorLine(
    modifier: Modifier = Modifier
) {
    val hsl = generateColor()

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
    LessonCard(
        lessonItem = LessonItem()
    )
}

@Preview(showBackground = false)
@Composable
fun PreviewLessonCardSomeLesson() {
    LessonCard(
        lessonList = listOf(
            LessonItem(),
            LessonItem()
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