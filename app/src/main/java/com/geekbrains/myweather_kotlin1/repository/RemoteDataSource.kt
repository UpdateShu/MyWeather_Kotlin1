package com.geekbrains.myweather_kotlin1.repository

import com.geekbrains.myweather_kotlin1.BuildConfig
import com.geekbrains.myweather_kotlin1.model.ILocation
import com.geekbrains.myweather_kotlin1.model.WeatherLocation
import com.geekbrains.myweather_kotlin1.model.forecast.dto.WeatherDTO
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RemoteDataSource {
    private val weatherApi = Retrofit.Builder()
        .baseUrl("https://api.weather.yandex.ru/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .client(createOkHttpClient(WeatherApiInterceptor()))
        .build()
        .create(WeatherAPI::class.java)

    fun getWeatherForecasts(location : WeatherLocation, callback: Callback<WeatherDTO>) {
        weatherApi.getWeather(location.lat, location.lon).enqueue(callback)
    }

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        httpClient.addInterceptor(KeyInterceptor())
        return httpClient.build()
    }

    inner class WeatherApiInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            return chain.proceed(chain.request())
        }
    }

    inner class KeyInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
                .newBuilder()
                .addHeader("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)
                .build()

            return chain.proceed(request)
        }
    }

    inner class KeyInterceptorQuery : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            val url = request.url().newBuilder()
                .addQueryParameter("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)
                .build()

            request = request.newBuilder().url(url).build()

            return chain.proceed(request)
        }
    }


}
