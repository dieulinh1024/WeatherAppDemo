package com.uet.dieulinh.weatherappdemo.base

import android.os.Bundle

/**
 * Created by PhongBM on 01/02/2019
 */

abstract class ViewModelBaseFragment<VM : BaseViewModel> : BaseFragment(), ViewModelBaseView<VM> {
    private lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
    }

    override fun getViewModel(): VM {
        return viewModel
    }

}