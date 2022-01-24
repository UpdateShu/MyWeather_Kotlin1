package com.geekbrains.myweather_kotlin1.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

class WeatherFragment : Fragment() {

    companion object {
        const val BUNDLE_EXTRA = "city"
    }

    private var _binding: WeatherFragmentBinding? = null

    private val binding get() = _binding!!

    private var currentCity : City? = null

    private val viewModel: WeatherViewModel by lazy {
        ViewModelProvider(this)[WeatherViewModel::class.java]
    }

    private val adapter = WeatherFragmentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.weatherForecastsView.adapter = adapter
        binding.city.setOnClickListener { changeCity() }
        currentCity = arguments?.getParcelable(BUNDLE_EXTRA)
        viewModel.getLiveData().observe(viewLifecycleOwner, { data -> renderData(data) })
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

    private fun refreshWeather() {
        currentCity?.let { viewModel.getWeatherForecasts(currentCity) }
    }

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
            }
            is AppState.Loading -> {
                context?.showLoading(R.string.loading_message)
            }
            is AppState.Error -> {
                binding.city.showSnackBar(R.string.loading_error)
                refreshWeather()

                //binding.weatherForecastsView.showSnackBar(getString(R.string.loading_error),
                    //getString(R.string.repeat_loading), { refreshWeather() })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
