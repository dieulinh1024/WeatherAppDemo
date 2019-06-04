package com.uet.dieulinh.weatherappdemo.util

import android.os.AsyncTask

/**
 * Created by dieulinh on 02/06/2019 at 03:30
 */

class ReceiveDataAsync : AsyncTask<String, Void, String>() {

    private lateinit var onAction: (data: String) -> Unit

    override fun doInBackground(vararg params: String?): String {
        return FetchData.getData(params[0]!!)
    }

    override fun onPostExecute(result: String?) {
        if (!result.isNullOrBlank()) {
            if (::onAction.isInitialized) {
                onAction.invoke(result)
            }
        }
    }

    fun setAction(action: (data: String) -> Unit) {
        onAction = action
    }
}
