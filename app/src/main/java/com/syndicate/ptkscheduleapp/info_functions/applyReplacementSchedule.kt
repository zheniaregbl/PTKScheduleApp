package com.syndicate.ptkscheduleapp.info_functions

import com.syndicate.ptkscheduleapp.data.model.LessonItem

fun applyReplacementSchedule(
    scheduleList: List<LessonItem>?,
    replacementList: List<LessonItem>?
): List<LessonItem>? {

    if (!replacementList.isNullOrEmpty() && !scheduleList.isNullOrEmpty()) {

        val replacementNumbers = ArrayList<Int>()
        val replacement = replacementList
        val removeList = ArrayList<LessonItem>()
        val schedule = scheduleList.toMutableList()

        replacement.forEach {
            if (it.pairNumber !in replacementNumbers) replacementNumbers.add(it.pairNumber)
        }

        schedule.forEach {

            if (it.pairNumber in replacementNumbers)
                removeList.add(it)
        }

        removeList.forEach {
            schedule.remove(it)
        }

        replacement.forEach {
            schedule.add(it)
        }

        schedule.sortBy { it.pairNumber }

        return schedule
    } else {
        return scheduleList
    }
}