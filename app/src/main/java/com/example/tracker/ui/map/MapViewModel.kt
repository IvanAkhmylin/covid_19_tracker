package com.example.tracker.ui.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tracker.model.Country
import com.example.tracker.ui.statistic.StatisticRepository


class MapViewModel(application: Application) : AndroidViewModel(application) {
    val model = CountriesRepository()
    val mShowProgress = MutableLiveData<Boolean>()
    val mCountriesStatistic = MutableLiveData<List<Country>>()
    val mFailureMessage = MutableLiveData<String>()
    val mSearchCountry = MutableLiveData<Country>()

    init {
        getCountriesStatistic()
    }

    private fun getCountriesStatistic() {
        mShowProgress.postValue(true)
        model.getCountries(getApplication(),{
            mCountriesStatistic.postValue(it)
            mShowProgress.postValue(false)
        }, {
            mFailureMessage.postValue(it)
            mShowProgress.postValue(false)
        })
    }
}


