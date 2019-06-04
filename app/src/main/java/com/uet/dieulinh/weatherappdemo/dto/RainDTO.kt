package com.uet.dieulinh.weatherappdemo.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by dieulinh on 01/06/2019 at 15:14
 */

data class RainDTO(
        @SerializedName("1h")
        var rain1h: Float? = null,
        @SerializedName("3h")
        var rain3h: Float? = null
)