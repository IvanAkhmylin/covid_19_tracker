package com.example.tracker.repository

import android.app.Application
import android.util.Log
import com.example.tracker.api.Interfaces
import com.example.tracker.model.CountriesStatisticModel
import com.example.tracker.model.StatisticModel
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response

class SearchRepository {

    fun getCountrySearch(application: Application, countryName: String , onResult: (CountriesStatisticModel) -> Unit, onFailure: (String) -> Unit  ) {
        val mOverallApi by lazy {
            Interfaces.initRetrofit(application)
        }

        mOverallApi.searchCountry(countryName,false).enqueue(object : Callback<CountriesStatisticModel> {
            override fun onFailure(call: Call<CountriesStatisticModel>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<CountriesStatisticModel>,
                response: Response<CountriesStatisticModel>
            ) {
                if (response.isSuccessful){
                    response.body()?.let { onResult(it) }
                }else{
                    onFailure("Country not found or doesn't have any cases")
                }
            }


        })
    }


}