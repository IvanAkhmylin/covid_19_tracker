package com.example.tracker.utils.converters

import androidx.room.TypeConverter
import com.example.tracker.data.local.entity.CountryInfo
import com.google.gson.Gson

class CountryDaoConverter {
    @TypeConverter
    fun fromCountryInfo(value: CountryInfo): String {
        val jsonString = Gson().toJson(value)
        return jsonString
    }

    @TypeConverter
    fun toCountryInfo(value: String): CountryInfo {
        val data = Gson().fromJson(value, CountryInfo::class.java)
        return data
    }

}