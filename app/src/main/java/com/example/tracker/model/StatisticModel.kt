package com.example.tracker.model


data class StatisticModel(val cases: Int? = 0, val deaths: Int? = 0, val recovered: Int? = 0)

data class CountriesStatisticModel(
    val country: String? = "",
    val countryInfo: countryInfo,
    val cases: Int? = 0,
    val todayCases: Int? = 0,
    val deaths: Int? = 0,
    val todayDeaths: Int? = 0,
    val recovered: Int? = 0,
    val active: Int? = 0,
    val critical: Int? = 0,
    val casesPerOneMillion: Double? = 0.0,
    val deathsPerOneMillion: Double? = 0.0,
    val tests: Int? = 0
)

data class countryInfo(
    val _id: Int? = 0, val iso2: String? = "", val iso3: String? = "",
    val lat: Double? = 0.0, val long: Double? = 0.0, val flag: String? = "")
