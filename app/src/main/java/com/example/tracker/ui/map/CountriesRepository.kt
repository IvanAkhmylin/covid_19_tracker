package com.example.tracker.ui.map

import android.app.Application
import com.example.tracker.api.Interfaces
import com.example.tracker.model.Country
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response

class CountriesRepository {

    fun getCountries(application: Application, onResult: (List<Country>) -> Unit, onFailure: (String) -> Unit  ) {
        val mOverallApi by lazy {
            Interfaces.initRetrofit(application)
        }

        mOverallApi.getCountriesStatistic().enqueue(object : Callback<List<Country>> {
            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                onFailure("ERROR MESSAGE: ${t.message}")
            }

            override fun onResponse(
                call: Call<List<Country>>,
                response: Response<List<Country>>
            ) {
                onResult(response.body()!!)
            }
        })
    }


}