package com.syndicate.ptkscheduleapp.info_functions

import com.syndicate.ptkscheduleapp.data.model.LessonItem

fun deleteEmptyLesson(
    schedule: List<LessonItem>?
): List<LessonItem>? {

    val newSchedule = ArrayList<LessonItem>()

    return if (schedule.isNullOrEmpty())
        schedule
    else {

        schedule.forEach {
            if (it.lessonTitle != "")
                newSchedule.add(it)
        }

        newSchedule
    }
}