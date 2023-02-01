package com.graphicless.newsapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


class CheckNetwork() {
    val context: Context = MyApplication.instance
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
}