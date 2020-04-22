package com.example.tracker.ui.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tracker.model.CountriesStatisticModel
import com.example.tracker.model.StatisticModel
import com.example.tracker.repository.StatisticRepository


class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val model = StatisticRepository()
    val mShowProgress = MutableLiveData<Boolean>()
    val mCountriesStatistic = MutableLiveData<List<CountriesStatisticModel>>()
    val mFailureMessage = MutableLiveData<String>()

    init {
        getCountriesStatistic()
    }



    private fun getCountriesStatistic() {
        mShowProgress.postValue(true)
        model.getCountryStatistic(getApplication(),{
            mCountriesStatistic.postValue(it)
            mShowProgress.postValue(false)
        }, {
            mFailureMessage.postValue(it)
            mShowProgress.postValue(false)
        })
    }
}


