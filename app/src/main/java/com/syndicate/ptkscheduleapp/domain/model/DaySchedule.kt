package com.syndicate.ptkscheduleapp.domain.model

import com.syndicate.ptkscheduleapp.data.model.response.PairObject

data class DaySchedule(
    val listPairItem: List<PairObject>
)