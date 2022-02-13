package com.geekbrains.myweather_kotlin1.presentation.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.model.forecast.DayTimeWeatherForecast
import com.geekbrains.myweather_kotlin1.model.forecast.DayWeatherForecast

class WeatherFragmentAdapter : RecyclerView.Adapter<WeatherFragmentAdapter.WeatherViewHolder>() {

    private var dayWeatherForecasts: List<DayWeatherForecast> = listOf()

    fun setWeatherForecasts(data: MutableList<DayWeatherForecast>) {
        dayWeatherForecasts = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WeatherViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather, parent, false) as View)

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(dayWeatherForecasts[position])
    }

    override fun getItemCount(): Int = dayWeatherForecasts.size
    
    inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(dayWeatherForecast: DayWeatherForecast) {

            itemView.findViewById<TextView>(R.id.week_day).text = dayWeatherForecast.day.dayName
            setDayTimeWeather(itemView, R.id.morningWeather, dayWeatherForecast.timeForecasts[0])
            setDayTimeWeather(itemView, R.id.noonWeather, dayWeatherForecast.timeForecasts[1])
            setDayTimeWeather(itemView, R.id.eveningWeather, dayWeatherForecast.timeForecasts[2])
            setDayTimeWeather(itemView, R.id.nightWeather, dayWeatherForecast.timeForecasts[3])
        }
    }

    private fun setDayTimeWeather(itemView: View, timeViewId : Int, timeWeather : DayTimeWeatherForecast) {
            val parent = itemView.findViewById<FrameLayout>(timeViewId)
        val timeView = LayoutInflater.from(parent.context).inflate(R.layout.item_time, parent, true)
        //timeView.findViewById<TextView>(R.id.weatherEvent).text = timeWeather.weather.weatherEvent.name
        timeView.findViewById<TextView>(R.id.temp).text = timeWeather.weather.temp.toString()
        val imgView = timeView.findViewById(R.id.imgEvent) as ImageView
        timeWeather.weather.weatherEvent.eventImg?.let {
//            GlideToVectorYou.justLoadImage(
//                activity,
//                Uri.parse("https://yastatic.net/weather/i/icons/blueye/color/svg/${it}.svg"),
//                weatherIcon
//            )
//        }
           // Glide.with(parent.context)
        // .load(timeWeather.weather.weatherEvent.eventImg)
         //       .into(imgView)
        }
    }
}