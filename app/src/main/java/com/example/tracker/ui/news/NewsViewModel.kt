package com.example.tracker.ui.news

import android.app.Application
import androidx.lifecycle.*
import com.example.tracker.Constants.Status
import com.example.tracker.model.News
import kotlinx.coroutines.*

class NewsViewModel(request: String) : ViewModel() {
    private val repository = NewsRepository()
    var mNewsListStatus = MutableLiveData<String>()
    val mNewsData = MutableLiveData<ArrayList<News>>()
    var mTrueLink = MutableLiveData<String>()

    init {
        getNewsData(request)
    }

    fun getNewsData(string: String) {
        mNewsListStatus.postValue(Status.LOADING)
        viewModelScope.launch {
            repository.getNews(  string, {
                mNewsData.postValue(it)
                mNewsListStatus.postValue(Status.SUCCESS)
            }, {
                mNewsListStatus.postValue(Status.ERROR)
            })
        }
    }

    fun getUrlWithOutRedirect(string: String) {
        mNewsListStatus.postValue(Status.LOADING)
        repository.getTrueLink(string , {
            mNewsListStatus.postValue(Status.SUCCESS)
            mTrueLink.postValue(it)
        }, {
            mNewsListStatus.postValue(Status.ERROR)
        })
    }


}