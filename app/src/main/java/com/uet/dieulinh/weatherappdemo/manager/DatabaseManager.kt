package com.uet.dieulinh.weatherappdemo.manager

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.uet.dieulinh.weatherappdemo.App
import com.uet.dieulinh.weatherappdemo.database.dao.CityDao
import com.uet.dieulinh.weatherappdemo.database.entity.City

/**
 * Created by dieulinh on 31/05/2019 at 05:59
 */

object DatabaseManager {

    private var weatherDb: WeatherDatabase? = null

    val weatherDatabase: WeatherDatabase
    get() {
        if (weatherDb == null){
            weatherDb = Room.databaseBuilder(App.context, DatabaseManager.WeatherDatabase::class.java, "weather_db").build()
        }
        return weatherDb!!
    }

    fun isWeatherDBExits(): Boolean{
        return App.context.getDatabasePath("weather_db").exists()
    }

/*====================================================================================================================*/

    @Database(
        entities = [City::class],
        exportSchema = false,
        version = 1
    )
    abstract class WeatherDatabase: RoomDatabase() {
        abstract fun cityDao(): CityDao
    }

}