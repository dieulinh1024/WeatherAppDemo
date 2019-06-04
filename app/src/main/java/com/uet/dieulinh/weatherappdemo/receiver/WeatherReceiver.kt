package com.uet.dieulinh.weatherappdemo.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uet.dieulinh.weatherappdemo.dto.WeatherDTO

/**
 * Created by dieulinh on 02/06/2019 at 00:44
 */

class WeatherReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("WeatherReceiver", "receive data")
        val gson = Gson()
        val type = object : TypeToken<WeatherDTO>() {}.type
        val weatherDTO = gson.fromJson<WeatherDTO>(intent?.getStringExtra("weather_data"), type)
        Toast.makeText(context, "${weatherDTO.name}: ${weatherDTO.coord.lat}" +
                ", ${weatherDTO.coord.lon}", Toast.LENGTH_SHORT).show()
    }
}