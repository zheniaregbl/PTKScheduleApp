package com.syndicate.ptkscheduleapp.info_functions

import com.syndicate.ptkscheduleapp.core.JsonFieldName
import com.syndicate.ptkscheduleapp.data.model.LessonItem
import org.json.JSONArray

fun getScheduleFromJson(
    scheduleJson: JSONArray
): List<List<LessonItem>> {

    val scheduleList = ArrayList<List<LessonItem>>()
    var currentDayOfWeek = ""
    var tempList = ArrayList<LessonItem>()

    for (i in 0..<scheduleJson.length()) {
        val onePair = scheduleJson.getJSONObject(i)
        val tempDayOfWeek = onePair.getString(JsonFieldName.day)

        if (currentDayOfWeek.isEmpty())
            currentDayOfWeek = tempDayOfWeek
        else {
            if (currentDayOfWeek != tempDayOfWeek) {
                scheduleList.add(tempList)

                tempList = ArrayList()
                currentDayOfWeek = tempDayOfWeek
            }
        }

        if (onePair.getString(JsonFieldName.subject).isNotEmpty()) {
            val lessonItem = LessonItem(
                time = onePair.getString(JsonFieldName.time),
                lessonTitle = onePair.getString(JsonFieldName.subject),
                teacher = onePair.getString(JsonFieldName.teacher),
                room = onePair.getString(JsonFieldName.room),
                subgroupNumber = onePair.getInt(JsonFieldName.subgroupNumber)
            )

            tempList.add(lessonItem)
        }
    }

    scheduleList.add(tempList)

    return scheduleList
}