package com.example.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.tracker.utils.converters.HistoricDaoConverter
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@TypeConverters(HistoricDaoConverter::class)
@Entity(tableName = "historic_table")
data class Historic (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var country: String? = "",
    var province: List<String>? = null ,

    @SerializedName("timeline")
    var timeLine: TimeLine?
) : Serializable





