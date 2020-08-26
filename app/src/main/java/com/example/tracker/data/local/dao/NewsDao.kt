package com.example.tracker.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tracker.data.local.entity.News

@Dao
interface NewsDao {

    @Query("SELECT * FROM news_table")
    suspend fun getAllNews(): List<News>

    @Query("DELETE FROM news_table")
    suspend fun clearData()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: List<News>)
}