package com.example.tracker.ui.statistic

import android.app.Application
import com.example.tracker.api.Interfaces
import com.example.tracker.model.Statistic
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response

class StatisticRepository {
    fun getStatistic(application: Application, onResult: (Statistic) -> Unit, onFailure: (String) -> Unit  ) {
        val mOverallApi by lazy {
            Interfaces.initRetrofit(application)
        }

        mOverallApi.getStatistic().enqueue(object : Callback<Statistic> {
            override fun onFailure(call: Call<Statistic>, t: Throwable) {
                onFailure("ERROR MESSAGE: ${t.message}")
            }

            override fun onResponse(
                call: Call<Statistic>,
                response: Response<Statistic>
            ) {
                onResult(response.body()!!)
            }

        })
    }

}