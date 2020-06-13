package com.example.tracker

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class App : Application(){
    override fun onCreate() {
        Fresco.initialize(this)
        super.onCreate()
    }

}