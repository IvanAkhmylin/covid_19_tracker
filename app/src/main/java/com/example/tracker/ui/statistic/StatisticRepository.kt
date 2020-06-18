package com.example.tracker.ui.statistic

import android.app.Application
import com.example.tracker.api.Interfaces
import com.example.tracker.model.Historic
import com.example.tracker.model.Statistic
import com.example.tracker.model.TimeLine
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response

class StatisticRepository {
    fun getStatistic(
        application: Application,
        onResult: (Statistic) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val mOverallApi by lazy {
            Interfaces.initRetrofit(application)
        }

        mOverallApi.getOverallStatistic().enqueue(object : Callback<Statistic> {
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

    fun getOverallHistoric(
        application: Application,
        onResult: (TimeLine) -> Unit,
        onFailure: (String) -> Unit
    ) {

        val mOverallApi by lazy {
            Interfaces.initRetrofit(application)
        }

        mOverallApi.getOverallHistoric().enqueue(object : Callback<TimeLine> {
            override fun onFailure(call: Call<TimeLine>, t: Throwable) {
                onFailure(t.message!!)
            }

            override fun onResponse(
                call: Call<TimeLine>,
                response: Response<TimeLine>
            ) {
                if (response.isSuccessful) {
                    val timeline = response.body()
                    onResult(timeline!!)
                } else {
                    onFailure(response.message())
                }
            }
        })
    }

}