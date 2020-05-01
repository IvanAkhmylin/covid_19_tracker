package com.example.tracker.ui.statistic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tracker.model.Statistic


class StatisticViewModel(application: Application) : AndroidViewModel(application) {
    private val model = StatisticRepository()
    val mShowProgress = MutableLiveData<Boolean>()
    val mStatistic = MutableLiveData<Statistic>()
    val mFailureMessage = MutableLiveData<String>()

    init {
        getStatistic()
    }

    private fun getStatistic() {
        mShowProgress.postValue(true)
        model.getStatistic(getApplication(),{
            mStatistic.postValue(it)
            mShowProgress.postValue(false)
        }, {
            mFailureMessage.postValue(it)
            mShowProgress.postValue(false)
        })
    }


}


