package com.example.tracker.data.local

import com.example.tracker.data.local.entity.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


class LocalDataSourceImpl @Inject constructor(
    val dataBase: TrackerDataBase
) : LocalDataSource {

    override suspend fun getCountries() = withContext(Dispatchers.IO) {
        return@withContext dataBase.mCountryDao().getAllCountries()
    }

    override suspend fun sortByContinent(continentName: String) = withContext(Dispatchers.IO) {
        return@withContext dataBase.mCountryDao().sortByContinent(continent = continentName)
    }

    override suspend fun clearCountries() = withContext(Dispatchers.IO) {
        return@withContext dataBase.mCountryDao().clearAllCountries()
    }

    override suspend fun clearCountriesHistoric()  = withContext(Dispatchers.IO) {
        return@withContext dataBase.mCountryDao().clearHistoric()
    }

    override suspend fun getCountryNames() = withContext(Dispatchers.IO) {
        return@withContext dataBase.mCountryDao().getAllCountryNames()
    }

    override suspend fun insertCountries(listOfCountries: List<Country>) =
        withContext(Dispatchers.IO) {
            return@withContext dataBase.mCountryDao().insertCountries(data = listOfCountries)
        }

    override suspend fun insertCountriesHistoric(data: List<Historic>) =
        withContext(Dispatchers.IO) {
            Timber.d("LocalDataSource ::: insertCountriesHistoric == ${data?.size}")

            return@withContext dataBase.mCountryDao().insertHistoric(data)
        }

    override suspend fun getCountriesHistoric() = withContext(Dispatchers.IO) {
        Timber.d("LocalDataSource ::: getCountriesHistoric == ${dataBase.mCountryDao().getHistoric().size}")
        return@withContext dataBase.mCountryDao().getHistoric()
    }

    override suspend fun getOverallTimeLine() = withContext(Dispatchers.IO) {
        return@withContext dataBase.mStatisticDao().getTimeLine()
    }

    override suspend fun clearOverallTimeLine() = withContext(Dispatchers.IO) {
        return@withContext dataBase.mStatisticDao().clearHistoric()
    }


    override suspend fun getNews() = withContext(Dispatchers.IO) {
        return@withContext dataBase.mNewsDao().getAllNews() as ArrayList<News>
    }

    override suspend fun clearNews() = withContext(Dispatchers.IO) {
        return@withContext dataBase.mNewsDao().clearData()
    }

    override suspend fun insertNews(newsList: List<News>) = withContext(Dispatchers.IO) {
        return@withContext dataBase.mNewsDao().insert(data = newsList)
    }

    override suspend fun getStatistic() = withContext(Dispatchers.IO) {
        return@withContext dataBase.mStatisticDao().getAllStatistic()
    }

    override suspend fun deleteStatistic() = withContext(Dispatchers.IO) {
        return@withContext dataBase.mStatisticDao().deleteStatistic()
    }

    override suspend fun insertOverallTimeLine(timeLine: TimeLine) = withContext(Dispatchers.IO) {
        return@withContext dataBase.mStatisticDao().insertTimeLine(data = timeLine!!)
    }

    override suspend fun insertStatistic(statisticData: Statistic) = withContext(Dispatchers.IO) {
        return@withContext dataBase.mStatisticDao().insert(data = statisticData)
    }


}