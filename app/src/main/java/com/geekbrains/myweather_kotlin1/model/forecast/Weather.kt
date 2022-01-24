package com.geekbrains.myweather_kotlin1.model.forecast

class Weather(val weatherEvent: WeatherEvent, val temp: Int) {

    companion object {

        fun randomWeather() : Weather {

            val event = when((0..2).random()) {
                0 -> WeatherEvent.Wind((0..20).random())
                1 -> WeatherEvent.Rain((1..10).random())
                2-> WeatherEvent.Snow((1..30).random())
                else -> WeatherEvent.Clear()
            }

            return Weather(event, (-20..30).random())
        }
    }
}