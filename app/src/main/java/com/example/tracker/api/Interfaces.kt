package com.example.tracker.api

import android.app.Application
import com.example.tracker.R
import com.example.tracker.model.CountriesStatisticModel
import com.example.tracker.model.StatisticModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

interface Interfaces {
    @Headers("Content-Type: application/json")
    @GET("/all")
    fun getStatistic(): Call<StatisticModel>

    @Headers("Content-Type: application/json")
    @GET("/countries")
    fun getCountriesStatistic(): Call<List<CountriesStatisticModel>>


    companion object{
        fun initRetrofit(application: Application): Interfaces {
            val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

            val httpClient: OkHttpClient = OkHttpClient.Builder().apply {
                this.addInterceptor(interceptor)
            }.build()

            val retrofit = Retrofit.Builder()
                .baseUrl(application.getString(R.string.baseURL))
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit?.create(Interfaces::class.java)!!
        }
    }
}