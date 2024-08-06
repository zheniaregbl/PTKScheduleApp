package com.syndicate.ptkscheduleapp.data.model.response

import com.google.gson.annotations.SerializedName

data class ScheduleResponse(

    @SerializedName("result")
    val listPair: List<PairObject>
)