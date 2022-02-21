package com.geekbrains.myweather_kotlin1.presentation.history

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.databinding.HistoryFragmentBinding
import com.geekbrains.myweather_kotlin1.model.AppState
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.showSnackBar
import kotlinx.android.synthetic.main.history_fragment.*

@RequiresApi(Build.VERSION_CODES.O)
class HistoryFragment : Fragment() {

    companion object {
        const val CHECKED_CITIES = "cities"

        @JvmStatic
        fun newInstance(bundle: Bundle): HistoryFragment {
            val fragment = HistoryFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _binding: HistoryFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by lazy { ViewModelProvider(this)[HistoryViewModel::class.java] }
    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }

    private var filterCities : ArrayList<City>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = HistoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyFragmentRecyclerview.adapter = adapter

        viewModel.historyLiveData.observe(viewLifecycleOwner, Observer { renderData(it) })
        filterCities = arguments?.getParcelableArrayList(CHECKED_CITIES)
        getCityHistory()
    }

    private fun getCityHistory() {
        filterCities?.let { viewModel.getCitiesHistory(filterCities!!) }
        //viewModel.getCityHistory(requireContext()
            //.getSharedPreferences(Constants.CITIES_PREFERENCES, Context.MODE_PRIVATE))
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.historyFragmentRecyclerview.visibility = View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                adapter.setData(appState.appData.historyEntities)
            }
            is AppState.Loading -> {
                binding.historyFragmentRecyclerview.visibility = View.GONE
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.historyFragmentRecyclerview.visibility = View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                binding.historyFragmentRecyclerview.showSnackBar(
                    getString(R.string.loading_error),
                    getString(R.string.repeat_loading),
                    {
                        getCityHistory()
                    })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}