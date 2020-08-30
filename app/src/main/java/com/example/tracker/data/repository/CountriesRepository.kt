package com.example.tracker.data.repository

import android.app.Application
import com.example.tracker.data.local.LocalDataSource
import com.example.tracker.data.local.entity.Country
import com.example.tracker.data.local.entity.Historic
import com.example.tracker.data.remote.RemoteDataSource
import com.example.tracker.utils.InternetConnectionManager
import com.example.tracker.utils.Result
import kotlinx.coroutines.*
import javax.inject.Inject

class CountriesRepository @Inject constructor(
    private val connectionManager: InternetConnectionManager,
    private val mLocalDataSource: LocalDataSource,
    private val mRemoteDataSource: RemoteDataSource
) {

    suspend fun getCountries(): Result<List<Country>?> =
        withContext(Dispatchers.IO) {
            if (connectionManager.isInternetConnectionExist) {
                val data = mRemoteDataSource.getCountries()
                when (data.status) {
                    Result.Status.SUCCESS -> {
                        data.let {
                            mLocalDataSource.clearCountries()
                            mLocalDataSource.insertCountries(data.data!!)
                            data
                        }
                    }
                    Result.Status.ERROR -> {
                        Result.error("ERROR")
                    }
                    else -> {
                        Result.loading(null)
                    }
                }
            } else {
                val data = mLocalDataSource.getCountries()
                if (data?.isNotEmpty()!!){
                    Result.success(data)
                }else{
                    Result.error("ERROR")
                }
            }
        }

    suspend fun getCountriesHistoric(countries: String): Result<List<Historic>?> =
        withContext(Dispatchers.IO) {
            if (connectionManager.isInternetConnectionExist) {
                val data = mRemoteDataSource.getHistoricForCountries(countries)
                when (data.status) {
                    Result.Status.SUCCESS -> {
                        mLocalDataSource.clearCountriesHistoric()
                        mLocalDataSource.insertCountriesHistoric(data.data!!)
                        data
                    }
                    Result.Status.ERROR -> {
                        Result.error("ERROR")
                    }
                    else -> {
                        Result.loading(null)
                    }
                }
            } else {
                val data = mLocalDataSource.getCountriesHistoric()
                if (data?.isNotEmpty()!!){
                    Result.success(data)
                }else{
                    Result.error("ERROR")
                }
            }
        }


    suspend fun sortByContinent(continent: String): List<Country> =
        withContext(Dispatchers.IO) {
            val data = mLocalDataSource.sortByContinent(continent)
            data
        }

    suspend fun getCountriesNames(): List<String> =
        withContext(Dispatchers.IO) {
            mLocalDataSource.getCountryNames()
        }


}