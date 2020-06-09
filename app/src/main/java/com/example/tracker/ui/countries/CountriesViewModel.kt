package com.example.tracker.ui.countries

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tracker.Constants.Constants
import com.example.tracker.Constants.Status
import com.example.tracker.model.Country
import com.example.tracker.model.Historic
import kotlinx.coroutines.*
import okhttp3.internal.wait

class CountriesViewModel(application: Application) : AndroidViewModel(application) {
    private var mSearchRequest = ""
    val model = CountriesRepository()

    private val mCountriesList = ArrayList<Country>()
    private val mSearchedList = ArrayList<Country>()
    private val mFilteredList = ArrayList<Country>()

    private val mCountryHistoric = MutableLiveData<List<Historic>>()
    val mCountriesStatus = MutableLiveData<String>()
    val mFilteredData = MutableLiveData<List<Country>>()

    init {
        getCountriesStatistic()
    }

    fun getCountriesStatistic() {
        mCountriesStatus.postValue(Status.LOADING)
        model.getCountries(getApplication(), {
            mCountriesList.addAll(it)
            mFilteredList.addAll(it)
            mFilteredData.postValue(it)
            getCountriesHistoric()
        }, {
            mCountriesStatus.postValue(Status.ERROR)
        })
    }

    private fun getCountriesHistoric() {
        GlobalScope.launch(Dispatchers.Main) {
            val countries = withContext(Dispatchers.Default) {
                getCountriesNames()
            }
            Log.d("TAG", countries)

            model.getCountriesHistoric(getApplication(), countries, {
                mCountryHistoric.postValue(it)
                mCountriesStatus.postValue(Status.SUCCESS)
            }, {
                mCountriesStatus.postValue(Status.ERROR)
            })
        }
    }

    fun getCountriesByContinent(checkedId: Int) {
        GlobalScope.launch {
            if (checkedId == -1) {
                mFilteredList.clear()
                mFilteredList.addAll(mCountriesList)

                if (mSearchRequest.isNotEmpty()) {
                    searchByFilteredData(mSearchRequest)
                } else {
                    mFilteredData.postValue(mCountriesList)
                }

            } else {
                mFilteredList.clear()
                val continent = Constants.CONTINENTS[checkedId]
                mCountriesList.forEach() {
                    if (it.continent == continent) {
                        mFilteredList.add(it)
                    }
                }
                GlobalScope.launch(Dispatchers.Main) {
                    if (mSearchRequest.isNotEmpty()) {
                        searchByFilteredData(mSearchRequest)
                        mCountriesStatus.postValue(Status.SUCCESS)
                    } else {
                        mFilteredData.postValue(mFilteredList)
                    }
                }
            }
        }
    }

    fun searchByFilteredData(newText: String?) {
        mSearchRequest = newText!!
        GlobalScope.launch {
            if (newText.isNotEmpty()) {
                mSearchedList.clear()
                mFilteredList.forEach() {
                    if (it.country?.toLowerCase()?.contains(newText.toLowerCase())!!) {
                        mSearchedList.add(it)
                    }
                }
                GlobalScope.launch(Dispatchers.Main) {
                    if (mSearchedList.isNotEmpty()) {
                        mFilteredData.postValue(mSearchedList)
                        mCountriesStatus.postValue(Status.SUCCESS)
                    } else {
                        mCountriesStatus.postValue(Status.NOT_FOUND)
                    }
                }
            } else {
                mFilteredData.postValue(mFilteredList)
                mCountriesStatus.postValue(Status.SUCCESS)
            }
        }
    }

    suspend fun getCountriesNames(): String {
        var names = ""
        val countries = CoroutineScope(Dispatchers.IO).async {
            mCountriesList.forEachIndexed { index, country ->
                if (index == 0) names += "${country.country}"
                names += ",${country.country}"
            }
            return@async
        }
        countries.await()
        return names
    }

    fun resetData() {
        mSearchRequest = ""
        mFilteredData.postValue(mCountriesList)
    }

    fun  getHistoric(country: String?): Historic? {
        return mCountryHistoric.value!!.firstOrNull {
            it.country == country
        }

    }


}
