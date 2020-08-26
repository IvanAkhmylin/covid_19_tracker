package com.example.tracker.di.module

import android.app.Application
import androidx.room.Room
import com.example.tracker.data.local.LocalDataSource
import com.example.tracker.data.local.LocalDataSourceImpl
import com.example.tracker.data.local.TrackerDataBase
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): TrackerDataBase {
        return Room.databaseBuilder(
            application.applicationContext,
            TrackerDataBase::class.java,
            "tracker_database"
        ).fallbackToDestructiveMigration().build()
    }

}