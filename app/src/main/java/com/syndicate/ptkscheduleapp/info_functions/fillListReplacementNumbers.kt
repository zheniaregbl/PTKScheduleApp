package com.syndicate.ptkscheduleapp.info_functions

import com.syndicate.ptkscheduleapp.data.model.LessonItem

fun fillListReplacementNumber(
    replacement: List<LessonItem>?
): List<Int> {
    val list = ArrayList<Int>()

    if (!replacement.isNullOrEmpty()) {

        replacement.forEach {
            if (it.pairNumber !in list) list.add(it.pairNumber)
        }

        return list
    }

    return emptyList()
}