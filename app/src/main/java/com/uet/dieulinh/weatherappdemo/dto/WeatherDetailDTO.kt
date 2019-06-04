package com.uet.dieulinh.weatherappdemo.dto

/**
 * Created by dieulinh on 01/06/2019 at 15:07
 */

data class WeatherDetailDTO(
        var id: String = "",
        var main: String = "",
        var description: String = "",
        var icon: String = ""
) {
}