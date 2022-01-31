package com.geekbrains.myweather_kotlin1.model

interface CallBack<T> {
    fun onSuccess(result: T)
    fun onError(error: Throwable?)
}