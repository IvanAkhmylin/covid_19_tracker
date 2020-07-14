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
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


interface Interfaces {
    @Headers("Content-Type: application/json")
    @GET("v3/covid-19/all")
    fun getOverallStatistic(): Call<Statistic>

    @Headers("Content-Type: application/json")
    @GET("v3/covid-19/historical/all?lastdays=99")
    fun getOverallHistoric(): Call<TimeLine>

//    @Headers("Content-Type: application/json")
//    @GET("v3/covid-19/countries/{query}")
//    fun searchCountry(
//        @Path("query") country: String,
//        @Query("strict") strictSearch: Boolean
//    ): Call<Country>


    @Headers("Content-Type: application/json")
    @GET("v3/covid-19/countries")
    fun getCountriesStatistic(): Call<List<Country>>

    @Headers("Content-Type: application/json")
    @GET("v3/covid-19/historical/{path}?lastdays=100")
    fun getCountriesHistoric(@Path("path") countries: String): Call<List<Historic>>


    companion object {
        fun initRetrofit(application: Application): Interfaces {
            val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }


            val httpClient: OkHttpClient = OkHttpClient.Builder().apply {
                addInterceptor(interceptor)
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