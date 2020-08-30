package com.example.tracker.data.local.entity

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tracker.utils.ExpansionUtils.timestampToDate

@Entity(tableName = "statistic_table")
data class Statistic(
    @PrimaryKey
    var updated: Long = 0,

    var cases: Int? = 0,
    var todayCases: Int? = 0,
    var deaths: Int? = 0,
    var todayDeaths: Int? = 0,
    var recovered: Int? = 0,
    var todayRecovered: Int? = 0,
    var active: Int? = 0,
    var critical: Int? = 0,
    var undefined: Int? = 0,
    var population: Long? = 0,
    var casesPerOneMillion: Double? = 0.0,
    var activePerOneMillion: Double? = 0.0,
    var recoveredPerOneMillion: Double? = 0.0,
    var deathsPerOneMillion: Double? = 0.0,
    var tests: Int? = 0,
    var testsPerOneMillion: Double? = 0.0,
    var affectedCountries: Int? = 0
)

