package com.uet.dieulinh.weatherappdemo.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.res.AssetManager
import android.os.AsyncTask
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uet.dieulinh.weatherappdemo.App
import com.uet.dieulinh.weatherappdemo.base.BaseViewModel
import com.uet.dieulinh.weatherappdemo.manager.DatabaseManager
import com.uet.dieulinh.weatherappdemo.database.entity.City
import com.uet.dieulinh.weatherappdemo.dto.CityDTO
import com.uet.dieulinh.weatherappdemo.dto.WeatherDTO

/**
 * Created by dieulinh on 24/05/2019 at 03:41
 */

class WeatherListViewModel(application: Application) : BaseViewModel(application) {

    val fusedLocationProviderClient: FusedLocationProviderClient
    var cities = ArrayList<City>()
    var weatherDTOs = ArrayList<WeatherDTO>()

    init {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(App.context)
    }

    fun initDatabase(assetManager: AssetManager, fileName: String, onFinished: () -> Unit) {
        val data = assetManager.open(fileName).bufferedReader().use { it.readText() }
        InitDataAsync().apply {
            setAction {
                onFinished.invoke()
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data)
    }

    fun updateFavoriteCity(cityId: String, favorite: Boolean, onFinished: () -> Unit) {
        Thread(Runnable {
            val city = DatabaseManager.weatherDatabase.cityDao().getById(cityId)
            city.favorite = favorite
            DatabaseManager.weatherDatabase.cityDao().update(city)
            onFinished.invoke()
        }).start()
    }

    @SuppressLint("StaticFieldLeak")
    inner class InitDataAsync : AsyncTask<String, Void, Void>() {

        private lateinit var onAction: () -> Unit

        override fun doInBackground(vararg params: String?): Void? {
            val gson = Gson()
            val type = object : TypeToken<List<CityDTO>>() {}.type
            val cities = ArrayList<City>()
            val cityDTOs: List<CityDTO> = gson.fromJson(params[0], type)
            cityDTOs.forEach {
                cities.add(City(it))
            }
            DatabaseManager.weatherDatabase.cityDao().insertList(cities)
            return null
        }

        override fun onPostExecute(result: Void?) {
            if (::onAction.isInitialized) {
                onAction.invoke()
            }
        }

        fun setAction(action: () -> Unit) {
            onAction = action
        }
    }

}