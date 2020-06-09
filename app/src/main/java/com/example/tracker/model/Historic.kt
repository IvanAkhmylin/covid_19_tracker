package com.example.tracker.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.sql.Time

data class Historic (
    val country: String ,
    val province: List<String>,

    @SerializedName("timeline")
    val timeLine: TimeLine
) : Serializable

data class TimeLine(
    val cases: Map<String, Int>,
    val deaths: Map<String, Int>,
    val recovered: Map<String, Int>
): Serializable



