package com.syndicate.ptkscheduleapp.info_functions

import com.syndicate.ptkscheduleapp.data.model.LessonItem

fun filterSchedule(
    inputSchedule: List<LessonItem>?,
    isUpper: Boolean
): List<LessonItem> {

    val schedule = if (inputSchedule.isNullOrEmpty()) {
        return emptyList()
    } else {
        inputSchedule
    }

    var resultList = ArrayList<LessonItem>()
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

    if (isUpper) {

        val tempList = ArrayList<LessonItem>()
        val forAllPairNumbers = ArrayList<Int>()

        upperPair.forEach {
            if (it.subgroupNumber == 0)
                forAllPairNumbers.add(it.pairNumber)
        }

        resultList.forEach {
            if (it.pairNumber !in forAllPairNumbers && it.pairNumber !in upperNumbers)
                tempList.add(it)
        }

        resultList = tempList

        upperPair.forEach {
            resultList.add(it)
        }
    }

    resultList.sortBy { it.pairNumber }

    return resultList
}