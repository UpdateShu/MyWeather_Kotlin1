package com.geekbrains.myweather_kotlin1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.Forecast.DayWeatherForecast
import com.geekbrains.myweather_kotlin1.model.IRepository
import com.geekbrains.myweather_kotlin1.model.Repository

class WeatherViewModel(private val liveDataToObserve : MutableLiveData<AppState> = MutableLiveData()) : ViewModel() {

    private val repository: IRepository = Repository()

    fun getLiveData() : LiveData<AppState> = liveDataToObserve

    fun getWeatherForecasts(selectedCity: City?) {
        liveDataToObserve.postValue(AppState.Loading)
        Thread{
            Thread.sleep(500)
            //when ((0..1).random()) {
            //    0 -> {
                    val data = AppData()
                    var city = selectedCity
                    if (city == null) {
                        city = repository.getCurrentCity()
                    }
                    data.currentCity = city
                    if (data.currentCity != null)
                        data.weatherForecasts = repository.getWeekWeatherForecasts(data.currentCity!!)
                    liveDataToObserve.postValue(AppState.Success(data))
            //    }
            //    1 -> {
            //        liveDataToObserve.postValue(AppState.Error(Throwable("No success!")))
            //    }
            //}
        }.start()
    }
}