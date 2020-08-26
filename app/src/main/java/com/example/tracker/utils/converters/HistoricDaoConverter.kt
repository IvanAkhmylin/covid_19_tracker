package com.example.tracker.utils.converters

import androidx.room.TypeConverter
import com.example.tracker.data.local.entity.TimeLine
import com.google.gson.Gson
import java.util.*

class HistoricDaoConverter {
    @TypeConverter
    fun toTimeLine(value: String): TimeLine? {
        return  Gson().fromJson(value, TimeLine::class.java)
    }

    @TypeConverter
    fun fromTimeLine(value: TimeLine?): String {
        return Gson().toJson(value)
    }


    @TypeConverter
    fun fromProvince(province: List<String>?): String {
        return province.toString()
    }

    @TypeConverter
    fun toProvince(province: String): List<String>? {
        val provinceList = ArrayList<String>()
        for (w in province.trim().split(",")) {
            if (w.isNotEmpty()) {
                provinceList.add(w)
            }
        }

        return provinceList
    }
}