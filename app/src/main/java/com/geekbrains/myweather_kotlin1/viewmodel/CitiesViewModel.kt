package com.geekbrains.myweather_kotlin1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.myweather_kotlin1.model.IRepository
import com.geekbrains.myweather_kotlin1.model.Repository

class CitiesViewModel(private val liveDataToObserve : MutableLiveData<AppState> = MutableLiveData()) : ViewModel() {
    private val repository: IRepository = Repository()

    fun getLiveData() : LiveData<AppState> = liveDataToObserve

    fun getCities() {
        liveDataToObserve.postValue(AppState.Loading)
        Thread{
            Thread.sleep(500)
            //when ((0..1).random()) {
            //    0 -> {
                    val data = AppData()
                    data.cities = repository.getCities()
                    liveDataToObserve.postValue(AppState.Success(data))
            //    }
            //    1 -> {
            //        liveDataToObserve.postValue(AppState.Error(Throwable("No success!")))
            //    }
            //}
        }.start()
    }
}