package com.example.tracker.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import javax.inject.Inject

class InternetConnectionManager @Inject constructor(app: Application) {
    val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isInternetConnectionExist: Boolean
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                return  actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            } else {
                val netInfo = connectivityManager.activeNetworkInfo
                return netInfo?.isConnectedOrConnecting == true
            }
        }

}