package com.uet.dieulinh.weatherappdemo.view.fragment

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uet.dieulinh.weatherappdemo.R
import com.uet.dieulinh.weatherappdemo.base.ViewModelBaseFragment
import com.uet.dieulinh.weatherappdemo.constant.Constant
import com.uet.dieulinh.weatherappdemo.dto.WeatherDTO
import com.uet.dieulinh.weatherappdemo.extension.createViewModel
import com.uet.dieulinh.weatherappdemo.extension.gone
import com.uet.dieulinh.weatherappdemo.extension.visible
import com.uet.dieulinh.weatherappdemo.manager.AppPreManager
import com.uet.dieulinh.weatherappdemo.util.ReceiveDataAsync
import com.uet.dieulinh.weatherappdemo.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.swipeRefresh
import kotlinx.android.synthetic.main.fragment_main.btnBack
import kotlinx.android.synthetic.main.fragment_main.tvTemperature
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by dieulinh on 02/06/2019 at 22:50
 */

class WeatherDetailFragment : ViewModelBaseFragment<HomeViewModel>() {

    var cityId = ""
    var url = ""

    override fun getContentViewId(): Int {
        return R.layout.fragment_detail
    }

    override fun initViews() {
        swipeRefresh.isRefreshing = true
    }

    override fun initComponents() {
    }

    override fun initData() {
        cityId = WeatherDetailFragmentArgs.fromBundle(arguments!!).cityId.trim()
        if (cityId.isNotBlank()) {
            url = "${Constant.CUR_WEATHER_URL}${AppPreManager.getTempUnitRequest()}&id=$cityId"
            updateData()
        }

    }

    override fun registerListener() {
        btnBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        swipeRefresh.setOnRefreshListener {
            updateData()
            val time = DateTime(DateTimeZone.getDefault()).toString("dd/MM/yyyy HH:mm")
            Toast.makeText(context!!, "Last updated: $time", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View?) {
    }

    override fun createViewModel(): HomeViewModel {
        return createViewModel(HomeViewModel::class.java)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun updateData() {
        ReceiveDataAsync().apply {
            setAction {
                val type = object : TypeToken<WeatherDTO>() {}.type
                val weatherDTO = Gson().fromJson<WeatherDTO>(it, type)
                getViewModel().weatherDTO = weatherDTO
                var location = getString(R.string.detail_location) + " " + weatherDTO.name
                if (weatherDTO.sys.country.isNotBlank()) {
                    location += ", " + weatherDTO.sys.country
                }
                tvLocation.text = location
                tvDescription.text = getString(R.string.detail_description) + " " + weatherDTO.weather.get(0).description
                tvTemperature.text = getString(R.string.detail_temperature) + " ${weatherDTO.main.temp.toInt()}${Typography.degree}${AppPreManager.getTempUnit()}"
                tvMinTemp.text = getString(R.string.detail_min_temp) + " ${weatherDTO.main.temp_min.toInt()}${Typography.degree}${AppPreManager.getTempUnit()}"
                tvMaxTemp.text = getString(R.string.detail_max_temp) + " ${weatherDTO.main.temp_max.toInt()}${Typography.degree}${AppPreManager.getTempUnit()}"
                tvHumidity.text = getString(R.string.detail_humidity) + " ${weatherDTO.main.humidity}%"
                tvPressure.text = getString(R.string.detail_pressure) + " ${weatherDTO.main.pressure} hPa"
                if (weatherDTO.main.sea_level != null) {
                    tvSeaLevel.visible()
                    tvSeaLevel.text = getString(R.string.detail_sea_level) + " ${weatherDTO.main.sea_level} hPa"
                } else {
                    tvSeaLevel.gone()
                }
                if (weatherDTO.main.grnd_level != null) {
                    tvGrndLevel.visible()
                    tvGrndLevel.text = getString(R.string.detail_sea_level) + " ${weatherDTO.main.grnd_level} hPa"
                } else {
                    tvGrndLevel.gone()
                }

                tvWindSpeed.text = getString(R.string.detail_wind_speed) + " ${weatherDTO.wind.speed} m/s"
                tvWindDirection.text = getString(R.string.detail_wind_direction) + " ${weatherDTO.wind.deg}"
                tvCloudiness.text = getString(R.string.detail_cloudiness) + " ${weatherDTO.clouds.all}%"
                if (weatherDTO.visibility != null) {
                    tvVisibility.visible()
                    val kmVisibility = weatherDTO.visibility!! / 1000
                    tvVisibility.text = getString(R.string.detail_visibility) + " ${kmVisibility} km"
                } else {
                    tvVisibility.gone()
                }
                if (weatherDTO.rain != null) {
                    tvRain.visible()
                    weatherDTO.rain!!.rain3h?.let {
                        tvRain.text = getString(R.string.detail_rain_volume) + " $it mm"
                    }
                    weatherDTO.rain!!.rain1h?.let {
                        tvRain.text = getString(R.string.detail_rain_volume) + " $it mm"
                    }
                } else {
                    tvRain.gone()
                }
                if (weatherDTO.snow != null) {
                    tvSnow.visible()
                    weatherDTO.snow!!.snow3h?.let {
                        tvRain.text = getString(R.string.detail_rain_volume) + " $it mm"
                    }
                    weatherDTO.snow!!.snow1h?.let {
                        tvRain.text = getString(R.string.detail_rain_volume) + " $it mm"
                    }
                } else {
                    tvSnow.gone()
                }
                val format = SimpleDateFormat("HH:mm")
                tvSunrise.text = getString(R.string.detail_sunrise) + " " + format.format(Date(weatherDTO.sys.sunrise * 1000))
                tvSunset.text = getString(R.string.detail_sunset) + " " + format.format(Date(weatherDTO.sys.sunset * 1000))
                swipeRefresh.isRefreshing = false
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url)
    }
}