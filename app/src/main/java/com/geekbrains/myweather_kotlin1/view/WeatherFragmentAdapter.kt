package com.geekbrains.myweather_kotlin1.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.model.Forecast.DayTimeWeatherForecast
import com.geekbrains.myweather_kotlin1.model.Forecast.DayWeatherForecast

class WeatherFragmentAdapter : RecyclerView.Adapter<WeatherFragmentAdapter.WeatherViewHolder>() {

    private var dayWeatherForecasts: List<DayWeatherForecast> = listOf()

    fun setWeatherForecasts(data: List<DayWeatherForecast>) {
        dayWeatherForecasts = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherViewHolder {
        return WeatherViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_weather, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(dayWeatherForecasts[position])
    }

    override fun getItemCount(): Int {
        return dayWeatherForecasts.size
    }

    inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(dayWeatherForecast: DayWeatherForecast) {
            itemView.findViewById<TextView>(R.id.week_day).text = dayWeatherForecast.day.dayName
            itemView.findViewById<TextView>(R.id.morning_forecast).text = dayWeatherForecast.timeForecasts[0].toString()
            itemView.findViewById<TextView>(R.id.noon_forecast).text = dayWeatherForecast.timeForecasts[1].toString()
            itemView.findViewById<TextView>(R.id.evening_forecast).text = dayWeatherForecast.timeForecasts[2].toString()
            itemView.findViewById<TextView>(R.id.night_forecast).text = dayWeatherForecast.timeForecasts[3].toString()
        }
    }
}