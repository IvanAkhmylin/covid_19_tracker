package com.example.tracker.ui.countries

import android.app.Application
import android.util.Log
import com.example.tracker.api.Interfaces
import com.example.tracker.model.Country
import com.example.tracker.model.Historic
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountriesRepository {

    fun getCountries(
        application: Application,
        onResult: (List<Country>) -> Unit,
        onFailure: (String) -> Unit
    ) {

        val mOverallApi by lazy {
            Interfaces.initRetrofit(application)
        }

        mOverallApi.getCountriesStatistic().enqueue(object : Callback<List<Country>> {
            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                onFailure("ERROR MESSAGE: ${t.message}")
                Log.d("TAG" , "ERROR GETTING  --- > ${t.message}")
            }

            override fun onResponse(
                call: Call<List<Country>>,
                response: Response<List<Country>>
            ) {
                Log.d("TAG" , "FINE GETTING  --- > ${response.body()!!.size}")
                onResult(response.body()!!)

            }
        })
    }

    fun getCountriesHistoric(
        application: Application, countries: String,
        onResult: (List<Historic>) -> Unit,
        onFailure: (String) -> Unit
    ) {

        val mOverallApi by lazy {
            Interfaces.initRetrofit(application)
        }

        mOverallApi.getCountriesHistoric(countries).enqueue(object : Callback<List<Historic>> {
            override fun onFailure(call: Call<List<Historic>>, t: Throwable) {
                onFailure(t.message!!)
            }

            override fun onResponse(
                call: Call<List<Historic>>,
                response: Response<List<Historic>>
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