package com.example.tracker.viewmodel

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tracker.model.OverallStatisticModel
import com.example.tracker.repository.OverallStatisticRepository


class OverallStatisticViewModel : ViewModel() {


    private val model = OverallStatisticRepository()
    val mData = MutableLiveData<OverallStatisticModel>()
    val mFailureMessage = MutableLiveData<String>()

    init {
        model.getStatistic({
            mData.postValue(it)
        }, {
            mFailureMessage.postValue(it)
        })
    }


    fun getData() {
        model.getStatistic({
            mData.postValue(it)
        }, {
            mFailureMessage.postValue(it)
        })
    }

//    init {
//
//
//        val INTERVAL: Long = 500 * 60 * 1 //2 minutes
//        val mHandler = Handler()
//
//        val mHandlerTask: Runnable = object : Runnable {
//            override fun run() {
//                model.getStatistic({
//                    mData.postValue(it)
//                }, {
//                    mFailureMessage.postValue(it)
//                })
//                mHandler.postDelayed(this, INTERVAL)
//            }
//        }
//
//        mHandlerTask.run()
//
//
//    }

}


