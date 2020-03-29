package com.example.tracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tracker.model.CountriesStatisticModel
import com.example.tracker.model.StatisticModel
import com.example.tracker.repository.StatisticRepository


class StatisticViewModel(application: Application) : AndroidViewModel(application) {
    private val model = StatisticRepository()
    val mStatistic = MutableLiveData<StatisticModel>()
    val mCountriesStatistic = MutableLiveData<List<CountriesStatisticModel>>()
    val mFailureMessage = MutableLiveData<String>()

    init {
        getStatistic()
        getCountriesStatistic()
    }

    fun getStatistic() {
        model.getStatistic(getApplication(),{
            mStatistic.postValue(it)
        }, {
            mFailureMessage.postValue(it)
        })
    }

    private fun getCountriesStatistic() {
        model.getCountryStatistic(getApplication(),{
            mCountriesStatistic.postValue(it)
        }, {
            mFailureMessage.postValue(it)
        })
    }
}


