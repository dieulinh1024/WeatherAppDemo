package com.uet.dieulinh.weatherappdemo.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.uet.dieulinh.weatherappdemo.base.BaseViewModel

/**
 * Created by PhongBM on 01/27/2019
 */

// -------------------------------------------------------------------------------------------------
fun <VM : BaseViewModel> Fragment.createViewModel(viewModelClass: Class<VM>,
                                                  fragment: Fragment = this): VM {
    return ViewModelProviders.of(fragment).get(viewModelClass)
}

// -------------------------------------------------------------------------------------------------
