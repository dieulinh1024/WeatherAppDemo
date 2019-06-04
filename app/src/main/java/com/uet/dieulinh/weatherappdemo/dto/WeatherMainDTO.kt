package com.uet.dieulinh.weatherappdemo.dto

/**
 * Created by dieulinh on 01/06/2019 at 15:02
 */

data class WeatherMainDTO(
        var temp: Float = 0f,
        var pressure: Float = 0f,
        var humidity: Float = 0f,
        var temp_min: Float = 0f,
        var temp_max: Float = 0f,
        var sea_level: Float? = null,
        var grnd_level: Float? = null
)