package com.uet.dieulinh.weatherappdemo.viewmodel

import android.app.Application
import com.uet.dieulinh.weatherappdemo.base.BaseViewModel
import com.uet.dieulinh.weatherappdemo.dto.WeatherDTO

/**
 * Created by dieulinh on 02/06/2019 at 20:25
 */

class HomeViewModel(app: Application) : BaseViewModel(app) {

    lateinit var weatherDTO: WeatherDTO

    fun isWeatherDTOInitialized(): Boolean {
        return ::weatherDTO.isInitialized
    }
}