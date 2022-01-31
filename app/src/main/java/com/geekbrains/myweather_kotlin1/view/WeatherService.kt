package com.geekbrains.myweather_kotlin1.view

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.geekbrains.myweather_kotlin1.model.forecast.WeatherDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val LATITUDE_EXTRA = "Latitude"
const val LONGITUDE_EXTRA = "Longitude"
private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 10000
private const val YOUR_API_KEY = "1b1206d3-40ea-41d0-a443-adf1da771cbc"

class WeatherService(name: String = "WeatherService") : IntentService(name)  {
    private val broadcastIntent = Intent(WEATHER_INTENT_FILTER)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            onEmptyIntent()
        } else {
            val lat = intent.getDoubleExtra(LATITUDE_EXTRA, 0.0)
            val lon = intent.getDoubleExtra(LONGITUDE_EXTRA, 0.0)
            if (lat == 0.0 && lon == 0.0) {
                onEmptyData()
            } else {
                when ((0..1).random()) {
                    0 -> {
                        loadWeatherForecasts(lat.toString(), lon.toString())
                    }
                    1 -> {
                        onErrorRequest("Test error!")
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadWeatherForecasts(lat: String, lon: String)
    {
        try {
            val uri =
                URL("https://api.weather.yandex.ru/v2/forecast?lat=${lat}&lon=${lon}&limit=${7}")

            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.apply {
                    requestMethod = REQUEST_GET
                    readTimeout = REQUEST_TIMEOUT
                    addRequestProperty("X-Yandex-API-Key", YOUR_API_KEY)
                }
                // преобразование ответа от сервера (JSON) в модель данных (WeatherDTO)
                val weatherDTO = Gson().fromJson(getLines(BufferedReader(InputStreamReader(urlConnection.inputStream))),
                    WeatherDTO::class.java)
                onResponse(weatherDTO)
            } catch (e: Exception) {
                onErrorRequest(e.message ?: "Fail connection")
            } finally {
                urlConnection.disconnect()
            }
        }
        catch (e: MalformedURLException) {
            onMalformedURL()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    private fun onResponse(weatherDTO: WeatherDTO) {
        val fact = weatherDTO.fact
        if (fact == null) {
            onEmptyResponse()
        } else {
            onSuccessResponse(weatherDTO)
        }
    }

    private fun onSuccessResponse(weatherDTO: WeatherDTO?) {
        putLoadResult(WEATHER_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(WEATHER_DTO_EXTRA, weatherDTO as Parcelable)
        sendBroadcast(broadcastIntent)
    }

    private fun onMalformedURL() {
        putLoadResult(WEATHER_URL_MALFORMED_EXTRA)
        sendBroadcast(broadcastIntent)
    }

    private fun onErrorRequest(error: String) {
        putLoadResult(WEATHER_REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(WEATHER_REQUEST_ERROR_MESSAGE_EXTRA, error)
        sendBroadcast(broadcastIntent)
    }

    private fun onEmptyResponse() {
        putLoadResult(WEATHER_RESPONSE_EMPTY_EXTRA)
        sendBroadcast(broadcastIntent)
    }

    private fun onEmptyIntent() {
        putLoadResult(WEATHER_INTENT_EMPTY_EXTRA)
        sendBroadcast(broadcastIntent)
    }

    private fun onEmptyData() {
        putLoadResult(WEATHER_DATA_EMPTY_EXTRA)
        sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(WEATHER_LOAD_RESULT_EXTRA, result)
    }
}