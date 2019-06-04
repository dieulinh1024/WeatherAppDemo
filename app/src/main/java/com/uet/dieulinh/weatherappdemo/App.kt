package com.uet.dieulinh.weatherappdemo

import android.app.Application
import android.content.Context


/**
 * Created by dieulinh on 22/05/2019 at 19:39
 */

class App : Application() {
    companion object {
        private lateinit var instance: App

        val context: Context
            get() {
                return instance.applicationContext
            }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}