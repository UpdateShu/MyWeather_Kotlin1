package com.geekbrains.myweather_kotlin1.repository

import com.geekbrains.myweather_kotlin1.model.City

interface IRepository {

    fun getCities() : ArrayList<City>
    fun getCurrentCity() : City
}