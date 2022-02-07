package com.geekbrains.myweather_kotlin1.presentation.weather

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.myweather_kotlin1.model.forecast.*
import com.geekbrains.myweather_kotlin1.model.AppData
import com.geekbrains.myweather_kotlin1.model.AppState
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.forecast.dto.WeatherDTO
import com.geekbrains.myweather_kotlin1.presentation.App
import com.geekbrains.myweather_kotlin1.repository.*
import com.geekbrains.myweather_kotlin1.utils.Constants
import com.geekbrains.myweather_kotlin1.utils.convertDtoToModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

@RequiresApi(Build.VERSION_CODES.O)
class WeatherViewModel(
    private val mutableWeatherLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val weatherRepository: IWeatherRepository = WeatherRepository(RemoteDataSource())
) : ViewModel() {

    val weatherLiveData: LiveData<AppState> get() = mutableWeatherLiveData

    private val localRepository: ILocalRepository = LocalRepository(App.getHistoryDao())

    fun saveCurrentCity(city: City, preferences: SharedPreferences) = localRepository.saveCurrentCity(city, preferences)

    fun getCurrentCity(preferences: SharedPreferences) = localRepository.getCurrentCity(preferences)

    private val callBack = object :
        Callback<WeatherDTO> {

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            val serverResponse: WeatherDTO? = response.body()
            mutableWeatherLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
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
                    AppData().apply { weekWeather = convertDtoToModel(serverResponse)
                        } )
            }
        }
    }

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        this.
        mutableWeatherLiveData.value = AppState.Loading
        weatherRepository.getWeatherForecastsFromServer(lat, lon, callBack)
    }

    fun saveHistory(city: City?, weekWeather: WeekWeather){
        city?.let { localRepository.saveHistory(city, weekWeather) }
    }
}
