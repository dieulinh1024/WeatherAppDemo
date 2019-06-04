package com.uet.dieulinh.weatherappdemo.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uet.dieulinh.weatherappdemo.App
import com.uet.dieulinh.weatherappdemo.R
import com.uet.dieulinh.weatherappdemo.constant.Constant
import com.uet.dieulinh.weatherappdemo.dto.WeatherDTO
import com.uet.dieulinh.weatherappdemo.extension.invisible
import com.uet.dieulinh.weatherappdemo.manager.AppPreManager
import com.uet.dieulinh.weatherappdemo.viewmodel.WeatherListViewModel

/**
 * Created by dieulinh on 01/06/2019 at 15:36
 */

class WeatherCityAdapter(val viewModel: WeatherListViewModel) : RecyclerView.Adapter<WeatherCityAdapter.ViewHolder>() {

    private lateinit var onViewClickListener: (id: Int, position: Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather_location, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return viewModel.weatherDTOs.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cityWeather = viewModel.weatherDTOs.get(position)
        holder.tvCityName.text = cityWeather.name
        holder.tvCityName.isSelected = true
        holder.tvTemp.text = "${cityWeather.main.temp.toInt()}${Typography.degree}${AppPreManager.getTempUnit()}"
        val iconURL = "${Constant.IMG_URL}${cityWeather.weather.get(0).icon}.png"
        Glide.with(App.context).load(iconURL)
                .into(holder.imgIcon)
        holder.tvDescription.text = cityWeather.weather.get(0).main
        if (position == 0) {
            holder.btnDelete.invisible()
        }
        holder.lnlWeatherLocation.setOnClickListener {
            if (::onViewClickListener.isInitialized) {
                onViewClickListener.invoke(it.id, position)
            }
        }
        holder.btnDelete.setOnClickListener {
            if (::onViewClickListener.isInitialized) {
                onViewClickListener.invoke(it.id, position)
            }
        }
    }

    fun setItemViewClickListener(itemViewClickListener: (viewId: Int, position: Int) -> Unit) {
        onViewClickListener = itemViewClickListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var lnlWeatherLocation = view.findViewById(R.id.lnlWeatherLocation) as LinearLayout
        var tvCityName = view.findViewById<TextView>(R.id.tvCity)
        var tvTemp = view.findViewById<TextView>(R.id.tvTemperature)
        var tvUnit = view.findViewById<TextView>(R.id.tvUnit)
        var imgIcon = view.findViewById<ImageView>(R.id.imgWeather)
        var btnDelete = view.findViewById<ImageButton>(R.id.btnDelete)
        var tvDescription = view.findViewById<TextView>(R.id.tvDescription)
    }
}