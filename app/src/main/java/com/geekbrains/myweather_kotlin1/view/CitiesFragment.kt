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
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.viewmodel.AppState
import com.geekbrains.myweather_kotlin1.viewmodel.CitiesViewModel
import com.geekbrains.myweather_kotlin1.viewmodel.WeatherViewModel

class CitiesFragment : Fragment() {

    companion object {
        fun newInstance() = CitiesFragment()
    }

    private var _binding: CitiesFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: CitiesViewModel

    private val adapter = CitiesFragmentAdapter(object : OnCityItemViewClickListener {
        override fun onCityItemViewClick(city: City) {
            val manager = activity?.supportFragmentManager
            if (manager != null) {
                val bundle = Bundle()
                bundle.putParcelable(WeatherFragment.BUNDLE_EXTRA, city)
                manager.beginTransaction()
                    .replace(R.id.container, WeatherFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commit()
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CitiesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.citiesView.adapter = adapter

        viewModel = ViewModelProvider(this).get(CitiesViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer<AppState> { loadCitiesData(it) })
        viewModel.getCities()
    }

    private fun loadCitiesData(state: AppState) {
        when (state){
            is AppState.Success -> {
                binding.citiesLoading.visibility = View.GONE
                binding.citiesView.isVisible = true
                adapter.setCities(state.appData.cities)
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
        adapter.removeListener()
        super.onDestroy()
    }
}

interface OnCityItemViewClickListener {
    fun onCityItemViewClick(city: City)
}