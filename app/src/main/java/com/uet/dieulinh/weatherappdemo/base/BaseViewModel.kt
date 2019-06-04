package com.uet.dieulinh.weatherappdemo.base

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel

/**
 * Created by PhongBM on 01/10/2019
 */

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    override fun onCleared() {
        super.onCleared()
        Log.d(javaClass.name, "onCleared()... ")
    }

}