package com.uet.dieulinh.weatherappdemo.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.uet.dieulinh.weatherappdemo.dto.CityDTO

/**
 * Created by dieulinh on 23/05/2019 at 06:54
 */

@Entity(tableName = "city")
data class City(
        @PrimaryKey
        @ColumnInfo(name = "id")
        var id: String = "",
        @ColumnInfo(name = "name")
        var cityName: String = "",
        @ColumnInfo(name = "country")
        var country: String = "",
        @ColumnInfo(name = "lon")
        var lon: String = "",
        @ColumnInfo(name = "lat")
        var lat: String = "",
        @ColumnInfo(name = "favorite")
        var favorite: Boolean = false
) {
    @Ignore
    constructor(cityDTO: CityDTO) : this() {
        id = cityDTO.id
        cityName = cityDTO.name
        country = cityDTO.country
        lon = cityDTO.coord.lon
        lat = cityDTO.coord.lat
    }
}