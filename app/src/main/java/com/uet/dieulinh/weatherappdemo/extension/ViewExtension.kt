package com.uet.dieulinh.weatherappdemo.extension

import android.view.View

/**
 * Created by PhongBM on 01/10/2019
 */

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.disable() {
    isEnabled = false
}

fun View.enable() {
    isEnabled = true
}