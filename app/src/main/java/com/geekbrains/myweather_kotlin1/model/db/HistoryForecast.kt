package com.geekbrains.myweather_kotlin1.model.db

import android.os.Build
import androidx.annotation.RequiresApi
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.forecast.DaysTime
import com.geekbrains.myweather_kotlin1.utils.getFormattedDate
import java.time.LocalDate

data class HistoryForecast(val city: City,
                           val date: LocalDate,
                           val morning: String?,
                           val day: String? = null,
                           val evening: String? = null,
                           val night: String? = null) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun toString(): String {
        var history : String = "${city.name}, ${getFormattedDate(date)}: "
        morning?.let { history += "${getTimeForecast(DaysTime.Morning, morning)}, "}
        day?.let {  history += "${getTimeForecast(DaysTime.Day, day)}, "}
        evening?.let { history += "${getTimeForecast(DaysTime.Evening, evening)}, "}
        night?.let { history += "${getTimeForecast(DaysTime.Morning, night)}"}
        return history
    }

    private fun getTimeForecast(daysTime: DaysTime, forecast: String) = "(${daysTime.timeName}) $forecast"
}

