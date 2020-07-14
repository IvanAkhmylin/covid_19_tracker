package com.example.tracker.ui.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.tracker.Constants.Status
import com.example.tracker.R
import com.example.tracker.model.News
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

class NewsViewModel(locale: String, query: String) : ViewModel() {
    private val repository = NewsRepository()
    var mNewsListStatus = MutableLiveData<String>()
    val mNewsData = MutableLiveData<Pair<ArrayList<News>, String>>()


    init {
        getNewsData(query , locale)
    }

    fun getNewsData(string: String, locale: String) {
        Log.d("TAG" , "$string - $locale")
        viewModelScope.launch {
            mNewsData.postValue(null)
            repository.getNews(string,locale , { list, status ->
                mNewsData.postValue(list to status)
                mNewsListStatus.postValue(status)
            }, {
                mNewsListStatus.postValue(Status.ERROR)
            })
        }

    }




//    fun getUrlWithOutRedirect(string: String) {
//        mNewsListStatus.postValue(Status.LOADING)
//        repository.getTrueLink(string , {
//            mNewsListStatus.postValue(Status.SUCCESS)
//            mTrueLink.postValue(it)
//        }, {
//            mNewsListStatus.postValue(Status.ERROR)
//        })
//    }


}