package com.yatik.newsworld.utils

class FakeConnectivityHelper(private val shouldConnectToInternet: Boolean) : ConnectivityHelper {

    override fun isConnectedToInternet(): Boolean {
        return shouldConnectToInternet
    }

}