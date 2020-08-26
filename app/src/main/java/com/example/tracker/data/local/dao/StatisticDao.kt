package com.example.tracker.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tracker.data.local.entity.Statistic

@Dao
interface StatisticDao : HistoricDao {

    @Query("SELECT * FROM statistic_table")
    suspend fun getAllStatistic(): Statistic

    @Query("DELETE FROM statistic_table")
    suspend fun deleteStatistic()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: Statistic)

}