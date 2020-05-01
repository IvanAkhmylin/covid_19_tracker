package com.example.tracker.repository

import android.app.Application
import com.example.tracker.api.Interfaces
import com.example.tracker.model.Country
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response

class SearchRepository {

    fun getCountrySearch(application: Application, countryName: String, onResult: (Country) -> Unit, onFailure: (String) -> Unit  ) {
        val mOverallApi by lazy {
            Interfaces.initRetrofit(application)
        }

        mOverallApi.searchCountry(countryName,false).enqueue(object : Callback<Country> {
            override fun onFailure(call: Call<Country>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<Country>,
                response: Response<Country>
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