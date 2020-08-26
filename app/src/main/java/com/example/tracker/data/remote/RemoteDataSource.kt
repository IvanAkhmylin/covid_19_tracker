package com.example.tracker.data.remote

import com.example.tracker.data.local.entity.*
import com.example.tracker.utils.Result

interface RemoteDataSource  {

    suspend fun getGlobalStatistic(): Result<Statistic?>
    suspend fun getGlobalHistoric(): Result<TimeLine?>

    suspend fun getCountries(): Result<List<Country>?>
    suspend fun getHistoricForCountries(countries: String): Result<List<Historic>?>

    suspend fun getNews(query: String , locale: String,  onLoading: (Result<ArrayList<News>>)-> Unit)

}