package com.geekbrains.myweather_kotlin1.model.forecast

class Weather(val weatherEvent: WeatherEvent, val temp: Int) {

    companion object {

        fun randomWeather() : Weather {

            val event = WeatherEvent.Clear()

            return Weather(event, (-20..30).random())
        }

        fun fromDTO(timeWeatherDTO: TimeWeatherDTO) = Weather(getWeatherEvent(timeWeatherDTO), timeWeatherDTO?.temp_avg ?: 0)

        private fun getWeatherEvent(timeDTO: TimeWeatherDTO) : WeatherEvent {
            timeDTO?.condition?.let {
                if (timeDTO.condition.contains("snow"))
                    return WeatherEvent.Snow(timeDTO.prec_mm)
                if (timeDTO.condition.contains("rain"))
                    return WeatherEvent.Rain(timeDTO.prec_mm)
                if (timeDTO.wind_speed >= 2)
                    return WeatherEvent.Wind(timeDTO.wind_speed)
            }
            return WeatherEvent.Clear()
        }
    }
}