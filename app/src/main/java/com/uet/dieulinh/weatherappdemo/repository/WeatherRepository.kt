package com.uet.dieulinh.weatherappdemo.repository

import androidx.lifecycle.LiveData
import com.uet.dieulinh.weatherappdemo.manager.DatabaseManager
import com.uet.dieulinh.weatherappdemo.database.entity.City

/**
 * Created by dieulinh on 24/05/2019 at 03:18
 */

object WeatherRepository {

    fun getFavoriteCities(): LiveData<List<City>> {
        return DatabaseManager.weatherDatabase.cityDao().getFavoriteCities()
    }
}