package com.example.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")
data class News(
    var title: String = "",
    var resource: String = "",
    var time: String = "",
    var link: String = "",
    var image: String = "",
    var loadStatus: String = ""
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}