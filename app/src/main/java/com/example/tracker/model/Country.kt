package com.example.tracker.model

data class Country(
    val updated: Long = 0,
    var country: String? = "",
    val countryInfo: countryInfo,
    val cases: Int? = 0,
    val todayCases: Int? = 0,
    val deaths: Int? = 0,
    val todayDeaths: Int? = 0,
    val recovered: Int? = 0,
    val active: Int? = 0,
    val critical: Int? = 0,
    val population: Int? = 0,
    val casesPerOneMillion: Double? = 0.0,
    val deathsPerOneMillion: Double? = 0.0,
    val tests: Double? = 0.0,
    val testsPerOneMillion: Double? = 0.0,
    val continent: String? = ""
) {
    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as Country

        if (country != other.country) {
            return false
        }

        if (countryInfo._id != other.countryInfo._id) {
            return false
        }
        if (countryInfo.lat != other.countryInfo.lat) {
            return false
        }
        if (countryInfo.long != other.countryInfo.long) {
            return false
        }
        if (countryInfo.iso2 != other.countryInfo.iso2) {
            return false
        }
        if (countryInfo.iso3 != other.countryInfo.iso3) {
            return false
        }
        if (countryInfo.flag != other.countryInfo.flag) {
            return false
        }

        return true
    }
}


