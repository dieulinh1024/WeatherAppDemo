package com.uet.dieulinh.weatherappdemo.dto

/**
 * Created by dieulinh on 01/06/2019 at 10:35
 */

data class CityDTO(
        var id: String = "",
        var name: String = "",
        var country: String = "",
        var coord: CoordDTO = CoordDTO()
)