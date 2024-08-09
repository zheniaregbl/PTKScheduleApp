package com.syndicate.ptkscheduleapp.common.utils

import com.syndicate.ptkscheduleapp.domain.model.PairItem
import java.time.DayOfWeek
import java.time.LocalDate

object ScheduleUtils {

    fun groupDailyScheduleBySubgroup(
        dailySchedule: List<PairItem>
    ) : List<List<PairItem>> {

        var listSeveralPair = ArrayList<PairItem>()

        val schedule = ArrayList<List<PairItem>>()

        dailySchedule.forEachIndexed { index, pairItem ->

            if (pairItem.subgroupNumber == 0)
                schedule.add(listOf(pairItem))
            else {

                listSeveralPair.add(pairItem)

                if (index != dailySchedule.lastIndex && dailySchedule[index + 1].subgroupNumber == 0
                    || index == dailySchedule.lastIndex && dailySchedule.isNotEmpty()
                    || index != dailySchedule.lastIndex && dailySchedule[index + 1].pairNumber != pairItem.pairNumber
                ) {
                    schedule.add(listSeveralPair)
                    listSeveralPair = ArrayList()
                }
            }
        }

        return schedule
    }

    fun getWeekScheduleByWeekType(
        inputSchedule: List<List<PairItem>>,
        isUpperWeek: Boolean
    ): List<List<PairItem>> {

        val schedule = ArrayList<List<PairItem>>()

        inputSchedule.forEach { daySchedule ->
            schedule.add(filterScheduleByWeekType(daySchedule, isUpperWeek))
        }

        return schedule
    }

    private fun filterScheduleByWeekType(
        weekSchedule: List<PairItem>,
        isUpperWeek: Boolean
    ): List<PairItem> {

        if (weekSchedule.isEmpty())
            return emptyList()

        var schedule = ArrayList<PairItem>()
        val upperPair = ArrayList<PairItem>()
        val upperPairNumber = ArrayList<Int>()

        weekSchedule.forEach {

            if (!it.isUpper)
                schedule.add(it)
            else {
                upperPair.add(it)
                upperPairNumber.add(it.pairNumber)
            }
        }

        if (isUpperWeek) {

            val tempList = ArrayList<PairItem>()
            val forAllPairNumbers = ArrayList<Int>()

            upperPair.forEach {
                if (it.subgroupNumber == 0)
                    forAllPairNumbers.add(it.pairNumber)
            }

            schedule.forEach {
                if (it.pairNumber !in forAllPairNumbers && it.pairNumber !in upperPairNumber)
                    tempList.add(it)
            }

            schedule = tempList

            upperPair.forEach {
                schedule.add(it)
            }
        }

        schedule.sortBy { it.pairNumber }

        return schedule
    }

    fun getWeeksFromStartDate(
        startDate: LocalDate,
        weeksCount: Int
    ): List<List<LocalDate>> {
        val weeks = mutableListOf<List<LocalDate>>()
        var currentStartOfWeek = startDate

        while (currentStartOfWeek.dayOfWeek != DayOfWeek.MONDAY) {
            currentStartOfWeek = currentStartOfWeek.minusDays(1)
        }

        repeat(weeksCount) {
            val week = (0 until 7).map { currentStartOfWeek.plusDays(it.toLong()) }
            weeks.add(week)
            currentStartOfWeek = currentStartOfWeek.plusWeeks(1)
        }

        return weeks
    }

    fun getCurrentWeek(weeks: List<List<LocalDate>>, currentDate: LocalDate): Int {
        for (i in weeks.indices) {
            for (j in weeks[i].indices) {
                if (weeks[i][j] == currentDate) {
                    return i
                }
            }
        }

        return 0
    }

    fun getCurrentTypeWeek(
        typeWeekNow: Boolean,
        prevPage: Int,
        currentPage: Int
    ) = if (prevPage % 2 == currentPage % 2) typeWeekNow else !typeWeekNow
}