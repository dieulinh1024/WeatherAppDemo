package com.uet.dieulinh.weatherappdemo.base

/**
 * Created by PhongBM on 01/02/2019
 */

interface ViewModelBaseView<out VM : BaseViewModel> : BaseView {
    fun createViewModel(): VM

    fun getViewModel(): VM

}