package com.example.tracker.utils.converters

import androidx.room.TypeConverter

class TimeLineConverter {
    @TypeConverter
    fun fromMap(value :Map<String, Int>): String{
        return value.toString()
    }

    @TypeConverter
    fun toMap(value: String) : Map<String,Int>{
        val map = value.substring(1, value.length -1).split(",").associate {
            val (left, right) = it.split("=")
            left to right.toInt()
        }
        return map
    }
}