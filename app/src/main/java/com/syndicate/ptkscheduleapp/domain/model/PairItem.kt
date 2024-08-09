package com.syndicate.ptkscheduleapp.domain.model

data class PairItem(
    val dayOfWeek: String = "Понедельник",
    val isUpper: Boolean = false,
    val pairNumber: Int = 1,
    val subject: String = "Математика",
    val room: String = "410",
    val teacher: String = "Ширина",
    val subgroupNumber: Int = 0,
    val time: String = "8.30-10.10"
)