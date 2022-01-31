package com.geekbrains.myweather_kotlin1.view

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.databinding.CitiesFragmentBinding
import com.geekbrains.myweather_kotlin1.databinding.WeatherFragmentBinding
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.showLoading
import com.geekbrains.myweather_kotlin1.model.showSnackBar
import com.geekbrains.myweather_kotlin1.viewmodel.AppState
import com.geekbrains.myweather_kotlin1.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.weather_fragment.*
import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log
import android.net.ConnectivityManager

import android.content.IntentFilter
import android.net.Network
import android.net.NetworkInfo
import com.geekbrains.myweather_kotlin1.model.Repository
import com.geekbrains.myweather_kotlin1.model.forecast.WeatherDTO as WeatherDTO

const val WEATHER_INTENT_FILTER = "WEATHER INTENT FILTER"
const val WEATHER_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val WEATHER_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val WEATHER_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val WEATHER_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val WEATHER_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val WEATHER_DTO_EXTRA = "WEATHER_DTO"
const val WEATHER_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val WEATHER_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val WEATHER_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"

class WeatherFragment : Fragment() {

    companion object {
        const val BUNDLE_EXTRA = "city"
    }

    private val networkChangeReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            StringBuilder().apply {
                append("СООБЩЕНИЕ ОТ СИСТЕМЫ\n")
                append("Action: ${intent.action}\n")
                intent.extras?.let { (intent.extras!![ConnectivityManager.EXTRA_NETWORK_INFO] as NetworkInfo).also {
                    if (it.state == NetworkInfo.State.CONNECTED) {
                        append("Network ${it.typeName}") }
                    }
                    if (intent.extras!!.getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
                        append("\nThere's no network connectivity") }
                    }
                toString().also {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            Log.d("app", "Network connectivity change")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val loadResultsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            when (intent.getStringExtra(WEATHER_LOAD_RESULT_EXTRA)) {
                WEATHER_INTENT_EMPTY_EXTRA -> onError()
                WEATHER_DATA_EMPTY_EXTRA -> onError()
                WEATHER_RESPONSE_EMPTY_EXTRA -> onError()
                WEATHER_REQUEST_ERROR_EXTRA -> onError()
                WEATHER_REQUEST_ERROR_MESSAGE_EXTRA -> onError()
                WEATHER_URL_MALFORMED_EXTRA -> onError()
                WEATHER_RESPONSE_SUCCESS_EXTRA -> displayWeather(intent.getParcelableExtra(WEATHER_DTO_EXTRA))
                else -> onError()
            }
        }
    }

    private var _binding: WeatherFragmentBinding? = null

    private val binding get() = _binding!!

    private var currentCity : City? = null

    /*private val viewModel: WeatherViewModel by lazy {
        ViewModelProvider(this)[WeatherViewModel::class.java]
    }*/

    private val adapter = WeatherFragmentAdapter()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.registerReceiver(loadResultsReceiver, IntentFilter(WEATHER_INTENT_FILTER))
        context?.registerReceiver(networkChangeReceiver, IntentFilter().also {
            it.addAction(ConnectivityManager.CONNECTIVITY_ACTION) })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.weatherForecastsView.adapter = adapter
        binding.city.setOnClickListener { changeCity() }

        currentCity = arguments?.getParcelable(BUNDLE_EXTRA) ?: Repository.getCurrentCity()
        //viewModel.getLiveData().observe(viewLifecycleOwner, { data -> renderData(data) })
        refreshWeather()
    }

    private fun changeCity() {
        activity?.supportFragmentManager?.let { manager -> manager.beginTransaction()
            .replace(R.id.container, CitiesFragment().also {
                    fragment -> fragment.arguments = Bundle().also { bundle -> bundle.putParcelable(CitiesFragment.BUNDLE_EXTRA, currentCity) }
            })
            .addToBackStack("")
            .commit() }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun refreshWeather() {
        //viewModel.getWeatherForecasts(currentCity)
        currentCity?.let { city ->
            context?.let {
                context?.showLoading(R.string.loading_message)
                it.startService(Intent(it, WeatherService::class.java).apply {
                    putExtra(
                        LATITUDE_EXTRA,
                        city.lat
                    )
                    putExtra(
                        LONGITUDE_EXTRA,
                        city.lon
                    )
                })
            }
        }
    }

    var firstLoading : Boolean = true
    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayWeather(weatherDTO: WeatherDTO?) {

        with(binding) {
            forecastsLoading.visibility = View.GONE
            weatherForecastsView.isVisible = true
            city.text = currentCity?.name
        }
        weatherDTO?.let {
            val forecasts = Repository.getWeatherForecasts(weatherDTO)
            adapter.setWeatherForecasts(forecasts)
        }
        firstLoading = true
    }

    /*@RequiresApi(Build.VERSION_CODES.N)
    private fun renderData(state: AppState) {
        when (state){
            is AppState.Success -> {
                currentCity = state.appData.currentCity
                with(binding) {
                    forecastsLoading.visibility = View.GONE
                    weatherForecastsView.isVisible = true
                    city.text = currentCity?.name
                }
                adapter.setWeatherForecasts(state.appData.weatherForecasts)
                firstLoading = true
            }
            is AppState.Loading -> {
                context?.showLoading(R.string.loading_message)
            }
            is AppState.Error -> {
                binding.city.showSnackBar(R.string.loading_error)
                if (firstLoading) {
                    binding.weatherForecastsView.showSnackBar(getString(R.string.loading_error),
                        getString(R.string.repeat_loading), { refreshWeather() })
                }
                firstLoading = false
            }
        }
    }*/

    @RequiresApi(Build.VERSION_CODES.N)
    private fun onError(){
        binding.city.showSnackBar(R.string.loading_error)
        if (firstLoading) {
            binding.weatherForecastsView.showSnackBar(getString(R.string.loading_error),
                getString(R.string.repeat_loading), { refreshWeather() })
        }
        firstLoading = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(networkChangeReceiver)
        context?.unregisterReceiver(loadResultsReceiver)
        _binding = null
    }
}
