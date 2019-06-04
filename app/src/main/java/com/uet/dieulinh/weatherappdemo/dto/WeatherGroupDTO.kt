package com.uet.dieulinh.weatherappdemo.dto

/**
 * Created by dieulinh on 02/06/2019 at 15:13
 */

data class WeatherGroupDTO (
        var cnt: Int = 0,
        var list: List<WeatherDTO> = ArrayList()
)