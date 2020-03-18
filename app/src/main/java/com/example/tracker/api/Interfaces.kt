package com.example.tracker.api

import com.example.tracker.model.OverallStatisticModel
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
    fun getStatistic(): Call<OverallStatisticModel>

    companion object{
        fun getOverallStatistic(): Interfaces {
            val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

            val httpClient: OkHttpClient = OkHttpClient.Builder().apply {
                this.addInterceptor(interceptor)
            }.build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://corona.lmao.ninja")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit?.create(Interfaces::class.java)!!
        }
    }
}