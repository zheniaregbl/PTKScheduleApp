package com.syndicate.ptkscheduleapp.info_functions

import android.util.Log
import com.syndicate.ptkscheduleapp.core.JsonFieldName
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun getReplacementFromJsonByDay(
    currentDate: LocalDate,
    jsonObject: JSONObject,
    group: String
): List<LessonItem> {

    if (jsonObject.toString() == "")
        return emptyList()

    val dayReplacement = ArrayList<LessonItem>()

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    try {

        val replacement = jsonObject
            .getJSONObject(currentDate.format(formatter))
            .getJSONArray(group)

        Log.d("checkReplacement", currentDate.format(formatter) + replacement.toString())

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

    return dayReplacement
}