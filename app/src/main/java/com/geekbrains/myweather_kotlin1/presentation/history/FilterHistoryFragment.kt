package com.geekbrains.myweather_kotlin1.presentation.history

import CityFiltersFragment
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentResultListener
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.databinding.CityFiltersFragmentBinding
import com.geekbrains.myweather_kotlin1.databinding.FragmentFilterHistoryBinding
import com.geekbrains.myweather_kotlin1.databinding.HistoryFragmentBinding
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.presentation.weather.WeatherFragment
import com.geekbrains.myweather_kotlin1.view.CitiesFragment
import kotlinx.android.synthetic.main.fragment_filter_history.*

class FilterHistoryFragment : Fragment() {

    private var _binding: FragmentFilterHistoryBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var counter : Int = 0
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val bundle = Bundle()
        bundle.putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
        manager.beginTransaction()
            .replace(R.id.container, DetailsFragment.newInstance(bundle))
            .addToBackStack("")
            .commit()   */

        showCityFilters()
        binding.chbCityFilters.setOnCheckedChangeListener { _, _ -> showCityFilters() }

        parentFragmentManager.beginTransaction()
            .add(R.id.filters_fragment, CityFiltersFragment())
            .commit()
        parentFragmentManager.setFragmentResultListener(CityFiltersFragment.KEY_RESULT, viewLifecycleOwner,
            {_, result ->
                val counter = result.getInt(CityFiltersFragment.COUNTER)
                val cities = result.getParcelableArrayList<City>(CityFiltersFragment.CHECKED_CITIES)
                cities?.let {
                if (binding.chbCityFilters.isChecked) {
                    refreshHistory(it)
                }
            } })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshHistory(cities: ArrayList<City>) {
        val fragment = HistoryFragment.newInstance(Bundle().apply {
            putParcelableArrayList(HistoryFragment.CHECKED_CITIES, cities)
        })
        parentFragmentManager.beginTransaction()
            .replace(R.id.history_container, fragment)
            .commit()
    }

    fun showCityFilters() {
        filters_fragment.visibility = if (binding.chbCityFilters.isChecked) { View.VISIBLE } else { View.GONE }
    }
}