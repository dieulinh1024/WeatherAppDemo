package com.uet.dieulinh.weatherappdemo.service

import android.annotation.SuppressLint
import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uet.dieulinh.weatherappdemo.App
import com.uet.dieulinh.weatherappdemo.R
import com.uet.dieulinh.weatherappdemo.constant.Constant
import com.uet.dieulinh.weatherappdemo.dto.WeatherDTO
import com.uet.dieulinh.weatherappdemo.manager.AppPreManager
import com.uet.dieulinh.weatherappdemo.util.ReceiveDataAsync
import java.util.*

/**
 * Created by dieulinh on 01/06/2019 at 16:48
 */

class WeatherService() : IntentService(TAG) {

    private val chanelId by lazy {
        App.context.packageName
    }

    private val notificationManager by lazy {
        NotificationManagerCompat.from(this)
    }

    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    companion object {
        private const val TAG = "WeatherService"
        const val ACTION = "com.uet.dieulinh.weatherapp.action.RECEIVE_DATA"
    }

    init {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(App.context)
    }

    @SuppressLint("MissingPermission")
    override fun onHandleIntent(intent: Intent?) {
        fusedLocationProviderClient?.lastLocation?.addOnSuccessListener {
            if (it != null) {
                Log.i(TAG, "push notify")
                val url = "${Constant.CUR_WEATHER_URL}${AppPreManager.getTempUnitRequest()}&lat=${it.latitude}&lon=${it.longitude}"
                val asyncTask = ReceiveDataAsync()
                asyncTask.setAction {
                    val gson = Gson()
                    val type = object : TypeToken<WeatherDTO>() {}.type
                    val weatherDTO = gson.fromJson<WeatherDTO>(it, type)
                    showNotification(weatherDTO)
                }
                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url)
            }
        }
    }

    private fun showNotification(weatherDTO: WeatherDTO) {
        createNotificationChanel()

        val title = "${weatherDTO.name}: ${weatherDTO.main.temp}${Typography.degree}${AppPreManager.getTempUnit()}"
        val body = "Now, weather is ${weatherDTO.weather.get(0).description}"

        val builder = NotificationCompat.Builder(this, chanelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        val id = Random().nextInt()
        Thread(Runnable {
            val imgURL = "${Constant.IMG_URL}${weatherDTO.weather.get(0).icon}.png"
            val target = Glide.with(this).asBitmap().load(imgURL).submit()
            val bitmap = target.get()
            builder.setLargeIcon(bitmap)
            notificationManager.cancelAll()
            notificationManager.notify(id, builder.build())
        }).start()
    }

    private fun createNotificationChanel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val name = getString(R.string.app_name)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val chanel = NotificationChannel(chanelId, name, importance)

        notificationManager.createNotificationChannel(chanel)
    }

}