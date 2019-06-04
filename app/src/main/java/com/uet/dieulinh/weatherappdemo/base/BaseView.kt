package com.uet.dieulinh.weatherappdemo.base

/**
 * Created by dieulinh on 24/05/2019 at 03:43
 */

interface BaseView {

    fun getContentViewId(): Int

    fun initViews()

    fun initComponents()

    fun initData()

    fun registerListener()
}