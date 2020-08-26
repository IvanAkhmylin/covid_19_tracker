package com.example.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countryInfo_table")
data class CountryInfo(
    @PrimaryKey var id: Int? = 0,
    var iso2: String? = "",
    var iso3: String? = "",
    var lat: Double? = 0.0,
    var long: Double? = 0.0,
    var flag: String? = ""
)