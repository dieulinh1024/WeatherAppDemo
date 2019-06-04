package com.uet.dieulinh.weatherappdemo.dto

/**
 * Created by dieulinh on 01/06/2019 at 11:12
 */

data class WeatherDTO(
        var id: String = "",
        var dt: Long = 0L,
        var name: String = "",
        var cod: Int = 0,
        var visibility: Int? = null,
        var coord: CoordDTO = CoordDTO(),
        var weather: List<WeatherDetailDTO> = ArrayList(),
        var main: WeatherMainDTO = WeatherMainDTO(),
        var wind: WindDTO = WindDTO(),
        var rain: RainDTO? = null,
        var snow: SnowDTO? = null,
        var clouds: CloundDTO = CloundDTO(),
        var sys: SystemDTO = SystemDTO()
)