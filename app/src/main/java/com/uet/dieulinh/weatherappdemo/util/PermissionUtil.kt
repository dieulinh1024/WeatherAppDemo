package com.uet.dieulinh.weatherappdemo.util

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Created by dieulinh on 02/06/2019 at 01:46
 */

object PermissionUtil {

    @TargetApi(Build.VERSION_CODES.M)
    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

}