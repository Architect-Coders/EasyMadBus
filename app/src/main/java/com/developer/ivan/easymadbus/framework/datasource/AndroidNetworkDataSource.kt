package com.developer.ivan.easymadbus.framework.datasource

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.developer.ivan.data.datasources.NetworkDataSource

class AndroidNetworkDataSource(val context: Context): NetworkDataSource {
    override fun isConnected(): Boolean {
                var result: Boolean
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                return when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
    }
}