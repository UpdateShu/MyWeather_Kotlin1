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
import com.geekbrains.myweather_kotlin1.viewmodel.AppState
import com.geekbrains.myweather_kotlin1.viewmodel.WeatherViewModel

class WeatherFragment : Fragment() {

    companion object {

        const val BUNDLE_EXTRA = "city"

        fun newInstance(bundle: Bundle): WeatherFragment {
            val fragment = WeatherFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _binding: WeatherFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: WeatherViewModel

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
        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        val observer = Observer<AppState> {  data ->
            renderData(data)
        }
        with(viewModel) {
            getLiveData().observe(viewLifecycleOwner, observer)
            val city = arguments?.getParcelable<City>(BUNDLE_EXTRA)
            getWeatherForecasts(city)
        }
    }

    private fun renderData(state: AppState) {
        when (state){
            is AppState.Success -> {
                binding.forecastsLoading.visibility = View.GONE
                binding.weatherForecastsView.isVisible = true
                binding.city.text = state.appData.currentCity?.name
                adapter.setWeatherForecasts(state.appData.weatherForecasts)
            }
            is AppState.Loading -> {
                Toast.makeText(context, "Loading!", Toast.LENGTH_SHORT).show()
            }
            is AppState.Error -> {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}