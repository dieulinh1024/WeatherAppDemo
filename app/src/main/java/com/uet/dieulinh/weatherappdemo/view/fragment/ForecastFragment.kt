package com.uet.dieulinh.weatherappdemo.view.fragment

import android.os.AsyncTask
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uet.dieulinh.weatherappdemo.R
import com.uet.dieulinh.weatherappdemo.base.ViewModelBaseFragment
import com.uet.dieulinh.weatherappdemo.constant.Constant
import com.uet.dieulinh.weatherappdemo.dto.Weather3hDTO
import com.uet.dieulinh.weatherappdemo.dto.WeatherMainDTO
import com.uet.dieulinh.weatherappdemo.extension.createViewModel
import com.uet.dieulinh.weatherappdemo.manager.AppPreManager
import com.uet.dieulinh.weatherappdemo.util.ReceiveDataAsync
import com.uet.dieulinh.weatherappdemo.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_forecast.*
import kotlinx.android.synthetic.main.fragment_forecast.btnBack
import kotlinx.android.synthetic.main.fragment_forecast.swipeRefresh
import kotlinx.android.synthetic.main.item_day_of_week.view.*
import kotlinx.android.synthetic.main.item_weather_day.view.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import kotlin.collections.ArrayList

/**
 * Created by dieulinh on 03/06/2019 at 11:43
 */

class ForecastFragment : ViewModelBaseFragment<HomeViewModel>() {

    var cityId = ""

    override fun getContentViewId(): Int {
        return R.layout.fragment_forecast
    }

    override fun initViews() {
        swipeRefresh.isRefreshing = true
    }

    override fun initComponents() {

    }

    override fun initData() {
        cityId = ForecastFragmentArgs.fromBundle(arguments!!).cityId.trim()
        if (cityId.isNotBlank()) {
            updateData()
        }
    }

    override fun registerListener() {
        btnBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        swipeRefresh.setOnRefreshListener {
            updateData()
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

    private fun updateData() {
        val forecastURL = "${Constant.FORECAST_URL}${AppPreManager.getTempUnitRequest()}&id=$cityId"
        ReceiveDataAsync().apply {
            setAction {
                val type = object : TypeToken<Weather3hDTO>() {}.type
                val weather3hDTOs = Gson().fromJson<Weather3hDTO>(it, type)
                var tempMin = weather3hDTOs.list.get(0).main.temp_min
                var tempMax = weather3hDTOs.list.get(0).main.temp_max
                val weatherMainDTOs = ArrayList<WeatherMainDTO>()
                var count = 0
                var weatherCondition = ""
                var iconWeather = ""
                weather3hDTOs.list.forEach {
                    val date = DateTime(it.dt * 1000, DateTimeZone.UTC)
                    if (date.hourOfDay == 0) {
                        tempMax = it.main.temp_max
                        tempMin = it.main.temp_min
                    }
                    if (date.hourOfDay == 6 || weather3hDTOs.list.indexOf(it) == 0) {
                        weatherCondition = it.weather.get(0).main
                        iconWeather = it.weather.get(0).icon
                    }
                    if (it.main.temp_max > tempMax) {
                        tempMax = it.main.temp_max
                    }
                    if (it.main.temp_min < tempMin) {
                        tempMin = it.main.temp_min
                    }
                    if (date.hourOfDay == 21 || weather3hDTOs.list.indexOf(it) == weather3hDTOs.list.size - 1) {
                        updateUI(date, count, weatherCondition, iconWeather)
                        val weatherMainDTO = WeatherMainDTO().apply {
                            temp_min = tempMin
                            temp_max = tempMax
                        }
                        weatherMainDTOs.add(weatherMainDTO)
                        count++
                    }
                }
                chartView.updateView(weatherMainDTOs)
                swipeRefresh.isRefreshing = false
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, forecastURL)
    }

    private fun updateUI(date: DateTime, count: Int, weatherCondition: String, iconWeather: String) {
        val dayOfWeek = date.toString("EEE")
        val tvDate = date.toString("dd/MM")
        when (count) {
            0 -> {
                tvDay1.tvDayOfWeek.text = dayOfWeek
                tvDay1.tvDate.text = tvDate
                tvWeatherDay1.tvWeather.text = weatherCondition
                Glide.with(context!!).load("${Constant.IMG_URL}$iconWeather.png")
                        .into(tvWeatherDay1.imgWeather)
            }
            1 -> {
                tvDay2.tvDayOfWeek.text = dayOfWeek
                tvDay2.tvDate.text = tvDate
                tvWeatherDay2.tvWeather.text = weatherCondition
                Glide.with(context!!).load("${Constant.IMG_URL}$iconWeather.png")
                        .into(tvWeatherDay2.imgWeather)
            }
            2 -> {
                tvDay3.tvDayOfWeek.text = dayOfWeek
                tvDay3.tvDate.text = tvDate
                tvWeatherDay3.tvWeather.text = weatherCondition
                Glide.with(context!!).load("${Constant.IMG_URL}$iconWeather.png")
                        .into(tvWeatherDay3.imgWeather)
            }
            3 -> {
                tvDay4.tvDayOfWeek.text = dayOfWeek
                tvDay4.tvDate.text = tvDate
                tvWeatherDay4.tvWeather.text = weatherCondition
                Glide.with(context!!).load("${Constant.IMG_URL}$iconWeather.png")
                        .into(tvWeatherDay4.imgWeather)
            }
            4 -> {
                tvDay5.tvDayOfWeek.text = dayOfWeek
                tvDay5.tvDate.text = tvDate
                tvWeatherDay5.tvWeather.text = weatherCondition
                Glide.with(context!!).load("${Constant.IMG_URL}$iconWeather.png")
                        .into(tvWeatherDay5.imgWeather)
            }
        }
    }

}