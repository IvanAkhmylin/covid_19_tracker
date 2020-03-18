package com.example.tracker

import android.app.Application

class App : Application(){

    override fun onCreate() {
        super.onCreate()
        getDataFromAPI()
    }

    private fun getDataFromAPI() {
//        OverallStatisticViewModel().getOverallStatistic()
    }


}