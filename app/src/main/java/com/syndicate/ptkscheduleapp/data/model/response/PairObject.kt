package com.syndicate.ptkscheduleapp.data.model.response

import com.google.gson.annotations.SerializedName
import com.syndicate.ptkscheduleapp.domain.model.PairItem

data class PairObject(

    @SerializedName("dayOfWeek")
    val dayOfWeek: String,

    @SerializedName("isUpper")
    val isUpper: Boolean,

    @SerializedName("pairNumber")
    val pairNumber: Int,

    @SerializedName("subject")
    val subject: String,

    @SerializedName("cabinet")
    val room: String,

    @SerializedName("teacher")
    val teacher: String,

    @SerializedName("subgroupNumber")
    val subgroupNumber: Int,

    @SerializedName("time")
    val time: String
) {

    fun toPairItem() = PairItem(
        dayOfWeek,
        isUpper,
        pairNumber,
        subject,
        room,
        teacher,
        subgroupNumber,
        time
    )
}