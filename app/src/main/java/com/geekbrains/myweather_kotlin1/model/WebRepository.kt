package com.geekbrains.myweather_kotlin1.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.geekbrains.myweather_kotlin1.model.forecast.*
import com.geekbrains.myweather_kotlin1.viewmodel.AppData
import com.geekbrains.myweather_kotlin1.viewmodel.AppState
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

object WebRepository : IRepository {

    private const val YOUR_API_KEY = "1b1206d3-40ea-41d0-a443-adf1da771cbc"

    override fun getCities(): ArrayList<City> {
        TODO("Not yet implemented")
    }

    override fun getCurrentCity(): City {
        TODO("Not yet implemented")
    }

    override fun getWeekWeatherForecasts(city: City): MutableList<DayWeatherForecast> {
        TODO("Not yet implemented")
    }

    override fun getWeatherForecasts(city: City): MutableList<DayTimeWeatherForecast> {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadWeatherForecasts(
        city: City, liveDataToObserve : MutableLiveData<AppState>) {

        var weatherDTO : WeatherDTO? = null
        try {
            val uri =
                URL("https://api.weather.yandex.ru/v2/forecast?lat=${city.lat}&lon=${city.lon}&limit=${7}")

            Thread(Runnable {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.addRequestProperty(
                        "X-Yandex-API-Key",
                        YOUR_API_KEY
                    )
                    urlConnection.readTimeout = 10000
                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))

                    // преобразование ответа от сервера (JSON) в модель данных (WeatherDTO)
                    weatherDTO = Gson().fromJson(getLines(bufferedReader), WeatherDTO::class.java)

                    liveDataToObserve.postValue(AppState.Success(AppData().also { data ->
                        data.currentCity = city
                        if (weatherDTO != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            data.weatherForecasts = getWeatherForecasts(weatherDTO!!)
                        }
                    }))

                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    liveDataToObserve.postValue(AppState.Error(e))

                } finally {
                    urlConnection.disconnect()
                }
            }).start()
        }
        catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            liveDataToObserve.postValue(AppState.Error(e))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getWeatherForecasts(weatherDTO: WeatherDTO) : MutableList<DayWeatherForecast> {
        val weatherForecasts : MutableList<DayWeatherForecast> = arrayListOf()
        if (weatherDTO.forecasts == null)
            return weatherForecasts
        for (forecast in weatherDTO.forecasts!!) {
            val date = LocalDate.parse(forecast.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val day = WeekDay.values()[date.dayOfWeek.ordinal]
            val timeWeatherForecasts : MutableList<DayTimeWeatherForecast> = arrayListOf()
            timeWeatherForecasts.add(DayTimeWeatherForecast(DaysTime.Morning, Weather.fromDTO(forecast.parts!!.morning)))
            timeWeatherForecasts.add(DayTimeWeatherForecast(DaysTime.Noon, Weather.fromDTO(forecast.parts!!.day)))
            timeWeatherForecasts.add(DayTimeWeatherForecast(DaysTime.Evening, Weather.fromDTO(forecast.parts!!.evening)))
            timeWeatherForecasts.add(DayTimeWeatherForecast(DaysTime.Night, Weather.fromDTO(forecast.parts!!.night)))


            weatherForecasts.add((DayWeatherForecast(day, timeWeatherForecasts)))
        }
        return weatherForecasts
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }
}