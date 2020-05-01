package com.example.tracker.ui.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tracker.model.Country
import com.example.tracker.ui.map.CountriesRepository
import java.util.*
import kotlin.collections.ArrayList

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val countryList = ArrayList<Country>()
    private val recentlyList = ArrayList<Country>()
    val searchResult = MutableLiveData<List<Country>>()
    val mShowProgress = MutableLiveData<Boolean>()
    val mRecentlySeen = MutableLiveData<List<Country>>()

    fun setCountryList(list: ArrayList<Country>) {
        countryList.addAll(list)
    }

    fun searchCountry(text: String) {
        val list = countryList.filter {
            it.country?.toLowerCase(Locale.getDefault())?.startsWith(text)!!
        }
        searchResult.postValue(list)
    }

//    fun searchCountry(countryName: String) {
//        mShowProgress.postValue(true)
//        model.getCountrySearch(getApplication(), countryName ,{
//            mCountry.postValue(it)
//            recentlyList.add(it)
//            mRecentlySeen.postValue(recentlyList)
//            mShowProgress.postValue(false)
//        }, {
//            mFailureMessage.postValue(it)
//            mShowProgress.postValue(false)
//        })
//    }
}


