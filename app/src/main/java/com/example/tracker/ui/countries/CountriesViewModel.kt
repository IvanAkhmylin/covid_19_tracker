package com.example.tracker.ui.countries

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tracker.Constants.Constants
import com.example.tracker.Constants.Status
import com.example.tracker.model.Country
import com.example.tracker.model.Historic
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class CountriesViewModel(application: Application) : AndroidViewModel(application) {
    val model = CountriesRepository()

    private var mSearchRequest = ""
    var mAppLang = "en"
    private val mCountriesList = ArrayList<Country>()
    private val mSearchedList = ArrayList<Country>()
    private val mFilteredList = ArrayList<Country>()

    val mCountryHistoric = MutableLiveData<List<Historic>>()
    val mCountriesStatus = MutableLiveData<String>()
    val mFilteredData = MutableLiveData<List<Country>>()
    val mCountriesNameList = MutableLiveData<List<String>>()

    init {
        Log.d("TAG" , "RE CALL ")
        getCountriesStatistic()
    }

    fun getCountriesStatistic() {
        viewModelScope.launch {
            mCountriesStatus.postValue(Status.LOADING)
            model.getCountries(getApplication(), {
                mCountriesList.addAll(it)
                mFilteredList.addAll(it)
                mFilteredData.postValue(it)
                getCountriesArray(it)
                getCountriesHistoric()
            }, {
                mCountriesStatus.postValue(it)
            })
        }
    }

    private fun getCountriesHistoric() {
        GlobalScope.launch(Dispatchers.Main) {
            val countries = withContext(Dispatchers.Default) {
                getCountriesNames()
            }

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
                Log.d("TAG" , "-1")
                mFilteredList.clear()
                mFilteredList.addAll(mCountriesList)

                if (mSearchRequest.isNotEmpty()) {
                    searchByFilteredData(mSearchRequest)
                } else {
                    mFilteredData.postValue(mCountriesList)
                }

            } else {
                Log.d("TAG" , "$checkedId")
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
        var countryNamesForHistoricRequest = ""
        val countries = CoroutineScope(Dispatchers.IO).async {
            mCountriesList.forEachIndexed { index, country ->
                if (index == 0) countryNamesForHistoricRequest += "${country.country}"
                countryNamesForHistoricRequest += ",${country.country}"
            }
            return@async
        }
        countries.await()
        return countryNamesForHistoricRequest
    }

    fun getCountriesArray(it: List<Country>) {
        val names = ArrayList<String>()
        Log.d("TAG", "INIT SHIT ")
        if (mAppLang != "en") {
            it.forEachIndexed { index, country ->
                val inLocale: Locale = Locale.forLanguageTag("en-EN")
                val outLocale: Locale = Locale.forLanguageTag(mAppLang)
                for (l in Locale.getAvailableLocales()) {
                    if (l.getDisplayCountry(inLocale).equals(country.country)) {
                        names.add(l.getDisplayCountry(outLocale))
                        break
                    }
                }
            }
        } else {
            it.forEachIndexed { index, country ->
                names.add(country.country!!)
            }
        }
        Log.d("TAG", "GETTING SHIT  ${names.size}")

        mCountriesNameList.postValue(names)

    }

    fun resetData() {
        mSearchRequest = ""
        mFilteredData.postValue(mCountriesList)
    }

    fun getHistoric(country: String?): Historic? {
        return mCountryHistoric.value!!.firstOrNull {
            it.country == country
        }

    }

    fun sortBy(type: String) {
        when (type) {
            Constants.CASES -> {
                mFilteredList.sortByDescending {
                    it.cases
                }

                mFilteredData.postValue(mFilteredList)
            }

            Constants.RECOVERED -> {
                mFilteredList.sortByDescending {
                    it.recovered
                }

                mFilteredData.postValue(mFilteredList)
            }

            Constants.DEATHS -> {
                mFilteredList.sortByDescending {
                    it.deaths
                }

                mFilteredData.postValue(mFilteredList)
            }
        }
    }


}
