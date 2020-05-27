package com.example.tracker.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tracker.Constants.Constants
import com.example.tracker.Constants.Status
import com.example.tracker.model.Country
import com.example.tracker.ui.map.CountriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    val model = CountriesRepository()
    private val mCountriesList = ArrayList<Country>()
    private val mSearchedList = ArrayList<Country>()
    private val mFilteredList = ArrayList<Country>()
    private var mSearchRequest = ""
    val mCountriesStatus = MutableLiveData<String>()
    val mFilteredData = MutableLiveData<List<Country>>()

    init {
        getCountriesStatistic()
    }



    private fun getCountriesStatistic() {
        mCountriesStatus.postValue(Status.LOADING)
        model.getCountries(getApplication(),{
            mCountriesStatus.postValue(Status.SUCCESS)
            mCountriesList.addAll(it)
            mFilteredList.addAll(it)
            mFilteredData.postValue(it)
        }, {
            mCountriesStatus.postValue(Status.ERROR)
        })
    }

    fun getCountriesByContinent(checkedId: Int) {
       GlobalScope.launch {
           if (checkedId == -1 ){
               mFilteredList.clear()
               mFilteredList.addAll(mCountriesList)
               mFilteredData.postValue(mCountriesList)
               searchByFilteredData(mSearchRequest)
           }else{
               mFilteredList.clear()
               val continent =  Constants.CONTINENTS[checkedId]
               mCountriesList.forEach() {
                   if (it.continent == continent){
                       mFilteredList.add(it)
                   }
               }
               GlobalScope.launch(Dispatchers.Main){
                   mFilteredData.postValue(mFilteredList)
                   searchByFilteredData(mSearchRequest)

               }
           }
       }
    }

    fun searchByFilteredData(newText: String?) {
        mSearchRequest = newText!!
        GlobalScope.launch {
            if (newText.isNotEmpty()){
                mSearchedList.clear()
                mFilteredList.forEach() {
                    if (it.country?.toLowerCase()?.startsWith(newText.toLowerCase())!!){
                        mSearchedList.add(it)
                    }
                }
                GlobalScope.launch(Dispatchers.Main){
                    if (mSearchedList.isNotEmpty()) {
                        mFilteredData.postValue(mSearchedList)
                        mCountriesStatus.postValue(Status.SUCCESS)
                    }else{
                        mCountriesStatus.postValue(Status.NOT_FOUND)
                    }
                }
            }else{
                mFilteredData.postValue(mFilteredList)
                mCountriesStatus.postValue(Status.SUCCESS)
            }
        }
    }


}




