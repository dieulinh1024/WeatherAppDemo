package com.uet.dieulinh.weatherappdemo.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.view.PixelCopy
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uet.dieulinh.weatherappdemo.R
import com.uet.dieulinh.weatherappdemo.base.ViewModelBaseFragment
import com.uet.dieulinh.weatherappdemo.constant.Constant
import com.uet.dieulinh.weatherappdemo.constant.RequestCode
import com.uet.dieulinh.weatherappdemo.dto.Weather3hDTO
import com.uet.dieulinh.weatherappdemo.dto.WeatherDTO
import com.uet.dieulinh.weatherappdemo.extension.createViewModel
import com.uet.dieulinh.weatherappdemo.extension.hideLoadingDialog
import com.uet.dieulinh.weatherappdemo.extension.showLoadingDialog
import com.uet.dieulinh.weatherappdemo.manager.AppPreManager
import com.uet.dieulinh.weatherappdemo.util.PermissionUtil
import com.uet.dieulinh.weatherappdemo.util.ReceiveDataAsync
import com.uet.dieulinh.weatherappdemo.view.dialog.SearchCityDialog
import com.uet.dieulinh.weatherappdemo.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

/**
 * Created by dieulinh on 02/06/2019 at 20:07
 */

class HomeFragment : ViewModelBaseFragment<HomeViewModel>() {

    var cityId = ""
    var bitmap: Bitmap? = null

    override fun getContentViewId(): Int {
        return R.layout.fragment_main
    }

    override fun initViews() {
        swipeRefresh.isRefreshing = true
    }

    override fun initComponents() {
    }

    override fun initData() {
        if (getViewModel().isWeatherDTOInitialized()) {
            cityId = getViewModel().weatherDTO.id
        } else {
            cityId = HomeFragmentArgs.fromBundle(arguments!!).cityId.trim()
        }
        if (cityId.isNotBlank()) {
            updateData()
        }
    }

    override fun registerListener() {
        btnBack.setOnClickListener(this)
        btnDetail.setOnClickListener(this)
        btnForecast.setOnClickListener(this)
        btnSearch.setOnClickListener(this)
        btnShare.setOnClickListener(this)

        swipeRefresh.setOnRefreshListener {
            updateData()
            val time = DateTime(DateTimeZone.getDefault()).toString("dd/MM/yyyy HH:mm")
            Toast.makeText(context!!, "Last updated: $time", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnBack -> Navigation.findNavController(v).popBackStack()
            R.id.btnDetail -> {
                val action = HomeFragmentDirections.openDetailScreen()
                action.cityId = getViewModel().weatherDTO.id
                Navigation.findNavController(v).navigate(action)
            }
            R.id.btnForecast -> {
                val action = HomeFragmentDirections.openDaysForecastScreen()
                action.cityId = getViewModel().weatherDTO.id
                Navigation.findNavController(v).navigate(action)
            }
            R.id.btnSearch -> {
                val searchDialog = SearchCityDialog().apply {
                    setSelectedCallback {
                        cityId = it
                        activity!!.showLoadingDialog()
                        updateData()
                        dismiss()
                    }
                }
                searchDialog.setTargetFragment(this, 0)
                searchDialog.show(fragmentManager!!, javaClass.name)
            }
            R.id.btnShare -> shareCaptureWeather()
        }

    }

    override fun createViewModel(): HomeViewModel {
        return createViewModel(HomeViewModel::class.java)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (PermissionUtil.isPermissionGranted(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            saveAndShare()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateData() {
        val curWeatherURL = "${Constant.CUR_WEATHER_URL}${AppPreManager.getTempUnitRequest()}&id=$cityId"
        ReceiveDataAsync().apply {
            setAction {
                val type = object : TypeToken<WeatherDTO>() {}.type
                val weatherDTO = Gson().fromJson<WeatherDTO>(it, type)
                getViewModel().weatherDTO = weatherDTO
                tvCityName.text = weatherDTO.name
                tvDescription.text = weatherDTO.weather.get(0).main
                tvTemperature.text = "${weatherDTO.main.temp.toInt()}${Typography.degree}"
                tvUnit.text = AppPreManager.getTempUnit()
                val imgURL = "${Constant.IMG_URL}${weatherDTO.weather.get(0).icon}.png"
                Glide.with(context!!).load(imgURL).into(imgWeather)
                updateForecast()
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, curWeatherURL)

    }

    @SuppressLint("SetTextI18n")
    private fun updateForecast() {
        val forecastURL = "${Constant.FORECAST_URL}${AppPreManager.getTempUnitRequest()}&id=$cityId"
        ReceiveDataAsync().apply {
            setAction {
                val type = object : TypeToken<Weather3hDTO>() {}.type
                val weather3hDTOs = Gson().fromJson<Weather3hDTO>(it, type)
                var count = 1
                var tempMin = weather3hDTOs.list.get(0).main.temp_min
                var tempMax = weather3hDTOs.list.get(0).main.temp_max
                val curDate = DateTime(DateTimeZone.UTC)
                weather3hDTOs.list.forEach {
                    val date = DateTime(it.dt * 1000, DateTimeZone.UTC)
                    if (date.dayOfMonth != curDate.dayOfMonth) {
                        if (date.hourOfDay == 0) {
                            tempMax = it.main.temp_max
                            tempMin = it.main.temp_min
                        }
                        if (it.main.temp_max > tempMax) {
                            tempMax = it.main.temp_max
                        }
                        if (it.main.temp_min < tempMin) {
                            tempMin = it.main.temp_min
                        }
                        if (date.hourOfDay == 21) {
                            when (count) {
                                1 -> {
                                    tvFirstDay.text = date.toString("dd/MM/yyyy")
                                    tvTempFirst.text = "${tempMin.toInt()}/${tempMax.toInt()}${Typography.degree}${AppPreManager.getTempUnit()}"
                                }
                                2 -> {
                                    tvSecondDay.text = date.toString("dd/MM/yyyy")
                                    tvTempSecond.text = "${tempMin.toInt()}/${tempMax.toInt()}${Typography.degree}${AppPreManager.getTempUnit()}"
                                }
                                3 -> {
                                    tvThirdDay.text = date.toString("dd/MM/yyyy")
                                    tvTempThird.text = "${tempMin.toInt()}/${tempMax.toInt()}${Typography.degree}${AppPreManager.getTempUnit()}"
                                }
                            }
                            count++
                        }
                    }
                }
                swipeRefresh.isRefreshing = false
                hideLoadingDialog()
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, forecastURL)
    }

    private fun shareCaptureWeather() {
        bitmap = Bitmap.createBitmap(lnlMain.width, lnlMain.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap!!)
        lnlMain.draw(canvas)
        if (PermissionUtil.isPermissionGranted(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            saveAndShare()

        } else {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), RequestCode.WRITE_EXTERNAL_REQUEST)
        }
    }

    private fun saveAndShare() {
        val path = "${Environment.getExternalStorageDirectory().absolutePath}/WeatherApp/IMG_${System.currentTimeMillis()}.png"
        val file = File(path)
        if (!file.exists()) {
            file.createNewFile()
        }
        try {
            val fos = FileOutputStream(file)
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.flush()
            fos.close()
            bitmap = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val uri = Uri.fromFile(file)
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("image/*")
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(intent, "Share by"))
    }

}