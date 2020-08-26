package com.example.tracker.di.module

import com.example.tracker.di.scopes.ActivityScope
import com.example.tracker.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeMainActivity() : MainActivity
}