package com.example.tracker.ui.news

import android.app.Application
import androidx.lifecycle.*
import com.example.tracker.App
import com.example.tracker.Constants.Status
import com.example.tracker.R
import com.example.tracker.data.local.entity.News
import com.example.tracker.data.repository.NewsRepository
import com.example.tracker.utils.Result
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

class NewsViewModel @Inject constructor(val model: NewsRepository) : ViewModel(){

    var mStatus = MutableLiveData<String>()
    var mErrorMessage = MutableLiveData<String>()
    val mNewsList = MutableLiveData<List<News>>()


    fun refreshNewsData(string: String, locale: String) {
        mStatus.postValue(Status.PRE_LOADING)

        viewModelScope.launch {
            model.refreshNews(string, locale) {
                when (it.status.name) {
                    Result.Status.SUCCESS.name -> {
                        mStatus.postValue(Status.SUCCESS)
                        mNewsList.postValue(it.data!!)
                    }

                    Result.Status.ERROR.name -> {
                        mErrorMessage.postValue(it.message!!)
                        mStatus.postValue(Status.ERROR)
                    }

                    Result.Status.LOADING.name -> {
                        mStatus.postValue(Status.LOADING)
                        it.let {
                            mNewsList.postValue(it.data!!)
                        }
                    }
                }
            }
        }


    }

}