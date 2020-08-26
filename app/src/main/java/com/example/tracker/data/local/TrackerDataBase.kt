package com.example.tracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tracker.data.local.dao.CountryDao
import com.example.tracker.data.local.dao.HistoricDao
import com.example.tracker.data.local.dao.NewsDao
import com.example.tracker.data.local.dao.StatisticDao
import com.example.tracker.data.local.entity.*

@Database(
    entities = [
        Country::class, CountryInfo::class, Historic::class,
        TimeLine::class, News::class, Statistic::class
    ],
    version = 23,
    exportSchema = false
)

abstract class TrackerDataBase : RoomDatabase() {
    abstract fun mNewsDao(): NewsDao
    abstract fun mCountryDao(): CountryDao
    abstract fun mStatisticDao(): StatisticDao
    abstract fun mHistoricDao(): HistoricDao
}

private lateinit var INSTANCE: TrackerDataBase

fun getDataBase(context: Context): TrackerDataBase {
    synchronized(TrackerDataBase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                TrackerDataBase::class.java,
                "tracker_database"
            ).fallbackToDestructiveMigration().build()
        }
    }

    return INSTANCE
}