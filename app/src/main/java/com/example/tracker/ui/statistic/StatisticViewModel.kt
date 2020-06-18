package com.example.tracker.ui.statistic

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tracker.Constants.Constants
import com.example.tracker.Constants.Status
import com.example.tracker.model.Statistic
import com.example.tracker.model.TimeLine
import com.mapbox.mapboxsdk.utils.Compare
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class StatisticViewModel(application: Application) : AndroidViewModel(application) {
    private val model = StatisticRepository()
    var mNewsListStatus = MutableLiveData<String>()
    val mStatistic = MutableLiveData<Statistic>()
    lateinit var mHistoricList: TimeLine
    val mHistoric = MutableLiveData<TimeLine>()

    init {
        getOverallData()
    }


    fun getOverallData() {
        mNewsListStatus.postValue(Status.LOADING)
        model.getStatistic(getApplication(), {
            mStatistic.postValue(it)
            mNewsListStatus.postValue(Status.SUCCESS)
        }, {
            mNewsListStatus.postValue(Status.ERROR)
        })

        mNewsListStatus.postValue(Status.LOADING)
        model.getOverallHistoric(getApplication(), {
            mHistoric.postValue(it)
            mHistoricList = it
        }, {
            mNewsListStatus.postValue(Status.ERROR)
        })
    }





}


