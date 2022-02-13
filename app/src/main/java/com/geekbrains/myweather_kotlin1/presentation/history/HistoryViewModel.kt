package com.geekbrains.myweather_kotlin1.presentation.history

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.myweather_kotlin1.model.AppData
import com.geekbrains.myweather_kotlin1.model.AppState
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.presentation.App.Companion.getHistoryDao
import com.geekbrains.myweather_kotlin1.repository.ILocalRepository
import com.geekbrains.myweather_kotlin1.repository.LocalRepository
import com.geekbrains.myweather_kotlin1.repository.Repository

class HistoryViewModel (
    val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepository: ILocalRepository = LocalRepository(getHistoryDao())
) : ViewModel() {

    fun getCityHistory(preferences: SharedPreferences) {
        historyLiveData.value = AppState.Loading
        val city = historyRepository.getCurrentCity(preferences) ?: Repository.getCurrentCity()
        historyLiveData.value = AppState.Success(AppData().apply {
            currentCity = city
            historyEntities = historyRepository.getCityHistory(city)
        })
    }

    fun getCitiesHistory(cities : ArrayList<City>) {
        historyLiveData.value = AppState.Loading
        historyRepository
        historyLiveData.value = AppState.Success(AppData().apply {
            historyEntities = historyRepository.getCitiesHistory(cities)
        })
    }
}
