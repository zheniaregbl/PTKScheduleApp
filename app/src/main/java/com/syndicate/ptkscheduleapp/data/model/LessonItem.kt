package com.syndicate.ptkscheduleapp.data.model

data class LessonItem(
    val time: String = "8.30-10.10",
    val lessonTitle: String = "Математика",
    val teacher: String = "Ширина",
    val room: String = "410",
    val pairNumber: Int = 1,
    val isUpper: Boolean = false,
    val subgroupNumber: Int = 0
)