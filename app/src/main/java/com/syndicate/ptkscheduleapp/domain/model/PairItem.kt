package com.syndicate.ptkscheduleapp.domain.model

data class PairItem(
    val dayOfWeek: String,
    val isUpper: Boolean,
    val pairNumber: Int,
    val subject: String,
    val room: String,
    val teacher: String,
    val subgroupNumber: Int,
    val time: String
)