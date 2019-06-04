package com.uet.dieulinh.weatherappdemo.util

import android.util.Log
import com.uet.dieulinh.weatherappdemo.constant.Constant
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by dieulinh on 02/06/2019 at 01:20
 */

object FetchData {

    fun getData(url: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.addRequestProperty("x-api-key", Constant.API_KEY)
        connection.doInput = true
        connection.doOutput = true
        connection.connect()
        Log.d("FetchData", connection.url.toString())
        val stringBuilder = StringBuilder()
        val inputStream: InputStream = connection.inputStream
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        bufferedReader.use {
            stringBuilder.append(it.readLine()).append("\r\n")
        }
        inputStream.close()
        connection.disconnect()
        return stringBuilder.toString()
    }
}