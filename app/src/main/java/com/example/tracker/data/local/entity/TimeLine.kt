package com.example.tracker.data.local.entity

import androidx.room.*
import com.example.tracker.utils.converters.TimeLineConverter
import java.io.Serializable

@TypeConverters(TimeLineConverter::class)
@Entity(tableName = "timeLine_table")

data class TimeLine(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    var cases: Map<String, Int>,
    var deaths: Map<String, Int>,
    var recovered: Map<String, Int>
) : Serializable