package com.example.tracker.ui.statistic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracker.Constants.Status
import com.example.tracker.data.local.entity.Statistic
import com.example.tracker.data.local.entity.TimeLine
import com.example.tracker.data.repository.StatisticRepository
import com.example.tracker.utils.Result
import kotlinx.coroutines.launch
import javax.inject.Inject


class StatisticViewModel
@Inject constructor(
    private val model: StatisticRepository
) : ViewModel() {

    var mStatus = MutableLiveData<String>()
    var mStatistic = MutableLiveData<Statistic>()
    var mHistoric = MutableLiveData<TimeLine>()

    init {
        getOverallStatistic()
        getOverallHistoric()
    }

    fun getOverallStatistic() {
        mStatus.postValue(Status.LOADING)
        viewModelScope.launch {
            val data = model.getOverallStatistic()
            when (data.status) {
                Result.Status.SUCCESS -> {
                    mStatus.postValue(Status.SUCCESS)
                    mStatistic.postValue(data.data!!)
                }

                Result.Status.ERROR -> {
                    mStatus.postValue(Status.ERROR)
                }

                Result.Status.LOADING -> {
                    mStatus.postValue(Status.LOADING)
                }
            }
        }
    }

    fun getOverallHistoric() {
        mStatus.postValue(Status.LOADING)
        viewModelScope.launch {
            val data = model.getOverallHistoric()
            when (data.status) {
                Result.Status.SUCCESS -> {
                    mStatus.postValue(Status.SUCCESS)
                    mHistoric.postValue(data.data!!)
                }

                Result.Status.ERROR -> {
                    mStatus.postValue(Status.ERROR)
                }

                Result.Status.LOADING -> {
                    mStatus.postValue(Status.LOADING)
                }
            }
        }
    }

}


