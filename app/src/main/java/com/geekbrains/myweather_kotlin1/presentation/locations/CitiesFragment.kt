package com.geekbrains.myweather_kotlin1.presentation.locations

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.databinding.CityFiltersFragmentBinding
import com.geekbrains.myweather_kotlin1.model.AppState
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.showLoading
import com.geekbrains.myweather_kotlin1.model.showSnackBar

class CitiesFragment : Fragment() {

    companion object {
        const val CHECKED_CITIES = "cities"
        const val COUNTER = "counter"
        const val KEY_RESULT = "CityFiltersFragment_KEY_RESULT"

        fun newInstance(bundle: Bundle) : CitiesFragment {
            val fragment = CitiesFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _binding: CityFiltersFragmentBinding? = null

    private val binding get() = _binding!!

    private val viewModel: CitiesViewModel by lazy {
        ViewModelProvider(this).get(CitiesViewModel::class.java)
    }

    private var counter : Int = 0
    private val adapter = CitiesFragmentAdapter(true, R.layout.filter_city, object :
        OnCityItemViewClickListener {
        override fun onCityCheckedChanged(checkedCities: ArrayList<City>) {
            counter++
            parentFragmentManager.setFragmentResult(
                KEY_RESULT,
                Bundle().apply { putParcelableArrayList(CHECKED_CITIES, checkedCities)
                                    putInt(COUNTER, counter) })
        }
        override fun onCityItemViewClick(city: City) {
        }

        override fun onLongCityItemViewClick(city: City) {
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CityFiltersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterCitiesView.adapter = adapter
        binding.filterCitiesView.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.getLiveData().observe(viewLifecycleOwner, { data -> loadCitiesData(data) })
        refreshCities()
    }

    private fun refreshCities() {
        viewModel.getCities(null)
    }

    private fun loadCitiesData(state: AppState) {
        when (state) {
            is AppState.Success -> {
                binding.citiesLoading.visibility = View.GONE
                binding.filterCitiesView.isVisible = true

                state.appData.currentCity?.let {
                    val cityIndex = state.appData.cities.indexOf(state.appData.currentCity!!)
                    binding.filterCitiesView.smoothScrollToPosition(cityIndex)
                }
                adapter.setCities(state.appData.cities)
            }
            is AppState.Loading -> {
                context?.showLoading(R.string.loading_message)
            }
            is AppState.Error -> {
                binding.filterCitiesView.showSnackBar(getString(R.string.loading_error),
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