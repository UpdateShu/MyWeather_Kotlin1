package com.geekbrains.myweather_kotlin1.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.myweather_kotlin1.model.*
import com.geekbrains.myweather_kotlin1.model.forecast.DayWeatherForecast
import com.geekbrains.myweather_kotlin1.model.CallBack as CallBack

class WeatherViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) : ViewModel() {

    fun getLiveData() : LiveData<AppState> = liveDataToObserve

    @RequiresApi(Build.VERSION_CODES.N)
    fun getWeatherForecasts(selectedCity: City?) {
        liveDataToObserve.postValue(AppState.Loading)
        when ((0..1).random()) {
            0 -> {
                WebRepository.loadWeatherForecasts(selectedCity ?: Repository.getCurrentCity(), liveDataToObserve)
            }
            1 -> {
                liveDataToObserve.postValue(AppState.Error(Throwable("No success!")))
            }
        }
    }
}