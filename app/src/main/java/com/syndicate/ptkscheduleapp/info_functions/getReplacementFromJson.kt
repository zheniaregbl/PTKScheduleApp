package com.syndicate.ptkscheduleapp.info_functions

import android.util.Log
import com.syndicate.ptkscheduleapp.core.JsonFieldName
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import org.json.JSONObject
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun getReplacementFromJson(
    currentDate: LocalDate,
    weekAmount: Int,
    jsonObject: JSONObject,
    group: String
): List<List<List<LessonItem>>> {

    if (jsonObject.toString() == "")
        return emptyList()

    val allReplacement = ArrayList<List<List<LessonItem>>>()
    var weekReplacement = ArrayList<List<LessonItem>>()
    var dayReplacement = ArrayList<LessonItem>()

    var tempDate = getStartDate(currentDate, weekAmount)

    Log.d("checkReplacement", tempDate.toString())

    while (tempDate <= currentDate) {

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        try {

            val replacement = jsonObject
                .getJSONObject(tempDate.format(formatter))
                .getJSONArray(group)

            Log.d("checkReplacement", tempDate.format(formatter) + replacement.toString())

            for (i in 0..<replacement.length()) {

                val item = replacement.getJSONObject(i)

                dayReplacement.add(
                    LessonItem(
                        time = item.getString(JsonFieldName.time),
                        lessonTitle = item.getString(JsonFieldName.subject),
                        teacher = item.getString(JsonFieldName.teacher),
                        room = item.getString(JsonFieldName.room),
                        pairNumber = item.getInt(JsonFieldName.pairNumber),
                        isUpper = item.getBoolean(JsonFieldName.isUpper),
                        subgroupNumber = item.getInt(JsonFieldName.subgroupNumber)
                    )
                )
            }

            Log.d("checkReplacement", "success")

        } catch (_: Exception) {

        }

        weekReplacement.add(dayReplacement)

        dayReplacement = ArrayList()

        tempDate = tempDate.plusDays(1)

        if (tempDate.dayOfWeek == DayOfWeek.MONDAY) {
            allReplacement.add(weekReplacement)
            weekReplacement = ArrayList()
        }
    }

    if (allReplacement.size < weekAmount + 1)
        allReplacement.add(weekReplacement)

    return allReplacement
}

fun getStartDate(
    currentDate: LocalDate,
    weekAmount: Int
): LocalDate {

    var tempDate = currentDate

    while (tempDate.dayOfWeek != DayOfWeek.MONDAY)
        tempDate = tempDate.minusDays(1)

    return tempDate.minusWeeks(weekAmount.toLong())
}