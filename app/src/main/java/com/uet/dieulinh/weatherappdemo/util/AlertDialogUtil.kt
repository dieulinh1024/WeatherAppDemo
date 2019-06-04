package com.uet.dieulinh.weatherappdemo.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.uet.dieulinh.weatherappdemo.R

/**
 * Created by PhongBM on 01/06/2019
 */

object AlertDialogUtil {
    internal enum class DialogType {
        LOADING,
        MESSAGE
    }

    private var isLoadingDialogShowing = false
    private var isMessageDialogShowing = false

    @SuppressLint("InflateParams")
    fun showLoadingDialog(context: Context): AlertDialog? {
        if (isLoadingDialogShowing()) return null

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        val imgLoading = view.findViewById(R.id.imgLoading) as ImageView

        val dialog = createDialog(context, false, view, true, DialogType.LOADING)

        Glide.with(context)
                .load(R.drawable.img_loading)
                .into(imgLoading)

        dialog.show()
        return dialog
    }

    // ---------------------------------------------------------------------------------------------

    private fun createDialog(context: Context,
                             cancelable: Boolean,
                             view: View,
                             transparentWindow: Boolean = false,
                             dialogType: DialogType = DialogType.MESSAGE): AlertDialog {
        val dialog = AlertDialog.Builder(context)
            .setCancelable(cancelable)
            .setView(view)
            .create()

        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimationNormal
        dialog.window?.decorView?.setBackgroundResource(android.R.color.transparent)
        if (transparentWindow) {
            dialog.window?.setDimAmount(0F)
        }

        dialog.setOnDismissListener {
            if (dialogType == DialogType.LOADING) {
                isLoadingDialogShowing = false
            }
            if (dialogType == DialogType.MESSAGE) {
                isMessageDialogShowing = false
            }
        }

        dialog.setOnCancelListener {
            if (dialogType == DialogType.LOADING) {
                isLoadingDialogShowing = false
            }
            if (dialogType == DialogType.MESSAGE) {
                isMessageDialogShowing = false
            }
        }

        return dialog
    }

    private fun isLoadingDialogShowing(): Boolean {
        if (isLoadingDialogShowing) {
            return true
        }
        isLoadingDialogShowing = true
        return false
    }

}