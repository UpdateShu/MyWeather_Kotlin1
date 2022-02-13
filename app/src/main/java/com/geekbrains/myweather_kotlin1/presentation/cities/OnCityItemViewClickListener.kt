package com.geekbrains.myweather_kotlin1.presentation.cities

import com.geekbrains.myweather_kotlin1.model.City

interface OnCityItemViewClickListener {
    fun onCityItemViewClick(city: City)
    fun onLongCityItemViewClick(city: City)
    fun onCityCheckedChanged(checkedCities: ArrayList<City>)
}