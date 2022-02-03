package com.geekbrains.myweather_kotlin1.model

sealed class AppState {

    data class Success(val appData: AppData) : AppState()
    data class Error(val error: Throwable): AppState()
    object Loading: AppState()
}