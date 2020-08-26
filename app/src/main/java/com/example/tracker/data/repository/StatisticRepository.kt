package com.example.tracker.data.repository

import com.example.tracker.data.local.LocalDataSource
import com.example.tracker.data.remote.RemoteDataSource
import com.example.tracker.utils.InternetConnectionManager
import com.example.tracker.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StatisticRepository @Inject constructor(
    private val connectionManager: InternetConnectionManager,
    private val mLocalDataSource: LocalDataSource,
    private val mRemoteDataSource: RemoteDataSource
) {

    suspend fun getOverallStatistic() =
        withContext(Dispatchers.IO) {
            if (connectionManager.isInternetConnectionExist) {
                val data = mRemoteDataSource.getGlobalStatistic()
                when (data.status) {
                    Result.Status.SUCCESS -> {
                        mLocalDataSource.deleteStatistic()
                        mLocalDataSource.insertStatistic(data.data!!)
                        data
                    }
                    Result.Status.ERROR -> {
                        Result.error(data.message.toString(), null)
                    }
                    else -> {
                        Result.loading(null)
                    }
                }
            } else {
                val data = mLocalDataSource.getStatistic()
                if (data != null){
                    Result.success(data)
                }else{
                    Result.error(data)
                }
            }
        }


    suspend fun getOverallHistoric() =
        withContext(Dispatchers.IO) {
            if (connectionManager.isInternetConnectionExist) {
                val data = mRemoteDataSource.getGlobalHistoric()
                when (data.status) {
                    Result.Status.SUCCESS -> {
                        data.let {
                            mLocalDataSource.clearOverallTimeLine()
                            mLocalDataSource.insertOverallTimeLine(data.data!!)
                            data
                        }
                    }

                    Result.Status.ERROR -> {
                        Result.error(data.message.toString(), null)
                    }

                    else -> {
                        Result.loading(null)
                    }
                }
            } else {
                val data = mLocalDataSource.getOverallTimeLine()
                if (data != null){
                    Result.success(data)
                }else{
                    Result.error(data)
                }
            }
        }

}
