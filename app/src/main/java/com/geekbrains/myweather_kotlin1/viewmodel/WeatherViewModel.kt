package com.geekbrains.myweather_kotlin1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.myweather_kotlin1.model.IRepository
import com.geekbrains.myweather_kotlin1.model.Repository

class WeatherViewModel(private val liveDataToObserve : MutableLiveData<AppState> = MutableLiveData()) : ViewModel() {

    private val repository: IRepository = Repository()

    fun getLiveData() : LiveData<AppState> = liveDataToObserve

    fun getWeatherForecast() {
        liveDataToObserve.postValue(AppState.Loading)
        Thread{
            Thread.sleep(2000)
            when ((0..1).random()) {
                0 -> {
                    val city = repository.getCurrentCity()
                    liveDataToObserve.postValue(AppState.Success(repository.getWeatherForecasts(city), city))
                }
                1 -> {
                    liveDataToObserve.postValue(AppState.Error(Throwable("No success!")))
                }
            }
        }.start()
    }
}