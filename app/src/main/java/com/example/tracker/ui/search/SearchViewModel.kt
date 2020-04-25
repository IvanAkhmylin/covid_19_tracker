package com.example.tracker.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tracker.model.CountriesStatisticModel
import com.example.tracker.repository.SearchRepository


class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val model = SearchRepository()
    val mShowProgress = MutableLiveData<Boolean>()
    val mCountry = MutableLiveData<CountriesStatisticModel>()
    val mFailureMessage = MutableLiveData<String>()

    fun searchCountry(countryName: String) {
        mShowProgress.postValue(true)
        model.getCountrySearch(getApplication(), countryName ,{
            mCountry.postValue(it)
            mShowProgress.postValue(false)
        }, {
            mFailureMessage.postValue(it)
            mShowProgress.postValue(false)
        })
    }
}


