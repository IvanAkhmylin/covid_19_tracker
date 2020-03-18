package com.example.tracker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tracker.model.OverallStatisticModel
import com.example.tracker.repository.OverallStatisticRepository

class OverallStatisticViewModel : ViewModel(){
    val model = OverallStatisticRepository()

    val data = MutableLiveData<OverallStatisticModel>()


    fun getOverallStatistic(){
         model.getStatistic {
             data.postValue(it)
         }
    }

}