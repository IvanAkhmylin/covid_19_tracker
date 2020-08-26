package com.example.tracker.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tracker.data.local.entity.Historic
import com.example.tracker.data.local.entity.TimeLine

@Dao
interface HistoricDao {

    @Query("SELECT * FROM historic_table")
    suspend fun getHistoric(): List<Historic>

    @Query("SELECT * FROM timeLine_table")
    suspend fun getTimeLine(): TimeLine?

    @Query("DELETE FROM historic_table")
    suspend fun clearHistoric()

    @Insert()
    suspend fun insertHistoric(data: List<Historic>?)

    @Insert()
    suspend fun insertTimeLine(data: TimeLine)

}
