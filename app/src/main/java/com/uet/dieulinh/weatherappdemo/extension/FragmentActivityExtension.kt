package com.uet.dieulinh.weatherappdemo.extension

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.uet.dieulinh.weatherappdemo.util.AlertDialogUtil

/**
 * Created by PhongBM on 01/04/2019
 */

private var loadingDialog: AlertDialog? = null

fun FragmentActivity.showLoadingDialog() {
    if (loadingDialog != null) {
        loadingDialog!!.dismiss()
        loadingDialog = null
    }

    loadingDialog = AlertDialogUtil.showLoadingDialog(this)
    loadingDialog?.show()
}

fun hideLoadingDialog() {
    loadingDialog?.dismiss()
}
