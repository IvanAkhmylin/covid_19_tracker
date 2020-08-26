package com.example.tracker.data.local

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tracker.data.local.entity.*

interface LocalDataSource {

    // CountryDao
    suspend fun getCountries(): List<Country>?
    suspend fun sortByContinent(continent: String): List<Country>
    suspend fun getCountryNames(): List<String>
    suspend fun insertCountries(data: List<Country>)
    suspend fun insertCountriesHistoric(data: List<Historic>)
    suspend fun getCountriesHistoric(): List<Historic>?
    suspend fun clearCountries()
    suspend fun clearCountriesHistoric()

    //NewsDao
    suspend fun getNews(): List<News>
    suspend fun insertNews(data: List<News>)
    suspend fun clearNews()

    //StatisticDao
    suspend fun getStatistic(): Statistic?
    suspend fun insertStatistic(data: Statistic)
    suspend fun insertOverallTimeLine(data: TimeLine)
    suspend fun getOverallTimeLine(): TimeLine?
    suspend fun deleteStatistic()
    suspend fun clearOverallTimeLine()

}
