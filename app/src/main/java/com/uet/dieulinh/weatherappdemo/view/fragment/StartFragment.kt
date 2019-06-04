package com.uet.dieulinh.weatherappdemo.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.AsyncTask
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uet.dieulinh.weatherappdemo.R
import com.uet.dieulinh.weatherappdemo.base.ViewModelBaseFragment
import com.uet.dieulinh.weatherappdemo.constant.Constant
import com.uet.dieulinh.weatherappdemo.constant.RequestCode
import com.uet.dieulinh.weatherappdemo.manager.DatabaseManager
import com.uet.dieulinh.weatherappdemo.dto.WeatherDTO
import com.uet.dieulinh.weatherappdemo.dto.WeatherGroupDTO
import com.uet.dieulinh.weatherappdemo.extension.createViewModel
import com.uet.dieulinh.weatherappdemo.extension.hideLoadingDialog
import com.uet.dieulinh.weatherappdemo.extension.showLoadingDialog
import com.uet.dieulinh.weatherappdemo.manager.AppPreManager
import com.uet.dieulinh.weatherappdemo.receiver.AlarmReceiver
import com.uet.dieulinh.weatherappdemo.repository.WeatherRepository
import com.uet.dieulinh.weatherappdemo.util.PermissionUtil
import com.uet.dieulinh.weatherappdemo.util.ReceiveDataAsync
import com.uet.dieulinh.weatherappdemo.view.adapter.WeatherCityAdapter
import com.uet.dieulinh.weatherappdemo.view.dialog.SearchCityDialog
import com.uet.dieulinh.weatherappdemo.viewmodel.WeatherListViewModel
import kotlinx.android.synthetic.main.fragment_start.*
import kotlinx.android.synthetic.main.view_list.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.*


/**
 * Created by dieulinh on 23/05/2019 at 06:51
 */

class StartFragment : ViewModelBaseFragment<WeatherListViewModel>() {

    lateinit var weatherCityAdapter: WeatherCityAdapter

    override fun getContentViewId(): Int {
        return R.layout.fragment_start
    }

    override fun initViews() {
        activity!!.showLoadingDialog()
        if (AppPreManager.getTempUnit() == "C") {
            rbCelsiusUnit.isChecked = true
            rbCelsiusUnit.setTextColor(Color.WHITE)
            rbFahrenheitUnit.setTextColor(Color.BLACK)
        } else {
            rbFahrenheitUnit.isChecked = true
            rbFahrenheitUnit.setTextColor(Color.WHITE)
            rbCelsiusUnit.setTextColor(Color.BLACK)
        }
    }

    override fun initComponents() {
        weatherCityAdapter = WeatherCityAdapter(getViewModel())
        rcvList.adapter = weatherCityAdapter
    }

    @SuppressLint("MissingPermission")
    override fun initData() {
        if (!DatabaseManager.isWeatherDBExits()) {
            getViewModel().initDatabase(activity!!.application.assets, "city_list.json") {
                getDataWeather()
            }
        } else {
            getDataWeather()
        }
    }

    override fun registerListener() {
        btnAddPlace.setOnClickListener {
            val searchDialog = SearchCityDialog()
            searchDialog.setTargetFragment(this, 0)
            searchDialog.setSelectedCallback {
                activity!!.showLoadingDialog()
                getViewModel().updateFavoriteCity(it, true) {
                    searchDialog.dismiss()
                    hideLoadingDialog()
                }
            }
            searchDialog.show(fragmentManager!!, javaClass.name)
        }

        weatherCityAdapter.setItemViewClickListener { viewId, position ->
            when (viewId) {
                R.id.btnDelete -> {
                    activity!!.showLoadingDialog()
                    getViewModel().updateFavoriteCity(getViewModel().weatherDTOs.get(position).id, false) {
                        hideLoadingDialog()
                    }
                }
                R.id.lnlWeatherLocation -> {
                    val action = StartFragmentDirections.openMainScreen()
                    action.cityId = getViewModel().weatherDTOs.get(position).id
                    Navigation.findNavController(activity!!, viewId).navigate(action)
                }
            }
        }
        swipeRefresh.setOnRefreshListener {
            getDataWeather()
            val time = DateTime(DateTimeZone.getDefault()).toString("dd/MM/yyyy HH:mm")
            Toast.makeText(context!!, "Last updated: $time", Toast.LENGTH_SHORT).show()
        }

        radioUnitType.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rbCelsiusUnit) {
                rbCelsiusUnit.setTextColor(Color.WHITE)
                rbFahrenheitUnit.setTextColor(Color.BLACK)
                AppPreManager.setCeliusTemperature(true)
            } else {
                rbCelsiusUnit.setTextColor(Color.BLACK)
                rbFahrenheitUnit.setTextColor(Color.WHITE)
                AppPreManager.setCeliusTemperature(false)
            }
            activity!!.showLoadingDialog()
            getDataWeather()
        }
    }

    override fun onClick(v: View?) {
    }

    override fun createViewModel(): WeatherListViewModel {
        return createViewModel(WeatherListViewModel::class.java)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == RequestCode.LOCATION_PERMISSION_REQUEST) {
            grantResults.forEach {
                if (it != PackageManager.PERMISSION_GRANTED) {
                    hideLoadingDialog()
                    Toast.makeText(context!!, "Need access location to receive data", Toast.LENGTH_SHORT).show()
                    return
                }
            }
            setScheduleAlarm()
            getDataWeather()
        }
    }


    fun getDataWeather() {
        if (PermissionUtil.isPermissionGranted(context!!, Manifest.permission.ACCESS_COARSE_LOCATION)
                && PermissionUtil.isPermissionGranted(context!!, Manifest.permission.ACCESS_FINE_LOCATION)) {
            WeatherRepository.getFavoriteCities().observe(this, Observer {
                val gson = Gson()
                getViewModel().weatherDTOs.clear()
                getViewModel().cities.clear()
                getViewModel().cities.addAll(it)
                if (it.size > 0) {
                    var url = "${Constant.GROUP_ID_URL}${AppPreManager.getTempUnitRequest()}&id="
                    it.forEach {
                        url = "$url${it.id},"
                    }
                    url = url.substring(0, url.length - 1)
                    ReceiveDataAsync().apply {
                        setAction {
                            getViewModel().weatherDTOs.clear()
                            val type = object : TypeToken<WeatherGroupDTO>() {}.type
                            val weatherGroupDTO = gson.fromJson<WeatherGroupDTO>(it, type)
                            getViewModel().weatherDTOs.addAll(weatherGroupDTO.list)
                            weatherCityAdapter.notifyDataSetChanged()
                            getCurWeather(gson)
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url)
                } else {
                    getCurWeather(gson)
                }
            })
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION), RequestCode.LOCATION_PERMISSION_REQUEST)
        }

    }

    @SuppressLint("MissingPermission")
    private fun getCurWeather(gson: Gson) {
        getViewModel().fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                val url = "${Constant.CUR_WEATHER_URL}${AppPreManager.getTempUnitRequest()}&lat=${it.latitude}&lon=${it.longitude}"
                ReceiveDataAsync().apply {
                    setAction {
                        val type = object : TypeToken<WeatherDTO>() {}.type
                        val weatherDTO = gson.fromJson<WeatherDTO>(it, type)
                        val weatherDTOs = getViewModel().weatherDTOs.filter { it.id == weatherDTO.id }
                        if (weatherDTOs.isEmpty()) {
                            getViewModel().weatherDTOs.add(0, weatherDTO)
                            weatherCityAdapter.notifyDataSetChanged()
                            swipeRefresh.isRefreshing = false
                            hideLoadingDialog()
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url)
            } else {
                hideLoadingDialog()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun setScheduleAlarm() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 14)
        calendar.set(Calendar.MINUTE, 25)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }
        val intent = Intent(context!!, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context!!, RequestCode.NOTIFY_WEATHER_REQUEST,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent)
    }

}