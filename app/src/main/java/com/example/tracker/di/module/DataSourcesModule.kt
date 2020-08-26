package com.example.tracker.di.module

import com.example.tracker.data.local.LocalDataSource
import com.example.tracker.data.local.LocalDataSourceImpl
import com.example.tracker.data.local.TrackerDataBase
import com.example.tracker.data.remote.RemoteDataSource
import com.example.tracker.data.remote.RemoteDataSourceImpl
import com.example.tracker.data.remote.retrofit.ApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DataSourcesModule {

    @Binds
    @Singleton
    abstract fun provideRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    @Singleton
    abstract fun provideLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource
}