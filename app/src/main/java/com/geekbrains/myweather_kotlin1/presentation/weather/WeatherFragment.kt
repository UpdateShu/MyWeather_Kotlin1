package com.geekbrains.myweather_kotlin1.presentation.weather

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
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.databinding.WeatherFragmentBinding
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.showSnackBar
import kotlinx.android.synthetic.main.weather_fragment.*
import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log
import android.net.ConnectivityManager

import android.content.IntentFilter
import android.net.NetworkInfo
import android.os.Handler
import com.bumptech.glide.Glide
import com.geekbrains.myweather_kotlin1.BuildConfig
import com.geekbrains.myweather_kotlin1.model.AppState
import com.geekbrains.myweather_kotlin1.model.forecast.DayWeatherForecast
import com.geekbrains.myweather_kotlin1.model.showLoading
import com.geekbrains.myweather_kotlin1.repository.Repository
import com.geekbrains.myweather_kotlin1.view.CitiesFragment

@RequiresApi(Build.VERSION_CODES.N)
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

    /*@RequiresApi(Build.VERSION_CODES.O)
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
    }*/

    private var _binding: WeatherFragmentBinding? = null

    private val binding get() = _binding!!

    private var currentCity : City? = null

    private val viewModel: WeatherViewModel by lazy {
        ViewModelProvider(this)[WeatherViewModel::class.java]
    }

    private val adapter = WeatherFragmentAdapter()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //context?.registerReceiver(loadResultsReceiver, IntentFilter(WEATHER_INTENT_FILTER))
        context?.registerReceiver(networkChangeReceiver, IntentFilter().also {
            it.addAction(ConnectivityManager.CONNECTIVITY_ACTION) })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherFragmentBinding.inflate(inflater, container, false)
        Glide.with(requireContext())
            .load("https://luculentia.ru/luc-up/uploads/2018/04/1.jpg")
                .into(binding.weatherBack)
                    return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            weatherForecastsView.adapter = adapter
            city.setOnClickListener { changeCity() }
        }
        currentCity = arguments?.getParcelable(BUNDLE_EXTRA) ?: Repository.getCurrentCity()

        viewModel.weatherLiveData.observe(viewLifecycleOwner, { data -> renderData(data) })
        refreshWeather()
    }

    private fun changeCity() {
        activity?.supportFragmentManager?.let { manager -> manager.beginTransaction()
            .replace(R.id.container, CitiesFragment().also {
                    fragment -> fragment.arguments = Bundle().also { bundle -> bundle.putParcelable(
                CitiesFragment.BUNDLE_EXTRA, currentCity) }
            })
            .addToBackStack("")
            .commit() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshWeather() {
        //viewModel.getWeatherForecasts(currentCity)
        currentCity?.let { newCity ->
            with(binding) {
                forecastsLoading.visibility = View.VISIBLE
                weatherForecastsView.isVisible = false
                city.text = newCity.name
            }
            viewModel.getWeatherFromRemoteSource(newCity.lat, newCity.lon)
            /*context?.let {
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
            }*/
            /*val client = OkHttpClient() // Клиент
            val builder: Request.Builder = Request.Builder() // Создаём строителя запроса
            builder.header(REQUEST_API_KEY, BuildConfig.WEATHER_API_KEY) // Создаём заголовок запроса
            builder.url(MAIN_LINK + "lat=${currentCity!!.lat}&lon=${currentCity!!.lon}") // Формируем URL
            val request: Request = builder.build() // Создаём запрос
            val call: Call = client.newCall(request) // Ставим запрос в очередь и отправляем
            call.enqueue(object : Callback {

                val handler: Handler = Handler()

                // Вызывается, если ответ от сервера пришёл
                @Throws(IOException::class)
                override fun onResponse(call: Call?, response: Response) {
                    val serverResponse: String? = response.body()?.string()
                    // Синхронизируем поток с потоком UI
                    if (response.isSuccessful && serverResponse != null) {
                        handler.post {
                            displayWeather(Gson().fromJson(serverResponse, WeatherDTO::class.java))
                        }
                    } else {
                        //TODO(PROCESS_ERROR)
                    }
                }

                // Вызывается при сбое в процессе запроса на сервер
                override fun onFailure(call: Call?, e: IOException?) {
                    //TODO(PROCESS_ERROR)
                }
            })*/
        }
    }

    var firstLoading : Boolean = true
    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayWeatherForecasts(weatherForecasts: MutableList<DayWeatherForecast>) {
        showLoading(false)
        adapter.setWeatherForecasts(weatherForecasts)
        firstLoading = true
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderData(state: AppState) {
        when (state){
            is AppState.Success -> {
                currentCity = state.appData.currentCity ?: currentCity
                binding.city.text = currentCity?.name

                showLoading(false)
                adapter.setWeatherForecasts(state.appData.weatherForecasts)
                firstLoading = true
            }
            is AppState.Loading -> {
                showLoading(true)
            }
            is AppState.Error -> {
                showLoading(false)
                binding.city.showSnackBar(R.string.loading_error)
                if (firstLoading) {
                    binding.weatherForecastsView.showSnackBar(getString(R.string.loading_error),
                        getString(R.string.repeat_loading), { refreshWeather() })
                }
                firstLoading = false
            }
        }
    }

    private fun showLoading(isLoading : Boolean) {
        with(binding) {
            forecastsLoading.isVisible = isLoading
            weatherForecastsView.isVisible = !isLoading
        }
        if (isLoading) {
            context?.showLoading(R.string.loading_message)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onError(){
        //binding.city.showSnackBar(R.string.loading_error)
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
        _binding = null
    }
}
