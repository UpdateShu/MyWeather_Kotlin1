package com.geekbrains.myweather_kotlin1.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.databinding.CitiesFragmentBinding
import com.geekbrains.myweather_kotlin1.databinding.WeatherFragmentBinding
import com.geekbrains.myweather_kotlin1.viewmodel.AppState
import com.geekbrains.myweather_kotlin1.viewmodel.WeatherViewModel

class WeatherFragment : Fragment() {

    companion object {
        fun newInstance() = WeatherFragment()
    }

    private var _binding: WeatherFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        val observer = Observer<AppState> {  data ->
            renderData(data)
        }
        with(viewModel) {
            getLiveData().observe(viewLifecycleOwner, observer)
            getWeatherForecast()
        }
    }

    private fun renderData(state: AppState) {
        when (state){
            is AppState.Success -> {
                binding.city.text = state.city.name
                val weatherForecasts = state.weatherForecasts;
                binding.morning.text = weatherForecasts[0].toString()
                binding.noon.text = weatherForecasts[1].toString()
                binding.evening.text = weatherForecasts[2].toString()
                binding.night.text = weatherForecasts[3].toString()
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