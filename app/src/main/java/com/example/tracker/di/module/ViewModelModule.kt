package com.example.tracker.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tracker.di.ViewModelFactory
import com.example.tracker.di.scopes.ViewModelKey
import com.example.tracker.ui.countries.CountriesViewModel
import com.example.tracker.ui.news.NewsViewModel
import com.example.tracker.ui.statistic.StatisticViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(StatisticViewModel::class)
    internal abstract fun bindStatisticViewModel(viewModel: StatisticViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    internal abstract fun bindNewsViewModel (viewModel: NewsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CountriesViewModel::class)
    internal abstract fun bindCountriesViewModel (viewModel: CountriesViewModel): ViewModel


    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}