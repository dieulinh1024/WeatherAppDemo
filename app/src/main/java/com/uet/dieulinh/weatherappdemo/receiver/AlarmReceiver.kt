package com.uet.dieulinh.weatherappdemo.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.uet.dieulinh.weatherappdemo.service.WeatherService

/**
 * Created by dieulinh on 01/06/2019 at 16:58
 */

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmReceiver", "receiver alarm")
        val serviceIntent = Intent(context, WeatherService::class.java)
        context?.startService(serviceIntent)
    }
}