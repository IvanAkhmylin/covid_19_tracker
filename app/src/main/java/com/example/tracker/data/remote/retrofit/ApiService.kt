package com.example.tracker.data.remote.retrofit

import android.app.Application
import com.example.tracker.R
import com.example.tracker.data.local.entity.Country
import com.example.tracker.data.local.entity.Historic
import com.example.tracker.data.local.entity.Statistic
import com.example.tracker.data.local.entity.TimeLine
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path


interface ApiService {
    @Headers("Content-Type: application/json")
    @GET("v3/covid-19/all")
    suspend fun getOverallStatistic(): Response<Statistic>

    @Headers("Content-Type: application/json")
    @GET("v3/covid-19/historical/all?lastdays=99")
    suspend fun getOverallHistoric(): Response<TimeLine>

    @Headers("Content-Type: application/json")
    @GET("v3/covid-19/countries")
    suspend fun getCountriesStatistic(): Response<List<Country>>

    @Headers("Content-Type: application/json")
    @GET("v3/covid-19/historical/{path}?lastdays=100")
    suspend fun getCountriesHistoric(@Path("path") countries: String): Response<List<Historic>>
}