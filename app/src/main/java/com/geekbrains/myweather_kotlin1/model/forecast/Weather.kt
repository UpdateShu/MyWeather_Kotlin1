package com.geekbrains.myweather_kotlin1.model.forecast

import com.geekbrains.myweather_kotlin1.model.forecast.dto.TimeWeatherDTO

class Weather(val weatherEvent: WeatherEvent, val temp: Int) {

    companion object {

        fun randomWeather() : Weather {

            val event = WeatherEvent.Clear(null)

            return Weather(event, (-20..30).random())
        }

        fun fromDTO(timeWeatherDTO: TimeWeatherDTO, icon: String) = Weather(getWeatherEvent(timeWeatherDTO, icon), timeWeatherDTO?.temp_avg ?: 0)

        private fun getWeatherEvent(timeDTO: TimeWeatherDTO, icon : String) : WeatherEvent {
            timeDTO?.condition?.let {
                if (timeDTO.condition.contains("snow"))
                    return WeatherEvent.Snow(timeDTO.prec_mm, icon)
                if (timeDTO.condition.contains("rain"))
                    return WeatherEvent.Rain(timeDTO.prec_mm, icon)
                if (timeDTO.wind_speed >= 2)
                    return WeatherEvent.Wind(timeDTO.wind_speed, icon)
            }
            return WeatherEvent.Clear(icon)
        }
    }
}