package com.example.tracker.di.module

import com.example.tracker.di.scopes.ActivityScope
import com.example.tracker.di.scopes.FragmentScope
import com.example.tracker.ui.MainActivity
import com.example.tracker.ui.countries.CountriesFragment
import com.example.tracker.ui.map.MapFragment
import com.example.tracker.ui.news.NewsFragment
import com.example.tracker.ui.statistic.StatisticFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeStatisticFragment() : StatisticFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeNewsFragment() : NewsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeCountriesFragment() : CountriesFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeMapFragment() : MapFragment

}