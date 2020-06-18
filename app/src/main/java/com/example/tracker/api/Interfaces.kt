package com.example.tracker.api

import android.app.Application
import com.example.tracker.R
import com.example.tracker.model.Country
import com.example.tracker.model.Historic
import com.example.tracker.model.Statistic
import com.example.tracker.model.TimeLine
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Interfaces {
    @Headers("Content-Type: application/json")
    @GET("v2/all")
    fun getOverallStatistic(): Call<Statistic>

    @Headers("Content-Type: application/json")
    @GET("v2/historical/all?lastdays=99")
    fun getOverallHistoric(): Call<TimeLine>

    @Headers("Content-Type: application/json")
    @GET("v2/countries/{query}")
    fun searchCountry(
        @Path("query") country: String,
        @Query("strict") strictSearch: Boolean
    ): Call<Country>


    @Headers("Content-Type: application/json")
    @GET("v2/countries")
    fun getCountriesStatistic(): Call<List<Country>>

    @Headers("Content-Type: application/json")
    @GET("v2/historical/{path}?lastdays=100")
    fun getCountriesHistoric(@Path("path") countries: String): Call<List<Historic>>


    companion object {
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

            return retrofit.create(Interfaces::class.java)
        }
    }
}