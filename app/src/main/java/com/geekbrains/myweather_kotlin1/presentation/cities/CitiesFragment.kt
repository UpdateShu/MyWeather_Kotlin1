package com.geekbrains.myweather_kotlin1.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.databinding.CitiesFragmentBinding
import com.geekbrains.myweather_kotlin1.model.AppState
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.showLoading
import com.geekbrains.myweather_kotlin1.model.showSnackBar
import com.geekbrains.myweather_kotlin1.presentation.cities.CitiesFragmentAdapter
import com.geekbrains.myweather_kotlin1.presentation.cities.CitiesViewModel
import com.geekbrains.myweather_kotlin1.presentation.cities.OnCityItemViewClickListener
import com.geekbrains.myweather_kotlin1.presentation.weather.WeatherFragment

class CitiesFragment : Fragment() {

    companion object {
        const val EXTRA_CITY = "city"

        fun newInstance(bundle: Bundle): CitiesFragment {
            val fragment = CitiesFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _binding: CitiesFragmentBinding? = null

    private val binding get() = _binding!!

    private var currentCity: City? = null

    private var editMode: Boolean = false

    private val viewModel: CitiesViewModel by lazy {
        ViewModelProvider(this).get(CitiesViewModel::class.java)
    }

    private val adapter = CitiesFragmentAdapter(false, R.layout.item_city, object :
        OnCityItemViewClickListener {
        override fun onCityCheckedChanged(checkedCities: ArrayList<City>) {

        }

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onCityItemViewClick(city: City) {
            activity?.supportFragmentManager?.let { manager ->
                manager.beginTransaction()
                    .replace(R.id.container, WeatherFragment().also { fragment ->
                        fragment.arguments = Bundle().also { bundle ->
                            bundle.putParcelable(EXTRA_CITY, city)
                        }
                    })
                    .addToBackStack("")
                    .commit()
            }
        }

        override fun onLongCityItemViewClick(city: City) {
            binding.citiesView.recycledViewPool.clear()
            //refreshCities()
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CitiesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.citiesView.adapter = adapter

        currentCity = arguments?.getParcelable(WeatherFragment.EXTRA_CITY)
        viewModel.getLiveData().observe(viewLifecycleOwner, { data -> loadCitiesData(data) })
        refreshCities()
    }

    private fun refreshCities() {
        currentCity?.let { Toast.makeText(context, currentCity!!.name, Toast.LENGTH_SHORT).show() }
        viewModel.getCities(currentCity)
    }

    private fun loadCitiesData(state: AppState) {
        when (state) {
            is AppState.Success -> {
                binding.citiesLoading.visibility = View.GONE
                binding.citiesView.isVisible = true

                state.appData.currentCity?.let {
                    val cityIndex = state.appData.cities.indexOf(state.appData.currentCity!!)
                    binding.citiesView.smoothScrollToPosition(cityIndex)
                }
                adapter.setCities(state.appData.cities)
            }
            is AppState.Loading -> {
                context?.showLoading(R.string.loading_message)
            }
            is AppState.Error -> {
                binding.citiesView.showSnackBar(getString(R.string.loading_error),
                    getString(R.string.repeat_loading), { refreshCities() })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.removeListener()
        _binding = null
    }
}