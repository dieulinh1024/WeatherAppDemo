package com.uet.dieulinh.weatherappdemo.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by dieulinh on 01/06/2019 at 15:18
 */

data class SnowDTO(
        @SerializedName("1h")
        var snow1h: Float? = null,
        @SerializedName("3h")
        var snow3h: Float? = null
)