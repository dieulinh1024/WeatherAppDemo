package com.uet.dieulinh.weatherappdemo.dto

/**
 * Created by dieulinh on 03/06/2019 at 18:22
 */

data class Weather3hDTO(
        var cnt: Int = 0,
        var cod: Int = 0,
        var dt_txt: String = "",
        var list: List<WeatherDTO> = ArrayList()
)