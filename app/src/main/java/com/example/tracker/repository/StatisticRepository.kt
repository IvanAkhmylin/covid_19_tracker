package com.example.tracker.repository

import android.app.Application
import com.example.tracker.api.Interfaces
import com.example.tracker.model.CountriesStatisticModel
import com.example.tracker.model.StatisticModel
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response

class StatisticRepository {
    fun getStatistic(application: Application, onResult: (StatisticModel) -> Unit, onFailure: (String) -> Unit  ) {
        val mOverallApi by lazy {
            Interfaces.initRetrofit(application)
        }

        mOverallApi.getStatistic().enqueue(object : Callback<StatisticModel> {
            override fun onFailure(call: Call<StatisticModel>, t: Throwable) {
                onFailure("ERROR MESSAGE: ${t.message}")
            }

            override fun onResponse(
                call: Call<StatisticModel>,
                response: Response<StatisticModel>
            ) {
                onResult(response.body()!!)
            }

        })
    }

    fun getCountryStatistic(application: Application, onResult: (List<CountriesStatisticModel>) -> Unit, onFailure: (String) -> Unit  ) {
        val mOverallApi by lazy {
            Interfaces.initRetrofit(application)
        }

        mOverallApi.getCountriesStatistic().enqueue(object : Callback<List<CountriesStatisticModel>> {
            override fun onFailure(call: Call<List<CountriesStatisticModel>>, t: Throwable) {
                onFailure("ERROR MESSAGE: ${t.message}")
            }

            override fun onResponse(
                call: Call<List<CountriesStatisticModel>>,
                response: Response<List<CountriesStatisticModel>>
            ) {
                onResult(response.body()!!)
            }


        })
    }


}