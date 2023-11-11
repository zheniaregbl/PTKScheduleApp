package com.syndicate.ptkscheduleapp.info_functions

import com.syndicate.ptkscheduleapp.data.model.LessonItem

fun filterSchedule(
    inputSchedule: List<LessonItem>?,
    isUpper: Boolean
): List<LessonItem> {

    val schedule = if (inputSchedule.isNullOrEmpty()) {
        listOf(
            LessonItem(), LessonItem(), LessonItem(), LessonItem()
        )
    } else {
        inputSchedule
    }

    val resultList = ArrayList<LessonItem>()
    val upperPair = ArrayList<LessonItem>()
    val numbers = ArrayList<Int>()
    val upperNumbers = ArrayList<Int>()

    schedule.forEach {
        if (!it.isUpper) {
            resultList.add(it)
            numbers.add(it.pairNumber)
        }
        else {
            upperPair.add(it)
            upperNumbers.add(it.pairNumber)
        }
    }

    for (i in 0..<resultList.size) {
        val pair = resultList[i]

        if (isUpper) {
            if (pair.pairNumber in upperNumbers) {
                upperPair.forEach {
                    if (pair.pairNumber == it.pairNumber)
                        resultList[i] = it
                }
            }
        }
    }

    upperPair.forEach {
        if (it.pairNumber !in numbers && isUpper) {
            resultList.add(it)
        }
    }

    resultList.sortBy { it.pairNumber }

    return resultList
}