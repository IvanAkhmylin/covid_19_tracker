package com.example.tracker.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewsViewModelFactory(private val request: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       if (modelClass.isAssignableFrom(NewsViewModel::class.java)){
           return NewsViewModel(request) as T
       }
        throw IllegalArgumentException("Unknown View Model class")
    }
}