package com.uet.dieulinh.weatherappdemo.base

import android.os.Bundle
import androidx.fragment.app.DialogFragment

/**
 * Created by PhongBM on 01/11/2019
 */

abstract class ViewModelBaseDialog<VM : BaseViewModel> : DialogFragment(), ViewModelBaseView<VM> {
    private lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
    }

    override fun getViewModel(): VM {
        return viewModel
    }

}