package com.geekbrains.myweather_kotlin1.presentation.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.repository.Repository
import com.geekbrains.myweather_kotlin1.model.AppData
import com.geekbrains.myweather_kotlin1.model.AppState

class CitiesViewModel(private val liveDataToObserve : MutableLiveData<AppState> = MutableLiveData()) : ViewModel() {

    fun getLiveData() : LiveData<AppState> = liveDataToObserve

    fun getCities(currentCity: City?) {
        liveDataToObserve.postValue(AppState.Loading)
        Thread{
            Thread.sleep(200)
            when ((0..0).random()) {
                0 -> {
                    liveDataToObserve.postValue(AppState.Success(AppData().also { data ->
                        data.currentCity = currentCity
                        data.cities = Repository.getCities()
                    }))

                }
                1 -> {
                    liveDataToObserve.postValue(AppState.Error(Throwable("No success!")))
                }
            }
        }.start()
    }
}