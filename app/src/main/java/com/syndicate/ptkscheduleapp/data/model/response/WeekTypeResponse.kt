package com.syndicate.ptkscheduleapp.data.model.response

import com.google.gson.annotations.SerializedName

data class WeekTypeResponse(

    @SerializedName("week_is_upper")
    val isUpper: Boolean
)
