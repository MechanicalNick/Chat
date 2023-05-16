package com.tinkoff.homework.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager

object NetworkChecker {
    fun isNetworkConnected(activity: Activity): Boolean {
        val manager = activity
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected()
    }
}