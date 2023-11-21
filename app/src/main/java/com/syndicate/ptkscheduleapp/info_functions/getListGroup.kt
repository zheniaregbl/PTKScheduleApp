package com.syndicate.ptkscheduleapp.info_functions

import org.json.JSONArray

fun getListGroup(groupJson: JSONArray): List<String> {

    val listGroup = ArrayList<String>()

    for (i in 0..<groupJson.length())
        listGroup.add(groupJson.getString(i))

    return listGroup
}