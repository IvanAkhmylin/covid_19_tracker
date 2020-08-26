package com.example.tracker.data.local.dao

import androidx.room.*
import com.example.tracker.data.local.entity.Country

@Dao
interface CountryDao : HistoricDao{

    @Query("SELECT * FROM country_table")
    suspend fun getAllCountries(): List<Country>

    @Query("SELECT * FROM country_table WHERE continent = :continent")
    suspend fun sortByContinent(continent: String): List<Country>

    @Query("DELETE FROM country_table")
    suspend fun clearAllCountries()

    @Query("SELECT country_name FROM country_table")
    suspend fun getAllCountryNames(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountries(data: List<Country>)

}
