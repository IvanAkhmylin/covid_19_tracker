package com.example.tracker.ui.news

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class NewsViewModelFactory(private var locale: String, private var query: String) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == NewsViewModel::class.java) {
            return NewsViewModel(locale, query) as T
        }else{
            return create(modelClass)
        }

    }
}