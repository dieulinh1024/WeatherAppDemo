package com.uet.dieulinh.weatherappdemo.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.uet.dieulinh.weatherappdemo.database.entity.City


/**
 * Created by dieulinh on 24/05/2019 at 02:46
 */

@Dao
interface CityDao {

    @Insert
    fun insert(city: City)

    @Insert
    fun insertList(cities: List<City>)

    @Delete
    fun delete(city: City)

    @Update
    fun update(city: City)

    @Query("select * from city")
    fun getAll(): LiveData<List<City>>?

    @Query("select * from city where id=:cityId")
    fun getByIdLive(cityId: String): LiveData<City>

    @Query("select * from city where id=:cityId")
    fun getById(cityId: String): City

    @Query("select * from city where favorite = 1 order by country")
    fun getFavoriteCities(): LiveData<List<City>>

    @Query("select * from city where favorite = 0 and name like :text order by country")
    fun searchCity(text: String): List<City>
}