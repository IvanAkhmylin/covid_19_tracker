package com.example.tracker.ui.statistic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tracker.model.Statistic


class StatisticViewModel(application: Application) : AndroidViewModel(application) {
    private val model = StatisticRepository()
    val mFailure = MutableLiveData<Boolean>()
    val mShowProgress = MutableLiveData<Boolean>()
    val mStatistic = MutableLiveData<Statistic>()

    init {
        getStatistic()
    }

    fun getStatistic() {
        mShowProgress.postValue(true)
        model.getStatistic(getApplication(),{
            mStatistic.postValue(it)
            mFailure.postValue(false)
            mShowProgress.postValue(false)
        }, {
            mFailure.postValue(true)
            mShowProgress.postValue(false)
        })
    }


}


