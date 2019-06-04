package com.uet.dieulinh.weatherappdemo.view

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.uet.dieulinh.weatherappdemo.R
import com.uet.dieulinh.weatherappdemo.constant.RequestCode
import com.uet.dieulinh.weatherappdemo.util.PermissionUtil

class MainActivity : AppCompatActivity() {

    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
