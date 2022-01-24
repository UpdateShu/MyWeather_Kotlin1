package com.geekbrains.myweather_kotlin1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.IRepository
import com.geekbrains.myweather_kotlin1.model.Repository

class WeatherViewModel(private val liveDataToObserve : MutableLiveData<AppState> = MutableLiveData()) : ViewModel() {

    fun getLiveData() : LiveData<AppState> = liveDataToObserve

    fun getWeatherForecasts(selectedCity: City?) {
        liveDataToObserve.postValue(AppState.Loading)
        Thread {
            Thread.sleep(500)
            when ((0..1).random()) {
                0 -> {
                    liveDataToObserve.postValue(AppState.Success(AppData().also {
                       data -> data.currentCity = selectedCity ?: Repository.getCurrentCity()
                       data.currentCity?.let { data.weatherForecasts = Repository.getWeekWeatherForecasts(it) }
                    }))
                }
                1 -> {
                   liveDataToObserve.postValue(AppState.Error(Throwable("No success!")))
                }
            }
        }.start()
    }
}