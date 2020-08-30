package com.example.tracker

import android.app.Application
import com.example.tracker.di.DaggerAppComponent
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class App : DaggerApplication()  {
    private val appComponent = DaggerAppComponent.builder().application(this).build()

    override fun onCreate() {
        Fresco.initialize(this)
        super.onCreate()
        mApplication = this
        initTimber()
    }

    override fun applicationInjector() = appComponent

    private fun initTimber() {
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object{
        lateinit var mApplication: Application


        fun getApplication() : Application{
            return mApplication
        }
    }

}