package com.geekbrains.myweather_kotlin1.presentation.weather

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.myweather_kotlin1.model.forecast.*
import com.geekbrains.myweather_kotlin1.repository.IWeatherRepository
import com.geekbrains.myweather_kotlin1.repository.RemoteDataSource
import com.geekbrains.myweather_kotlin1.repository.WeatherRepository
import com.geekbrains.myweather_kotlin1.model.AppData
import com.geekbrains.myweather_kotlin1.model.AppState
import com.geekbrains.myweather_kotlin1.model.forecast.dto.TimeWeatherDTO
import com.geekbrains.myweather_kotlin1.model.forecast.dto.WeatherDTO
import com.geekbrains.myweather_kotlin1.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

@RequiresApi(Build.VERSION_CODES.O)
class WeatherViewModel(
    private val mutableWeatherLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val weatherRepository: IWeatherRepository = WeatherRepository(RemoteDataSource())
) : ViewModel() {

    val weatherLiveData: LiveData<AppState> get() = mutableWeatherLiveData

    private val callBack = object :
        Callback<WeatherDTO> {

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            val serverResponse: WeatherDTO? = response.body()
            mutableWeatherLiveData.postValue(
                if (response.isSuccessful && serverResponse != null && (0..1).random() == 0) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            mutableWeatherLiveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: WeatherDTO): AppState {
            val fact = serverResponse.fact
            return if (fact?.temp == null || fact.feels_like == null || fact.condition.isNullOrEmpty()) {
                AppState.Error(Throwable(CORRUPTED_DATA))
            } else {
                AppState.Success(
                    AppData().apply { weatherForecasts = convertDtoToModel(serverResponse) } )
            }
        }
    }

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        mutableWeatherLiveData.value = AppState.Loading
        weatherRepository.getWeatherForecastsFromServer(lat, lon, callBack)
    }

    public fun convertDtoToModel(weatherDTO: WeatherDTO) : MutableList<DayWeatherForecast> {
        val weatherForecasts : MutableList<DayWeatherForecast> = arrayListOf()
        if (weatherDTO.forecasts == null)
            return weatherForecasts

        for (forecast in weatherDTO.forecasts!!) {
            val date = LocalDate.parse(forecast.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val day = WeekDay.values()[date.dayOfWeek.ordinal]

            val timeWeatherForecasts : MutableList<DayTimeWeatherForecast> = arrayListOf()
            addTimeWeatherForecast(DaysTime.Morning, forecast.parts!!.morning, timeWeatherForecasts)
            addTimeWeatherForecast(DaysTime.Noon, forecast.parts!!.day, timeWeatherForecasts)
            addTimeWeatherForecast(DaysTime.Evening, forecast.parts!!.evening, timeWeatherForecasts)
            addTimeWeatherForecast(DaysTime.Night, forecast.parts!!.night, timeWeatherForecasts)

            weatherForecasts.add((DayWeatherForecast(day, timeWeatherForecasts)))
        }
        return weatherForecasts
    }
}

private fun addTimeWeatherForecast(daysTime: DaysTime, timeWeatherDTO : TimeWeatherDTO,
                                   timeWeatherForecasts: MutableList<DayTimeWeatherForecast>) {
    timeWeatherForecasts.add(DayTimeWeatherForecast(daysTime, Weather.fromDTO(timeWeatherDTO,
        "https://yastatic.net/weather/i/icons/funky/dark/${timeWeatherDTO.icon}.svg")))
}
