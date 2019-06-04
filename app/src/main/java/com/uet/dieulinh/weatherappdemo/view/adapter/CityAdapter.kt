package com.uet.dieulinh.weatherappdemo.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uet.dieulinh.weatherappdemo.R
import com.uet.dieulinh.weatherappdemo.manager.DatabaseManager
import com.uet.dieulinh.weatherappdemo.database.entity.City

/**
 * Created by dieulinh on 02/06/2019 at 06:44
 */

class CityAdapter : RecyclerView.Adapter<CityAdapter.ViewHolder>(), Filterable {

    private lateinit var onItemClickListener: (city: City) -> Unit
    private var cityList: ArrayList<City> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_text_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cityList.get(position)
        holder.itemCity.text = city.cityName
        holder.itemCity.setOnClickListener {
            if (::onItemClickListener.isInitialized) {
                onItemClickListener.invoke(city)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val text = "%${constraint.toString().trim()}%"
                val cities = ArrayList<City>()
                if (text.isNotBlank()) {
                    val list = DatabaseManager.weatherDatabase.cityDao().searchCity(text)
                    cities.addAll(list)
                }
                val result = FilterResults()
                result.values = cities
                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                results!!.values?.let {
                    cityList.clear()
                    cityList.addAll(it as List<City>)
                }
                notifyDataSetChanged()
            }

        }

    }

    fun setItemClickListener(itemClickListener: (city: City) -> Unit) {
        onItemClickListener = itemClickListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemCity = view.findViewById<TextView>(R.id.tvItemText)
    }

}