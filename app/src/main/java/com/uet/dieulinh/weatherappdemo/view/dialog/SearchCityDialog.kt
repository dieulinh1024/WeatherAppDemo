package com.uet.dieulinh.weatherappdemo.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import com.uet.dieulinh.weatherappdemo.R
import com.uet.dieulinh.weatherappdemo.base.ViewModelBaseDialog
import com.uet.dieulinh.weatherappdemo.extension.createViewModel
import com.uet.dieulinh.weatherappdemo.extension.hideLoadingDialog
import com.uet.dieulinh.weatherappdemo.extension.showLoadingDialog
import com.uet.dieulinh.weatherappdemo.view.adapter.CityAdapter
import com.uet.dieulinh.weatherappdemo.viewmodel.WeatherListViewModel
import kotlinx.android.synthetic.main.dialog_add_city.*
import kotlinx.android.synthetic.main.view_list.*

/**
 * Created by dieulinh on 02/06/2019 at 06:57
 */

class SearchCityDialog : ViewModelBaseDialog<WeatherListViewModel>() {

    lateinit var cityAdapter: CityAdapter
    private lateinit var onSelectedCallback: (cityId: String) -> Unit

    override fun createViewModel(): WeatherListViewModel {
        return createViewModel(WeatherListViewModel::class.java, targetFragment!!)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.attributes?.windowAnimations = R.style.DialogAnimationNormal
        }
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getContentViewId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        initComponents()
        initData()
        registerListener()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.decorView?.setBackgroundResource(android.R.color.transparent)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_rounded_5dp)
        dialog?.window?.setGravity(Gravity.CENTER)
    }

    override fun getContentViewId(): Int {
        return R.layout.dialog_add_city
    }

    override fun initViews() {
    }

    override fun initComponents() {
        cityAdapter = CityAdapter()
        rcvList.adapter = cityAdapter
    }

    override fun initData() {
    }

    override fun registerListener() {
        btnClose.setOnClickListener {
            dismiss()
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                cityAdapter.filter.filter(query)
                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                cityAdapter.filter.filter(newText)
                return true
            }

        })
        cityAdapter.setItemClickListener {
            if (::onSelectedCallback.isInitialized) {
                onSelectedCallback.invoke(it.id)
            }
        }
    }

    fun setSelectedCallback(callback: (cityId: String) -> Unit) {
        onSelectedCallback = callback
    }
}