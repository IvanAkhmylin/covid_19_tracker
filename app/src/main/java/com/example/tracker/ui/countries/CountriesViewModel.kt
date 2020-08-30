package com.example.tracker.ui.countries

import androidx.lifecycle.*
import com.example.tracker.Constants.Constants
import com.example.tracker.Constants.Status
import com.example.tracker.data.local.entity.Country
import com.example.tracker.data.local.entity.Historic
import com.example.tracker.data.repository.CountriesRepository
import com.example.tracker.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.ArrayList


class CountriesViewModel @Inject constructor(val model: CountriesRepository) : ViewModel() {

    lateinit var mSearchQuery: String
    lateinit var mCountryImmutable: List<Country>

    var mStatus = MutableLiveData<String>()

    var mCountries = MutableLiveData<List<Country>>()

    var mCountriesNames = MutableLiveData<List<String>>()
    var mFilteredCountries = ArrayList<Country>()

    var mCountriesHistoric = MutableLiveData<List<Historic>>()

    init {
        getCountries()
    }

    fun getCountries() {
        mStatus.postValue(Status.LOADING)
        viewModelScope.launch {
            val data = model.getCountries()
            when (data.status) {
                Result.Status.SUCCESS -> {
                    getCountriesHistoric()
                    mCountries.postValue(data.data!!)
                    mFilteredCountries = data.data as ArrayList<Country>
                    mCountryImmutable = data.data
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

    fun getCountriesHistoric() {
        val countryNames = viewModelScope.async {
            model.getCountriesNames()
        }

        viewModelScope.launch {
            val names = countryNames.await()
            if (names.isNotEmpty()){
                mCountriesNames.postValue(names)

                var namesString = names.toString()
                namesString = namesString.substring(1, namesString.length - 1)

                val data = model.getCountriesHistoric(namesString)
                when (data.status) {
                    Result.Status.SUCCESS -> {
                        mStatus.postValue(Status.SUCCESS)
                        mCountriesHistoric.postValue(data.data!!)
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

    fun getCountriesByContinent(checkedId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (checkedId == -1) {
                mFilteredCountries = mCountryImmutable as ArrayList<Country>
            } else {
                mFilteredCountries =
                    model.sortByContinent(Constants.CONTINENTS[checkedId]) as ArrayList<Country>
            }

            viewModelScope.launch {
                if (::mSearchQuery.isInitialized && mSearchQuery.isNotEmpty())
                    searchByFilteredData(mSearchQuery)
                else
                    mCountries.postValue(mFilteredCountries)
            }
        }

    }

    fun searchByFilteredData(query: String) {
        val searchResult = ArrayList<Country>()
        mSearchQuery = query
        if (query.isNotEmpty()) {
            mFilteredCountries.forEach {
                if (it.country.toLowerCase().contains(query.toLowerCase())) {
                    searchResult.add(it)
                }
            }
            mCountries.postValue(searchResult)
        } else {
            mCountries.postValue(mFilteredCountries)
        }
    }

    fun getHistoric(country: String?): Historic? {
        return mCountriesHistoric.value?.firstOrNull {
            it.country == country
        }
    }

    fun resetData(){
        if (::mCountryImmutable.isInitialized){
            mSearchQuery = ""
            mFilteredCountries = mCountryImmutable as ArrayList<Country>
            mCountries.postValue(mCountryImmutable)
        }
    }

    fun sortBy(type: String) {
        when (type) {

            Constants.CASES -> {
                mCountries.value?.sortedByDescending { s -> s.cases }?.let {
                    mCountries.postValue(it)
                }
            }

            Constants.RECOVERED -> {
                mCountries.value?.sortedByDescending { s -> s.recovered }?.let {
                    mCountries.postValue(it)
                }
            }

            Constants.DEATHS -> {
                mCountries.value?.sortedByDescending { s -> s.deaths }?.let {
                    mCountries.postValue(it)
                }
            }

        }
    }
}
