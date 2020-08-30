package com.example.tracker.data.repository

import android.app.Application
import com.example.tracker.Constants.Status
import com.example.tracker.data.local.LocalDataSource
import com.example.tracker.data.local.entity.News
import com.example.tracker.data.remote.RemoteDataSource
import com.example.tracker.utils.Result
import com.example.tracker.utils.InternetConnectionManager
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val connectionManager:InternetConnectionManager,
    private val mLocalDataSource: LocalDataSource,
    private val mRemoteDataSource: RemoteDataSource
) {

    suspend fun refreshNews(
        query: String,
        locale: String,
        onResult: (Result<List<News>>) -> Unit
    ) = withContext(Dispatchers.IO) {
        if (connectionManager.isInternetConnectionExist) {
            mRemoteDataSource.getNews(query, locale) {
                GlobalScope.launch(Dispatchers.IO) {
                    mLocalDataSource.clearNews()
                    mLocalDataSource.insertNews(it.data!!)
                }
                onResult(it)
            }

        } else {
            val data = mLocalDataSource.getNews()
            if (data.isNotEmpty()){
                onResult(Result.success(data))
            }else{
                onResult(Result.error("ERROR"))
            }
        }
    }
}

