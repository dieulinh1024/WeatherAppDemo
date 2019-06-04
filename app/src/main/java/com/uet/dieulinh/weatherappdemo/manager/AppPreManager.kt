package com.uet.dieulinh.weatherappdemo.manager

import android.preference.PreferenceManager
import androidx.core.content.edit
import com.uet.dieulinh.weatherappdemo.App

/**
 * Created by dieulinh on 04/06/2019 at 05:03
 */

object AppPreManager {

    private val pref = PreferenceManager.getDefaultSharedPreferences(App.context)

    fun setCeliusTemperature(isCelsius: Boolean) {
        pref.edit {
            if (isCelsius) {
                putString("Temperature_Unit", "metric")
            } else {
                putString("Temperature_Unit", "imperial")
            }
        }
    }

    fun getTempUnitRequest(): String {
        return pref.getString("Temperature_Unit", "metric")!!
    }

    fun getTempUnit(): String {
        if (pref.getString("Temperature_Unit", "metric") == "metric") {
            return "C"
        }
        return "F"
    }

}