package com.example.tracker.di.module

import android.app.Application
import com.example.tracker.App
import com.example.tracker.data.local.LocalDataSource
import com.example.tracker.data.local.LocalDataSourceImpl
import com.example.tracker.data.local.TrackerDataBase
import com.example.tracker.data.remote.RemoteDataSource
import com.example.tracker.data.remote.RemoteDataSourceImpl
import com.example.tracker.data.remote.retrofit.ApiService
import com.example.tracker.utils.InternetConnectionManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule{

    @Provides
    @Singleton
    fun provideContext(application: App): Application {
        return application
    }


    @Provides
    fun provideInternetManager(
        app: Application
    ): InternetConnectionManager {
        return InternetConnectionManager(app)
    }


}