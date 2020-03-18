package com.example.tracker.repository

import android.util.Log
import com.example.tracker.api.Interfaces
import com.example.tracker.model.OverallStatisticModel
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response

class OverallStatisticRepository {
    fun getStatistic(onResult: (OverallStatisticModel) -> Unit) {
        val mOverallApi by lazy {
            Interfaces.getOverallStatistic()
        }

        mOverallApi.getStatistic().enqueue(object : Callback<OverallStatisticModel> {
            override fun onFailure(call: Call<OverallStatisticModel>, t: Throwable) {
                Log.d("TAG" , "${t.message} Ыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыы")

            }

            override fun onResponse(
                call: Call<OverallStatisticModel>,
                response: Response<OverallStatisticModel>
            ) {
                onResult(response.body()!!)
            }

        })
    }

}