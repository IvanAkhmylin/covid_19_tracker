package com.example.tracker.ui.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tracker.model.Country
import java.util.*
import java.util.function.Predicate
import kotlin.collections.ArrayList

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val countryList = ArrayList<Country>()
    private val recentlyList = ArrayList<Country>()
    val searchResult = MutableLiveData<List<Country>>()
    val notFound = MutableLiveData<Boolean>()
    val mRecentlySeen = MutableLiveData<List<Country>>()

    fun setCountryList(list: ArrayList<Country>) {
        countryList.clear()
        countryList.addAll(list)
    }

    fun searchCountry(text: String) {
        if (text.trim().isNotEmpty()) {
            val list = countryList.filter {
                it.country?.toLowerCase(Locale.getDefault())?.startsWith(text)!!
            }

            if (list.isEmpty()){
                notFound.postValue(true)
            }else{
                notFound.postValue(false)
            }

            searchResult.postValue(list)
        }else{
            searchResult.postValue(null)
        }
    }
    fun addRecentlyCountry(country : Country){
            recentlyList.forEachIndexed{index, it ->
                if (it.country == country.country){
                    recentlyList.removeAt(index)
                }
            }

        recentlyList.add(0,country)
        mRecentlySeen.postValue(recentlyList)
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


