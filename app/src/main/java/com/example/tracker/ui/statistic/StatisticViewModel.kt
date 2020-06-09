package com.example.tracker.ui.statistic

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tracker.Constants.Status
import com.example.tracker.model.Statistic


class StatisticViewModel(application: Application) : AndroidViewModel(application) {
    private val model = StatisticRepository()
    var mNewsListStatus = MutableLiveData<String>()
    val mStatistic = MutableLiveData<Statistic>()

    init {
        getStatistic()
    }

    fun getStatistic() {
        mNewsListStatus.postValue(Status.LOADING)
        model.getStatistic(getApplication(),{
            mStatistic.postValue(it)
            mNewsListStatus.postValue(Status.SUCCESS)
        }, {
            Log.d("TAG" , it)
            mNewsListStatus.postValue(Status.ERROR)
        })
    }


}


