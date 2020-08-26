package com.example.tracker.data.local.entity

import androidx.room.*
import com.example.tracker.utils.converters.CountryDaoConverter

@TypeConverters(CountryDaoConverter::class)
@Entity(tableName = "country_table")
data class Country(
    var updated: Long = 0,
    @PrimaryKey
    @ColumnInfo(name = "country_name")
    var country: String = "",
    var countryInfo: CountryInfo = CountryInfo(),
    var cases: Int? = 0,
    var todayCases: Int? = 0,
    var deaths: Int? = 0,
    var todayDeaths: Int? = 0,
    var recovered: Int? = 0,
    var active: Int? = 0,
    var critical: Int? = 0,
    var population: Int? = 0,
    var casesPerOneMillion: Double? = 0.0,
    var deathsPerOneMillion: Double? = 0.0,
    var tests: Double? = 0.0,
    var testsPerOneMillion: Double? = 0.0,
    var continent: String? = ""
) {

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as Country

        if (country != other.country) {
            return false
        }

        if (countryInfo.id != other.countryInfo.id) {
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



